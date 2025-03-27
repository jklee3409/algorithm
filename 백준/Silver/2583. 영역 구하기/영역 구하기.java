import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K;
    static int[][] paper;
    static List<Integer> result = new ArrayList<>();

    static class Point {
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        paper = new int[N][M];

        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());

            int x1 = Integer.parseInt(st.nextToken());
            int y1 = Integer.parseInt(st.nextToken());
            int x2 = Integer.parseInt(st.nextToken());
            int y2 = Integer.parseInt(st.nextToken());

            // 모눈종이 색칠
            // 주어진 입력과는 다르게 뒤집어서 칠해지지만 정답에는 영향 x
            for (int y = y1; y < y2; y++) {
                for (int x = x1; x < x2; x++) {
                    paper[y][x] = 1;
                }
            }
        }

        boolean[][] visited = new boolean[N][M];
        int cnt = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {

                if (paper[i][j] == 1 || visited[i][j]) continue;
                bfs(i, j, visited);
                cnt++;
            }
        }

        StringBuilder sb = new StringBuilder();
        Collections.sort(result);
        for (Integer i : result) {
            sb.append(i).append(" ");
        }

        System.out.println(cnt);
        System.out.println(sb);

        br.close();
    }

    static int dy[] = {-1, 1, 0, 0};
    static int dx[] = {0, 0, -1, 1};

    public static void bfs(int y, int x, boolean[][] visited) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        int area = 1;
        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0|| ny >= N || nx >= M) continue;
                if (paper[ny][nx] == 1 || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
                area++;
            }
        }

        result.add(area);
    }
}