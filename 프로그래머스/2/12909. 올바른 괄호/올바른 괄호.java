import java.util.*;

class Solution {
    boolean solution(String s) {
        Stack<Character> stack = new Stack<>();

        int idx = 0;
        
        while (idx < s.length()) {
            if (s.charAt(idx) == '(') stack.push('(');
            else {
                char cur = ' ';
                if (!stack.isEmpty()){
                    cur = stack.pop();
                } else {
                    return false;
                }
                
                if (cur != '(') return false;
            }
            
            idx++;
        }
        
        if (!stack.isEmpty()) return false;

        return true;
    }
}