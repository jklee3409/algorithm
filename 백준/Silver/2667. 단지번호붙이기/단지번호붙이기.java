import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][]houses;

    static class Point {
        private int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        houses = new int[N][N];

        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < N; j++) {
                houses[i][j] = str.charAt(j) - '0';
            }
        }

        boolean[][] visited = new boolean[N][N];
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                if (houses[i][j] == 0 || visited[i][j]) continue;

                bfs(i, j, visited, result);
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append(result.size()).append("\n");

        Collections.sort(result);
        for (Integer i : result) {
            sb.append(i).append("\n");
        }

        System.out.println(sb);
        br.close();
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void bfs(int y, int x, boolean[][] visited, List<Integer> result) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        int cnt = 1;
        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                if (houses[ny][nx] == 0 || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx));
                visited[ny][nx] = true;
                cnt++;
            }
        }

        result.add(cnt);
    }
}
