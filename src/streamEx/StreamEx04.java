package streamEx;

import java.util.Arrays;
import java.util.Collections;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class StreamEx04 {
	public static void main(String[] args) {
		
		// collect() vs reduce()
		/*
		 * reduce()
		 * 	리스트와 output타입이 같음
		 * 	이항 연산의 연쇄작용으로서 의미가 강함
		 * 	identity가 정말 identity(항등원)의 성질을 가지고 있는 겨우
		 * 
		 * collect()
		 * 	리스트와 타입이 다른 output을 내는 경우
		 * 
		 * 
		 * reduce의 accumulator(BinaryOperator<T>)는 return타입이 output타입과 동일
		 * collect의 accumulator(BiConsumer<R, ? super T>는 리턴타입이 void이다
		 * 즉, 연산결과가 리턴되지 않고 앞의 파라미터에 저장된다.
		 * 이는 "파라미터로 받은 데이터"를 보존할 것인가 변경할 것인가에 대한 문제다.
		 * 사실 collect가 supplier에서 인스턴스를 생성하여 그 생성한 인스턴스만을 변경하기때문에
		 * identity와 supplier가 생성한 인스턴스가 동일하게 항등원의 의미를 갖고 있다면
		 * 별 차이가 없으나, 내부 구현에 따라서 reduce가 identity를 보존해야하는 경우,
		 * 그리고 identity가 사실 identity의 의미를 갖지않은 경우라면 collect와 reduce의 용법은 달라질 수
		 * 있다.
		 * 
		 *supplier는 언제나 동일한 인스턴스를 생성
		 *identity는 연산의 시작접
		 */
		
		
		//Collecting
		
		//collect메소드는 또 다른 종료작업입니다.
		//Collector타입의 인자를 받아서 처리. 자주사용하는 작업은 Collectors객체에서 제공함
		
		List<Product> productList = 
				  Arrays.asList(new Product(23, "potatoes"),
				                new Product(14, "orange"),
				                new Product(13, "lemon"),
				                new Product(23, "bread"),
				                new Product(13, "sugar"));
		
		//Collectos.toList()는 스트림에서 작업한 결과를 담은 리스트로 반환.
		List<String> collectorCollection = 
				productList.stream()//스트림생성
				.map(Product::getName)//리스트 순회하며 product객체를 product.getName으로 대체
				.collect(Collectors.toList());//리스트 리턴
		System.out.println(collectorCollection.toString());
		
		//Collectors.joining()은 스트림에서 작업한 결과를 하나의 스트링으로 이어붙일 수 있습니다.
		//joining(delimiter구분자, prefix접두사, suffix접미사)
		String listToString = 
				productList.stream()
				.map(Product::getName)
				.collect(Collectors.joining());
		System.out.println(listToString.toString());
		
		String listToString2 = 
				productList.stream()
				.map(Product::getName)
				.collect(Collectors.joining(", ", "<", ">"));
		System.out.println(listToString2.toString());
		
		//Collectors.averageingInt() 숫자값의 평균을 냅니다.
		Double averageAmount = 
				productList.stream()
				.collect(Collectors.averagingInt(Product::getAmount));
		System.out.println(averageAmount);
		
		//Collectors.summingInt() 숫자값의 합
		Integer summingAmount = 
				productList.stream()
				.collect(Collectors.summingInt(Product::getAmount));
		System.out.println(summingAmount);
		
		//mapToInt
		Integer summingAmount2 = 
				productList.stream()
				.mapToInt(Product::getAmount)
				.sum();
		System.out.println(summingAmount2);
		
		//만약 합계와 평균 모두 필요하다면 summarizingInt를 사용하면 collect사용전에 통계작업을 위한 map을
		//호출할 필요가 없게 됨.
		IntSummaryStatistics statistics =
				productList.stream()
				.collect(Collectors.summarizingInt(Product::getAmount));
		System.out.println(statistics);//IntSummaryStatistics{count=5, sum=86, min=13, average=17.200000, max=23}
		System.out.println(statistics.getCount());
		System.out.println(statistics.getSum());
		System.out.println(statistics.getMin());
		System.out.println(statistics.getMax());
		System.out.println(statistics.getAverage());
		
		//Collectors.groupingBy()
		//특정 조건으로 요소들을 그룹지을 수 있다.
		//인자로 함수형인터페이스 Function을 받습니다.
		Map<Integer, List<Product>> collectorMapOfLists =
				productList.stream()
				.collect(Collectors.groupingBy(Product::getAmount));
		System.out.println(collectorMapOfLists.toString());
		
		//Collectors.partitioningBy()
		//groupingBy가 함수형 인터페이스 Function으로 특정값을 기준으로 스트림 내의 요소들을 묶었다면,
		//partitioningBy는 함수형 인터페이스 Predicate를 받습니다.
		//Predicate는 인자를 받아서 boolean값을 리턴합니다.
		//-> 따라서 평가를 하는 함수를 통해서 스트림 내 요소들을 true와 false 두가지로 나눌수 있습니다.
		Map<Boolean, List<Product>> mapPartitioned = productList.stream()
				.collect(Collectors.partitioningBy(el -> el.getAmount() > 15));
		System.out.println(mapPartitioned.toString());
		
		//Collectors.collectingAndThen()
		//특정 타입으로 결과를 collect한 후에 추가작업이 필요한 경우에 사용할 수 있습니다.
		/*
		 * public static<T,A,R,RR> Collector<T,A,RR> collectingAndThen(
		 *	  Collector<T,A,R> downstream,
		 *	  Function<R,RR> finisher) { ... }
		 */
		//이 메소드의 시그니쳐는 finisher가 추가된 모양인데, 이 피니셔는 collect를 한 후에 실행할 작업을 의미합니다.
		//다음 예제에서는 toSet을 이용하여 결과를 Set으로 Collect 한 후에 수정불가한 Set으로 변환하는 작업을 추가로
		//실행하는 코드입니다.
		Set<Product> unmodifiableSet = 
				productList.stream()
				.collect(Collectors.collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet));
		
		//직접 collector를 만들수도 있다.
		/**
		 * public static<T, R> Collector<T, R, R> of(
			  Supplier<R> supplier, // new collector 생성
			  BiConsumer<R, T> accumulator, // 두 값을 가지고 계산
			  BinaryOperator<R> combiner, // 계산한 결과를 수집하는 함수.
			  Characteristics... characteristics) { ... }
		 */
		Collector<Product, ?, LinkedList<Product>> toLinkedList = 
				Collector.of(
						LinkedList::new,//supplier에게 LinkedList 생성자를 넘겨줌
						LinkedList::add,//accumulator에게 add메소드를 넘겨줌
						(first, second) -> {//생성된 리스트들을 하나의 리스트로 함침
							first.addAll(second);
							return first;
						});
		LinkedList<Product> linkedListOfPerson = productList.stream().collect(toLinkedList);
		System.out.println(linkedListOfPerson.toString());
		
		//Matching
		//매칭은 조건식 람다 Predicate를 받아서 해당 조건을 만족하는 요소가 있는지 체크한 결과를 리턴
		//1. anyMatch : 하나라도 조건을 만족하는 요소가 있는지
		//2. allMatch : 모두 조건을 만족하는지
		//3. noneMatch : 모두 조건을 만족하지 않는지
		List<String> names = Arrays.asList("Eric", "Elena", "Java");
		
		boolean anyMatch = names.stream()
				.anyMatch(name -> name.contains("a"));
		System.out.println(anyMatch);
		
		boolean allMatch = names.stream()
				.allMatch(name -> name.length() > 3);
		
		boolean noneMatch = names.stream()
				.noneMatch(name -> name.endsWith("s"));
		
		//Iterating
		//forEach()는 요소를 돌면서 실행되는 최종작업입니다.
		//보통 프린트 메소드를 넘겨서 결과를 출력할때 사용하곤 합니다.
		//peek과는 중간작업과 최종작업의 차이가 있습니다.
		names.stream().forEach(System.out::println);
		
	}
}
