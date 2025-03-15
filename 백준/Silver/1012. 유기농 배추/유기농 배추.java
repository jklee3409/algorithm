import java.io.*;
import java.util.*;

public class Main {
    static int T , N, M, K;
    static int[][] field;
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
        T = Integer.parseInt(br.readLine());

        for (int i = 0; i < T; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            M = Integer.parseInt(st.nextToken()); // 가로
            N = Integer.parseInt(st.nextToken()); // 세로
            K = Integer.parseInt(st.nextToken()); // 배추 수
            field = new int[N][M];

            for (int j = 0; j < K; j++) {
                st = new StringTokenizer(br.readLine());

                int y = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());

                field[x][y] = 1; // 배추 기록
            }

            simulate();
        }

        System.out.println(sb);
        br.close();
    }

    // 배추 o && 아직 방문 x -> bfs 로 탐색하면서 방문 기록 (인접한 영역 표시)
    public static void simulate() {
        boolean[][] visited = new boolean[N][M];
        int cnt = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (field[i][j] == 0 || visited[i][j]) continue;

                bfs(i, j, visited);
                cnt++;
            }
        }

        sb.append(cnt).append("\n");
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static void bfs(int y, int x, boolean[][] visited) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0|| ny >= N || nx >= M) continue;
                if (visited[ny][nx] || field[ny][nx] == 0) continue;

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
            }
        }
    }
}
