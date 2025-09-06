import java.io.*;
import java.util.*;

public class Main {

    static class Point {
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int M = Integer.parseInt(st.nextToken());
            int N = Integer.parseInt(st.nextToken());

            if (N == 0 && M == 0) break;

            int[][] map = new int[N][M];
            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < M; j++) {
                    map[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            int count = 0;
            boolean[][] visited = new boolean[N][M];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (visited[i][j] || map[i][j] == 0) continue;
                    bfs(map, visited, i, j, N, M);
                    count++;
                }
            }

            sb.append(count).append("\n");
        }

        System.out.println(sb);
    }

    static int[] dy = {-1, 1, -1, 1, -1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1, 1, -1, -1, 1};

    static void bfs(int[][] map, boolean[][] visited, int startY, int startX, int N, int M) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.offer(new Point(startY, startX));
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int d = 0; d < 8; d++) {
                int ny = p.y + dy[d];
                int nx = p.x + dx[d];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (visited[ny][nx] || map[ny][nx] == 0) continue;

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
            }
        }
    }
}
