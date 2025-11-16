import java.io.*;
import java.util.*;

public class Main {
    static int n, m;
    static int[][] map, result;

    public static class Point {
        int y, x, len;

        Point(int y, int x, int len) {
            this.y = y;
            this.x = x;
            this.len = len;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        result = new int[n][m];

        int startY = 0, startX = 0;
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                int input = Integer.parseInt(st.nextToken());
                map[i][j] = input;

                if (input == 2) {
                    result[i][j] = 0;
                    startY = i;
                    startX = j;
                } else if (input == 0) {
                    result[i][j] = 0;
                } else {
                    result[i][j] = -1;
                }
            }
        }

        bfs(startY, startX);

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sb.append(result[i][j]).append(" ");
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void bfs(int startY, int startX) {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];

        queue.offer(new Point(startY, startX, 0));
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (map[p.y][p.x] == 1) result[p.y][p.x] = p.len;

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= n || nx >= m) continue;
                if (visited[ny][nx] || map[ny][nx] == 0) continue;

                queue.offer(new Point(ny, nx, p.len + 1));
                visited[ny][nx] = true;
            }
        }

    }
}
