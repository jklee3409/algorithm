import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int maxHeight = 0, maxSafeArea = 0;
    static int[][] map;

    static class Point {
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        map = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                maxHeight = Math.max(maxHeight, map[i][j]);
            }
        }

        for (int i = 0; i <= maxHeight; i++) {
            simulate(i);
        }

        System.out.println(maxSafeArea);
        br.close();
    }

    public static void simulate(int rain) {
        boolean[][] visited = new boolean[N][N];
        int safeArea = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                if (map[i][j] <= rain || visited[i][j]) continue;
                bfs(i, j, rain, visited);
                safeArea++;
            }
        }

        maxSafeArea = Math.max(maxSafeArea, safeArea);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void bfs(int y, int x, int rain, boolean[][] visited) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                if (map[ny][nx] <= rain || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
            }
        }
    }
}
