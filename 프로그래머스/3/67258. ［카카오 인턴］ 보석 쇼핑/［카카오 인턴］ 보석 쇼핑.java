import java.util.*;

class Solution {
    
    public int[] solution(String[] gems) {
        Set<String> set = new HashSet<>(Arrays.asList(gems));
        int totalTypesCnt = set.size();
        
        Map<String, Integer> map = new HashMap<>();
        
        int left = 0;
        int minLength = Integer.MAX_VALUE;
        
        int answerLeft = 0;
        int answerRight = 0;
        
        for (int right = 0; right < gems.length; right++) {
            map.put(gems[right], map.getOrDefault(gems[right], 0) + 1);
            
            if (map.size() == totalTypesCnt) {
                
                while (map.get(gems[left]) > 1) {
                    map.put(gems[left], map.get(gems[left]) - 1);
                    left++;
                }

                int length = right - left + 1;

                if (length < minLength) {
                    minLength = length;
                    answerLeft = left;
                    answerRight = right;
                }
            }
        }
        
        return new int[] {answerLeft + 1, answerRight + 1};
    }
}