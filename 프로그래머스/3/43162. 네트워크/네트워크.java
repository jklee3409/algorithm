import java.util.*;

class Solution {
    
    public int solution(int n, int[][] computers) {
        int answer = 0;
        boolean[] visited = new boolean[n];
        
        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;
            
            bfs(i, computers, visited);
            answer++;
        }
        
        return answer;
    }
    
    void bfs(int start, int[][] computers, boolean[] visited) {
        Queue<Integer> q = new ArrayDeque<>();
        
        q.offer(start);
        visited[start] = true;
        
        while(!q.isEmpty()) {
            int cur = q.poll();
            
            for (int next = 0; next < computers.length; next++) {
                if (computers[cur][next] == 0 || visited[next]) continue;
                
                q.offer(next);
                visited[next] = true;
            }
        }
    }
}