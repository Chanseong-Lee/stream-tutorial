package streamEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamEx02 {
	public static void main(String[] args) {
		//가공하기
		List<String> names = Arrays.asList("Eric", "Elena", "Java");
		
		//Filtering
		//필터는 스트림 내 요소들을 하나씩 평가해서 걸러내는 작업. 
		//인자로 받는 Predicate<? super T>는 boolean을 리턴하는 인터페이스 
		Stream<String> stream = names.stream()
				.filter(name -> name.contains("a"));//[Elena, Java]
		
		//Mapping
		//맵은 스트림 내 요소들을 하나씩 특정 값으로 변환해줍니다.
		//이 때 값을 변환하기 위한 람다를 인자로 받습니다.
		//스트림에 들어가 있는 값이 input이 되어 특정 로직을 거친 후 output이 되어(리턴) 새로운 스트림에 담김.
		//이러한 작업을 매핑이라고 한다.
	
		//이중콜론연산자를 사용할 경우
		//이중콜론연산자는 자바8에 추가된 메소드 참조 연산자.
		// 인스턴스::메소드명(or new)
		// ex) User::getId
		//람다표현식에서만 사용가능, static메소드인 경우 인스턴스 대신 클래스이름으로 사용도 가능
		//
		Stream<String> stream2 = names.stream().map(x -> x.toUpperCase()); //[ERIC, ELENA, JAVA]
		Stream<String> stream3 = names.stream().map(String::toUpperCase); //[ERIC, ELENA, JAVA]
		
		//Flattening
		//flatMap은 컬렉션의 중첩구조를 한단계 제거하고 단일 컬렉션으로 만들어주는 역할을 함
		List<List<String>> listList = Arrays.asList(Arrays.asList("a"), Arrays.asList("b")); //[[a], [b]]
		List<String> flatList = listList.stream().flatMap(Collection::stream).collect(Collectors.toList());
		//객체 flattening으로 평균 구하기
		Student a = new Student(20, 30, 40);
		Student b = new Student(80, 90, 100);
		Student c = new Student(40, 40, 20);
		List<Student> students = new ArrayList<>();
		students.add(a);students.add(b);students.add(c);
		students.stream()
		.flatMapToInt(student -> IntStream.of(
				student.getEng(),
				student.getKor(),
				student.getMath()))
		.average().ifPresent(avg -> System.out.println(Math.round(avg * 10)/10.0));
		
		//Sorting
		//정렬은 다른 정렬과 마찬가지로, Comparator를 이용합니다.
		//인자가 없이 그냥 호출할 경우 오름차순으로 정렬합니다.
		IntStream.of(14, 11, 20, 39, 23)
		.sorted()
		.boxed()
		.collect(Collectors.toList()); //[11, 14, 20, 23, 39]
		
		//Comparator없이 알파벳순 정렬
		List<String> lang = Arrays.asList("Java", "Scala", "Groovy", "Python", "Go", "Swift");
		lang.stream()
		.sorted()
		.collect(Collectors.toList());// [Go, Groovy, Java, Python, Scala, Swift]
		
		lang.stream()
		.sorted(Comparator.reverseOrder())
		.collect(Collectors.toList());// [Swift, Scala, Python, Java, Groovy, Go]
		
		//Comparator의 compare()는 두인자를 비교해서 값을 리턴
		//기본적으로 Comparator사용법과 동일
		lang.stream()
		.sorted(Comparator.comparingInt(String::length))
		.collect(Collectors.toList());//[Go, Java, Scala, Swift, Groovy, Python]

		lang.stream()
		.sorted((s1, s2) -> s2.length() - s1.length())
		.collect(Collectors.toList());// [Groovy, Python, Scala, Swift, Java, Go]
		
		//Iterating
		//peek()은 리턴값이 없는 함수형 인터페이스 Consumer를 인자로 받음
		//따라서 스트림내부 요소들 각각에 특정 작업을  수행할 뿐 결과에 영향을 미치지는 않는다.
		//아래처럼 작업을 처리하는 중간에 결과를 확인해 볼때 사용할 수있다.
		int sum = IntStream.of(1,3,5,7,9)
				.peek(System.out::println)
				.sum();
		System.out.println(sum);
		
		
	}
}
