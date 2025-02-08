import java.util.*;

public class Solution {
    public int[] solution(int []arr) {
        int len = arr.length;
        int[] answer = new int[len];
        
        Stack<Integer> stack = new Stack<>();
        stack.push(arr[0]);
        
        for (int i = 1; i < len; i++){
            
            if (stack.peek() != arr[i]) stack.push(arr[i]);
        }
        
        answer = stack.stream().mapToInt(Integer::intValue).toArray();

        return answer;
    }
}