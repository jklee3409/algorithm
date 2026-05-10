import java.util.*;

class Solution {
    
    String[] answer;
    boolean[] visited;
    
    
    public String[] solution(String[][] tickets) {
        Arrays.sort(tickets, (a, b) -> {
            if(a[0].equals(b[0])) return a[1].compareTo(b[1]);
            return a[0].compareTo(b[0]);
        });
        
        answer = new String[tickets.length + 1];
        visited = new boolean[tickets.length];
        
        dfs(tickets, "ICN", 0);
        
        return answer;
    }
    
    boolean dfs(String[][] tickets, String current, int depth) {
        answer[depth] = current;
        
        if (depth == tickets.length) return true;
        
        for (int i = 0; i < tickets.length; i++) {
            if (visited[i]) continue;
            
            if (tickets[i][0].equals(current)) {
                visited[i] = true;
                
                if (dfs(tickets, tickets[i][1], depth + 1)) {
                    return true;
                }
                
                visited[i] = false;
            }
        }
        
        return false;
    }
}