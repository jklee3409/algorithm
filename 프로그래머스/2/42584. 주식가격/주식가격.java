import java.util.*;

class Solution {
    public int[] solution(int[] prices) {
        int len = prices.length;
        int[] result = new int[len];
        
        for(int i = 0; i < len; i++) {            
            int time = 0;
            int curPrice = prices[i];
            
            for (int j = i + 1; j < len; j++) {
                int afterPrice = prices[j];

                if (curPrice <= afterPrice) { 
                    time++;
                } else {
                    time++;
                    break;
                }
            }
             result[i] = time;
        }
        
        return result;
    }
}