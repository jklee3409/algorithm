import java.util.*;

class Solution {
    public int solution(int[][] board, int[] moves) {
        Stack<Integer> stack = new Stack<>();
        int count = 0;
        
        for (int i = 0; i < moves.length; i++) {
            int line = moves[i] - 1;
            
            for (int row = 0; row < board.length; row++) {
                int doll = board[row][line];
                
                if(doll == 0) continue;
                                
                if (!stack.isEmpty() && stack.peek() == doll) {
                    board[row][line] = 0;
                    stack.pop();
                    count += 2;
                    break;
                } else {
                    board[row][line] = 0;
                    stack.push(doll);
                    break;
                } 
            }
        }
        
        return count;
    }
}