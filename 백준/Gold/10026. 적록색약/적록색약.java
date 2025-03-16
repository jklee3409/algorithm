import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static char[][] graph;
    static StringBuilder sb = new StringBuilder();

    static class Point{
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        graph = new char[N][N];

        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < N; j++) {
                graph[i][j] = str.charAt(j);
            }
        }

        simulate();
    }

    static void simulate() {
        // 적록색약 x
        boolean[][] visited = new boolean[N][N];
        int cnt = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j]) continue;

                char c = graph[i][j];
                bfs_x(i, j, c, visited);
                cnt++;
            }
        }

        sb.append(cnt).append(" ");

        visited = new boolean[N][N];
        cnt = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (visited[i][j]) continue;

                char c = graph[i][j];
                bfs_o(i, j, c, visited);
                cnt++;
            }
        }

        sb.append(cnt);
        System.out.println(sb);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void bfs_x(int y, int x, char c, boolean[][] visited) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                if (graph[ny][nx] != c || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
            }
        }
    }

    static void bfs_o(int y, int x, char c, boolean[][] visited) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N || visited[ny][nx]) continue;

                if (c == 'R' || c == 'G') { // 적색 or 녹색
                    if (graph[ny][nx] == 'B') continue; // 적색과 녹색은 같은 색으로 취급
                } else { // 파란색
                    if (graph[ny][nx] != c) continue;
                }

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
            }
        }
    }
}
