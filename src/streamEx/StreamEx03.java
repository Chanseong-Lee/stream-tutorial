package streamEx;

import java.util.Arrays;
import java.util.OptionalInt;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamEx03 {
	public static void main(String[] args) {
		//결과 만들기
		//가공한 스트림을 가지고 내가 사용할 결과값으로 만들어내는 단계입니다.
		//따라서 스트림을 끝내는 최종작입입니다.(terminal operations)
		
		//calculating
		//Stream api는 다양한 종료작업을 제공합니다. 최소, 최대, 합, 평균 등 기본형 타입으로 결과를 만듬.
		long count = IntStream.of(1,3,5,7,9).count();
		long sum = LongStream.of(1,3,5,7,9).sum();
		
		//만약 스트림이 비어있는 경우 count와 sum 은 0을 출력하면됩니다.
		//하지만 평균, 최소, 최대의 경우에는 표현할 수가 없기 때문에 Optional을 이용해 리턴합니다.
		OptionalInt min = IntStream.of(1,3,5,7,9).min();
		OptionalInt max = IntStream.of(1,3,5,7,9).max();
		
		//Stream에서 바로 ifPresent()를 메소드를 이용해서 Optional을 처리할 수 있습니다.
		DoubleStream.of(1.1, 2.2, 3.3, 4.4, 5.5).average().ifPresent(System.out::println);
		
		//이 외에도 사용자가 원하는대로 결과를 만들어내기 위해 reduce와 collect메소드를 제공합니다.
		
		//Reduction
		//스트림은 reduce라는 메소드를 이용해서 결과를 만들어 냅니다.
		//람다예제에서 살펴봤듯이 스트림에 있는 여러 요소의 총합을 낼 수도 있습니다.
		//reduce메소드는 총 세가지의 파라미터를 받을 수 있습니다.
		// - accumulator : 각 요소를 처리하는 계산 로직, 각요소가 올때마다 중간결과를 생성하는 로직
		// - identity : 계산을 위한 초기값으로 스트림이 비어서 계산할 내용이 없더라도 이값은 리턴
		// - combiner : 병렬스트림에서 나눠 계산한 결과를 하나로 합치는 동작하는 로직
		
		
		//먼저 인자가 하나만 있는 경우 -> accumulator만(같은타입의 인자 두개를 받아 같은타입의 결과를 반환)
		OptionalInt reduced = 
				IntStream.range(1, 4) //[1,2,3]
				.reduce((a, b) -> {
					return Integer.sum(a, b);
				}); // 6 (1+2+3)
		
		//인자가 두개인 경우 -> identity(초기값), accumulator
		int reducedTwoParams = IntStream.range(1, 4)
				.reduce(10, Integer::sum); //메소드참조형태, 16 (10+1+2+3)
		
		//인자가 세개인경우는 마지막 인자인 combiner는 병렬스트림일 경우에만 실행됨
		Integer reducedParams = Stream.of(1, 2, 3)
				.reduce(10,
						Integer::sum,
						(a, b) -> {
							System.out.println(a+b);
							return a+b;
						}); //combiner작동안함
		
		
		Integer reducedParallelParams = Arrays.asList(1, 2, 3).parallelStream()
				.reduce(10,
						Integer::sum,
						(a, b) -> {
							System.out.println(a+b);
							return a+b;
						}); // 36
		// 값이 36인 이유는 먼저 accumulator는 총 세번 동작함.
		// 초기값 10에 각 스트림 값을 더한 세개의 값(10+1, 10+2, 10+3)을 계산한다.
		// combiner는 identity와 accumulator를 가지고 여러 쓰레드에서 나눠 계산한 결과를 합치는 역할을 함.
		// 12 + 13 = 25,  25 + 11 = 36
		
		//병렬 스트림이 무조건 시퀀셜보다 좋은것은 아니다. 
		//오히려 간단한 경우에는 이렇게 부가적인 처리가 필요하기 때문에 오히려 느릴 수도 있습니다.
				
		
	}
}
