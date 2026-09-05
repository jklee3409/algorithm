import java.util.*;

class Solution {

    public int[] solution(String[] gems) {
        Set<String> types = new HashSet<>(Arrays.asList(gems));
        int totalTypeCount = types.size();

        Map<String, Integer> count = new HashMap<>();

        int left = 0;
        int minLength = Integer.MAX_VALUE;
        
        int answerLeft = 0;
        int answerRight = 0;

        for (int right = 0; right < gems.length; right++) {
            count.put(gems[right], count.getOrDefault(gems[right], 0) + 1);

            if (count.size() == totalTypeCount) {

                while (count.get(gems[left]) > 1) {
                    count.put(gems[left], count.get(gems[left]) - 1);
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