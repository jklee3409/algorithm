import java.util.*;

class Solution {
    
    int[] dy = {-1, 1, 0, 0};
    int[] dx = {0, 0, -1, 1};
    
    int[][] map = new int[102][102];
    boolean[][] visited = new boolean[102][102];
    
    public int solution(int[][] rectangle, int characterX, int characterY, int itemX, int itemY) {
        
        // 직사각형 전부 1로 칠하기
        for (int[] r : rectangle) {
            int x1 = r[0] * 2;
            int y1 = r[1] * 2;
            int x2 = r[2] * 2;
            int y2 = r[3] * 2;
            
            for (int y = y1; y <= y2; y++) {
                for (int x = x1; x <= x2; x++) {
                    map[y][x] = 1;
                }
            }
        }
        
        // 직사각형 내부 0으로 칠하기
        for (int[] r : rectangle) {
            int x1 = r[0] * 2;
            int y1 = r[1] * 2;
            int x2 = r[2] * 2;
            int y2 = r[3] * 2;
            
            for (int y = y1 + 1; y < y2; y++) {
                for (int x = x1 + 1; x < x2; x++) {
                    map[y][x] = 0;
                }
            }
        }
        
        return bfs(map, characterY * 2, characterX * 2, itemY * 2, itemX * 2);
    }
    
    int bfs(int[][] map, int startY, int startX, int itemY, int itemX) {
        Queue<int[]> q = new ArrayDeque<>();
        
        q.offer(new int[]{startY, startX, 0});
        visited[startY][startX] = true;
        
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int y = cur[0];
            int x = cur[1];
            int dist = cur[2];
            
            if (y == itemY && x == itemX) return dist / 2;
            
            for (int d = 0; d < 4; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];
                
                if (ny < 1 || nx < 1 || ny > 100 || nx > 100) continue;
                if (visited[ny][nx] || map[ny][nx] == 0) continue;
                
                q.offer(new int[]{ny, nx, dist + 1});
                visited[ny][nx] = true;
            }
        }
        
        return -1;
    }
}