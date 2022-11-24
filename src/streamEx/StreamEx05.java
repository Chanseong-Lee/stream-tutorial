package streamEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamEx05 {
	
	private static long counter;
	private static void wasCalled() {
		counter++;
	}
	public static <T> Stream<T> collectionToStream(Collection<T> collection) {
	    return Optional
	      .ofNullable(collection)
	      .map(Collection::stream)
	      .orElseGet(Stream::empty);
	  } 
	public static void main(String[] args) {
		/*
		 * 동작순서 
		 * 성능향상 
		 * 스트림 
		 * 재사용 
		 * 지연처리 
		 * Null-safe스트림 생성 
		 * 줄여쓰기
		 */

		// 동작순서
		/*
		 * 1. 처음 요소인 “Eric” 은 “a” 문자열을 가지고 있지 않기 때문에 다음 요소로 넘어갑니다. 
		 * 	  이 때 “filter() was called.” 가 한 번 출력됩니다.
		 * 2. 다음 요소인 “Elena” 에서 "filter() was called."가 한 번 더 출력됩니다. 
		 *    "Elena"는 "a"를 가지고 있기 때문에 다음 연산으로 넘어갈 수 있습니다.
		 * 3. 다음 연산인 map 에서 toUpperCase 메소드가 호출됩니다. 
		 *    이 때 "map() was called"가 출력됩니다.
         * 4. 마지막 연산인 findFirst 는 첫 번째 요소만을 반환하는 연산입니다. 
         *    따라서 최종 결과는 “ELENA” 이고 다음 연산은 수행할 필요가 없어 종료됩니다.
		 */
		List<String> names = new ArrayList<>();
		names.add("Eric");
		names.add("Elena");
		names.add("Java");
		
		names.stream().filter(el -> {
			System.out.println("필터호출! : " + el);
			return el.contains("a");
		}).map(el -> {
			System.out.println("맵호출! : " + el);
			return el.toUpperCase();
		}).findFirst();
		
		//성능향상 : 스트림은 한요소씩 수직적으로 실행됨. 그러므로 요소의 범위를 줄이는 작업을 먼저
		//실행하면 불필요한 연산을 막을 수 있다.(skip, filter, distinct)
		names.stream()
		.map(el -> {
			System.out.println("case1");
			return el.substring(0, 3);
		})
		.skip(2)
		.collect(Collectors.toList());
		
		names.stream()
		.skip(2)
		.map(el -> {
			System.out.println("case2");
			return el.substring(0, 3);
		})
		.collect(Collectors.toList());
		
		//스트림 재사용
		//종료작업을 하지 않는 한 하나의 인스턴스로서 계속해서 사용이 가능합니다.
		//하지만 종료작업을 하는 순간, 스트림이 닫히기 때문에 재사용은 할수 없습니다.
		//스트림은 저장된 데이터를 꺼내서 처리하는 용도이지 데이터를 저장하려는 목적으로 설계되지
		// 않았기 때문입니다.
		Stream<String> stream = Stream.of("Eric", "Elena", "Java")
				.filter(name -> name.contains("a"));
		Optional<String> firstElement = stream.findFirst();
//		Optional<String> anyElement = stream.findAny(); //Runtime Exception(IllegalStateException: stream has already been operated upon or closed)
		//findFirst로 이미 스트림이 종료되었기 때문에 런타임 에러가 발생함
		//그러므로 아래처럼 데이터를 List에 저장하고 필요할때마다 스트림을 생성해서 사용합니다.
		List<String> names2 = Stream.of("Eric", "Elena", "Java")
				.filter(name -> name.contains("a"))
				.collect(Collectors.toList());
		Optional<String> firstElement2 = names2.stream().findFirst();
		Optional<String> anyElement2 = names2.stream().findAny(); 
		
		//지연처리
		//스트림에서 최종결과는 최종작업이 이루어질때 계산됩니다.
		//다음예제에서 리스트의 요소가 3개이기 때문에 총 세번 호출되어 결과가 3이 출력될것같지만
		// 결과는 0입니다.
		counter = 0;
		Stream<String> st = names.stream()
				.filter(el -> {
					wasCalled();
					return el.contains("a");
				});
		System.out.println(counter); //0
		//최종작업이 실행되지 않아서 실제로 스트림의 연산이 실행되지 않음.
		//최종작업인 collect가 호출되어 결과가 3으로 나옴
		names.stream().filter(el -> {
			  wasCalled();
			  return el.contains("a");
			}).collect(Collectors.toList());
		System.out.println(counter); // 3
			
		//Null-safe 스트림 생성하기
		//NullPointerException은 개발시 흔히 발생하는 예외입니다.
		// Optinal을 이용해서 null에 안전한 스트림을 생성해보겠습니다.
		/*
		 * public <T> Stream<T> collectionToStream(Collection<T> collection) {
			    return Optional
			      .ofNullable(collection)
			      .map(Collection::stream)
			      .orElseGet(Stream::empty);
  			} 
		 */
		
		//위 코드는 인자로 받은 컬랙션 객체를 이용해 옵셔널 객체를 만들고 스트림을 생성후 리턴하는 메소드입니다.
		// 그리고 만약 컬렉션이 비어있는 경우라면, 빈스트림을 리턴하도록 합니다.
		//제네릭을 이용해 어떤 타입이든 받을 수 있습니다.
		List<Integer> intList = Arrays.asList(1,2,3);
		List<String> strList = Arrays.asList("a", "b", "c");
		
		Stream<Integer> intStream = collectionToStream(intList);
		Stream<String> strStream = collectionToStream(strList);
		
		//null로 테스트하기
		List<String> nullList = null;
//		nullList.stream()
//		.filter(str -> str.contains("a"))
//		.map(String::length)
//		.forEach(System.out::println);//NPE!!
		
		//위에서 만든 메소드를 사용하면 null처리가 되어 빈스트림을 리턴한다.
		collectionToStream(nullList)
		.filter(str -> str.contains("a"))
		.map(String::length)
		.forEach(System.out::println); //[]
	}
}
