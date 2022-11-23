package streamEx;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StreamEx04 {
	public static void main(String[] args) {
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
		Map<Integer, List<Product>> collectorMapOfLists =
				productList.stream()
				.collect(Collectors.groupingBy(Product::getAmount));
		System.out.println(collectorMapOfLists.toString());
		
		//https://futurecreator.github.io/2018/08/26/java-8-streams/
	}
}
