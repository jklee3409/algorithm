import java.util.*;

class Solution {
    
    public int solution(int[][] maps) {
        int answer = bfs(maps, maps.length, maps[0].length);
        return answer;
    }
    
    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};
    
    int bfs(int[][] maps, int N, int M) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][M];
        
        q.offer(new int[]{0, 0, 1});
        visited[0][0] = true;
        
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            
            int y = cur[0];
            int x = cur[1];
            int dist = cur[2];
            
            if (y == N - 1 && x == M - 1) return dist;
            
            for (int d = 0; d < 4; d++) {
                int ny = cur[0] + dy[d];
                int nx = cur[1] + dx[d];
                
                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (maps[ny][nx] == 0 || visited[ny][nx]) continue;
                
                q.offer(new int[]{ny, nx, dist + 1});
                visited[ny][nx] = true;
            }
        }
        
        return -1;
    }
}