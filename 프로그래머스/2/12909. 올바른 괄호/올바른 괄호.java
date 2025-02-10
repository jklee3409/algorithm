import java.util.*;

class Solution {
    boolean solution(String s) {
        Stack<Character> stack = new Stack<>();

        int idx = 0;
        
        while (idx < s.length()) {
            if (s.charAt(idx) == '(') stack.push('(');
            else {
                if(stack.isEmpty()) return false;
                
                if (stack.pop() != '(') return false;
            }
            
            idx++;
        }
        
        if (!stack.isEmpty()) return false;

        return true;
    }
}