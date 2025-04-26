import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int[][] map;
    static int min = Integer.MAX_VALUE;

    static class Point {
        int y, x, distance;

        public Point(int y, int x, int distance) {
            this.y = y;
            this.x = x;
            this.distance = distance;
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
            }
        }

        simulate();
        System.out.println(min);
    }

    public static void simulate() {
        boolean[][] visited = new boolean[N][N];
        int code = 2;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j] || map[i][j] == 0) continue;

                separation(visited, i, j, code);
                code++;
            }
        }

        visited = new boolean[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (map[i][j] == 0) continue;

                code = map[i][j];
                min = Math.min(min, bfs(visited, i, j, code));
                visited = new boolean[N][N];
            }
        }
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    // 섬 분리
    public static void separation(boolean[][] visited, int y, int x, int code) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x, 0));
        visited[y][x] = true;
        map[y][x] = code;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                if (map[ny][nx] == 0 || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx, 0));
                visited[ny][nx] = true;
                map[ny][nx] = code;
            }
        }
    }

    public static int bfs(boolean[][] visited, int y, int x, int code) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x, 0));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            // 새로운 섬 도착
            if (map[p.y][p.x] != 0 && map[p.y][p.x] != code) return p.distance - 1;

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                if (map[ny][nx] == code || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx, p.distance + 1));
                visited[ny][nx] = true;
            }
        }

        return 100000;
    }
}
