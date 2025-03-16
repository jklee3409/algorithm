import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] map;

    static class Iceberg {
        int y, x;
        int height;

        public Iceberg(int y, int x, int height) {
            this.y = y;
            this.x = x;
            this.height = height;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int time = 0;
        while (true) {
            boolean[][] visited = new boolean[N][M];
            int cnt = 0;
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    if (map[i][j] <= 0 || visited[i][j]) continue;

                    bfs(i, j , visited);
                    cnt++;

                    if (cnt >= 2) {
                        System.out.println(time);
                        return;
                    }
                }
            }

            simulate();
            time++;

            if (isEmpty()) {
                System.out.println(0);
                return;
            }
        }
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void simulate() {
        int[][] temp = new int[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (map[i][j] <= 0) continue;

                int oceanCnt = 0;
                for (int k = 0; k < 4; k++) {
                    int ny = i + dy[k];
                    int nx = j + dx[k];

                    if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                    if (map[ny][nx] > 0) continue;

                    oceanCnt++;
                }

                temp[i][j] = oceanCnt;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                map[i][j] -= temp[i][j];
            }
        }
    }

    static void bfs(int y, int x, boolean[][] visited) {
        Queue<Iceberg> queue = new ArrayDeque<>();

        queue.offer(new Iceberg(y, x, map[y][x]));
        visited[y][x] = true;

        while (!queue.isEmpty()) {
            Iceberg iceberg = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = iceberg.y + dy[i];
                int nx = iceberg.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (map[ny][nx] <= 0 || visited[ny][nx]) continue;

                queue.offer(new Iceberg(ny, nx, map[ny][nx]));
                visited[ny][nx] = true;
            }
        }
    }

    static boolean isEmpty() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if(map[i][j] > 0) return false;
            }
        }

        return true;
    }

}
