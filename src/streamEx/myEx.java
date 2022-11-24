package streamEx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class myEx {
	public static void main(String[] args) {
		List<Map<String, Object>> aa = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("이름", "이찬성");
		map.put("나이", "30");
		Map<String, Object> map1 = new HashMap<>();
		map1.put("이름", "홍길동");
		map1.put("나이", "20");
		Map<String, Object> map2 = new HashMap<>();
		map2.put("이름", "장비");
		map2.put("나이", "40");
		aa.add(map);
		aa.add(map1);
		aa.add(map2);
		System.out.println(aa);
		
		List<Map<String, Object>> bb = aa.stream().filter(x -> {
			int age = Integer.parseInt(x.get("나이").toString());
			return age > 20;
		}).peek(System.out::println).collect(Collectors.toList());
		
		System.out.println(bb);
		bb.forEach(row -> {
			row.forEach((key, value) -> {
				System.out.println(key + " : " + value);
			});
		});
		
		List<Integer> first = Arrays.asList(1, 5, 2, 4, 6, 1);
        List<Integer> second = Arrays.asList(1, 3, 5, 7);
        Set<Integer> set = first.stream().filter(x -> second.contains(x)).collect(Collectors.toSet());//중복을 피하기 위해서 set
        System.out.println(set);
        
        
	}
}
