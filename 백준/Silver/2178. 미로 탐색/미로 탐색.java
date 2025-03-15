import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] maze;

    static class Point{
        int y, x, distance;

        public Point(int y, int x, int distance){
            this.y = y;
            this.x = x;
            this.distance = distance;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        maze = new int[N][M];

        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < M; j++) {
                maze[i][j] = str.charAt(j) - '0';
            }
        }

        bfs();
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void bfs() {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][M];
        queue.offer(new Point(0, 0, 1));
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.y == N - 1 && p.x == M - 1) {
                System.out.println(p.distance);
                return;
            }

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (visited[ny][nx] || maze[ny][nx] == 0) continue;

                queue.offer(new Point(ny, nx, p.distance + 1));
                visited[ny][nx] = true;
            }
        }
    }
}
