package streamEx;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StreamEx01 {
	public static void main(String[] args) throws IOException {
		//stream은 1. 생성 2. 가공 3. 결과
		
		//배열 스트림
		String[] arr = new String[] {"a", "b", "c"};
		//스트림 생성
		Stream<String> stream = Arrays.stream(arr);
		Stream<String> streamOfArrayPart = Arrays.stream(arr, 1, 3); //1~2요소[b, c]
		
		//컬렉션 스트림
		List<String> list = Arrays.asList("a", "b", "c");
		Stream<String> stream2 = list.stream();
		Stream<String> parallelStream = list.parallelStream(); //병렬처리 스트림
		
		//Stream builder를 이용하여 직접적으로 원하는 값을 넣기. 마지막에 build()로 스트림을 리턴 
		Stream<String> builderStream = Stream.<String>builder()
				.add("Eric").add("Elena").add("Java")
				.build();
		
		/*
		 * Stream.generate()를 이용하여 매개변수에 람다로 값을 넣을 수 있다.
		 * public static<T> Stream<T> generate(Supplier<T> s)
		 * generate로 생성되는 스트림은 크기가 정해져 있지 않고 무한하기 때문에
		 * 특정 사이즈로 최대 크기를 제한해야한다.
		 */
		Stream<String> generatedStream = Stream.generate(() -> "gen").limit(5); //[gen, gen, gen, gen, gen]
		
		/*
		 * Stream.iterate()를 이용하면 초기값과 해당값을 다루는 람다를 이용해서 스트림에 들어갈 요소를 만듬
		 */
		Stream<Integer> iteratedStream = Stream.iterate(30, n -> n + 2).limit(5); //[30, 32, 34, 36, 38]
		
		//기본타입형 스트림
		IntStream intStream = IntStream.range(1, 5); //[1, 2, 3, 4]
		IntStream intStream2 = IntStream.rangeClosed(1, 5); //[1, 2, 3, 4, 5]
		
		//기본타입형 스트림은 제네릭을 사용하지않아 오토박싱을 하지 않는다.
		//그러므로 마지막에 boxed()로 박싱을 해주면 제네릭스트림에 담을 수 있다.
		Stream<Integer> boxedIntStream = IntStream.range(1, 5).boxed();
		
		//Random클래스로 세가지 타입의 스트림(IntStream, LongStream, DoubleStream)을 만들 수 있음
		//각각의 타입으로 난수 3개 생성
		DoubleStream doubles = new Random().doubles(3);
		IntStream ints = new Random().ints(3);
		LongStream longs = new Random().longs(3);
		
		//문자열 스트림
		//String의 각 문자[char]를 intStream으로 변환
		IntStream charsStream = "Stream".chars();
		
		//RegEx를 이용하여 문자열 자르고, 스트림을 만들기
		Stream<String> stringStream = Pattern.compile(", ").splitAsStream("Eric, Elena, Java");
		
		//파일 스트림
		Stream<String> lineStream = Files.lines(Paths.get("file.txt"), Charset.forName("UTF-8"));
		
		//병렬스트림 parallel stream
		// Stream 생성시 사용하는 stream() 대신 parallelStream()으로 병렬스트림생성 가능
		// 내부적으로는 쓰레드를 처리 하기 위하여, 자바7부터 도입된 Fork/Join framework를 사용
		
		//스트림 연결하기
		Stream<String> stream3 = Stream.of("Java", "Scala", "Groovy");
		Stream<String> stream4 = Stream.of("Python", "Go", "Swift");
		Stream<String> concat3and4 = Stream.concat(stream3, stream4);
		
		
		
		
		
	}
}
