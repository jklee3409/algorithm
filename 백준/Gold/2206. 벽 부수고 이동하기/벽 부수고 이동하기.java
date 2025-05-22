import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int[][] map;

    static class Point {
        int y, x, step, brokenWall; // brokenWall: 0 (벽 부수지 않음), 1 (벽 부숨)

        public Point(int y, int x, int step, int brokenWall) {
            this.y = y;
            this.x = x;
            this.step = step;
            this.brokenWall = brokenWall;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][M];

        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = str.charAt(j) - '0';
            }
        }

        bfs();
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void bfs() {
        Queue<Point> queue = new ArrayDeque<>();
        int[][][] visited = new int[N][M][2];
        
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                visited[i][j][0] = Integer.MAX_VALUE; // 벽을 부수지 않고 이동한 최단 거리
                visited[i][j][1] = Integer.MAX_VALUE; // 벽을 부수고 이동한 최단 거리
            }
        }

        queue.offer(new Point(0, 0, 1, 0));
        visited[0][0][0] = 1; 

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            
            if (p.y == N - 1 && p.x == M - 1) {
                System.out.println(p.step);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                
                if (map[ny][nx] == 1) { // 이동하려는 곳이 벽이라면
                   
                    if (p.brokenWall == 0) { // 벽을 부술 수 있는 경우
                        
                        if (visited[ny][nx][1] > p.step + 1) {
                            
                            visited[ny][nx][1] = p.step + 1;
                            queue.offer(new Point(ny, nx, p.step + 1, 1)); 
                        }
                    }
                    
                } else {
                    
                    if (p.brokenWall == 0) {
                        
                        if (visited[ny][nx][0] > p.step + 1) {
                            
                            visited[ny][nx][0] = p.step + 1;
                            queue.offer(new Point(ny, nx, p.step + 1, 0)); 
                        }
                        
                    } else {
                        
                        if (visited[ny][nx][1] > p.step + 1) {
                            
                            visited[ny][nx][1] = p.step + 1;
                            queue.offer(new Point(ny, nx, p.step + 1, 1)); 
                        }
                    }
                }
            }
        }

        System.out.println(-1);
    }
}