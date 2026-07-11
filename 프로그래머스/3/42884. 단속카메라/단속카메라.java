import java.util.*;

class Solution {
    
    public int solution(int[][] routes) {
        int answer = 1;
        
        Arrays.sort(routes, (a, b) -> Integer.compare(a[1], b[1]));
        
        int prevStart = routes[0][0];
        int prevEnd = routes[0][1];
        
        int curCamPos = prevEnd;
        
        for (int i = 1; i < routes.length; i++) {
            int curStart = routes[i][0];
            int curEnd = routes[i][1];
            
            if (curStart <= curCamPos) continue;
            
            curCamPos = curEnd;
            answer++;
            
            prevStart = curStart;
            prevEnd = curEnd;
        }
        
        return answer;
    }
}