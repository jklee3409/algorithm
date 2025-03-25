import java.io.*;
import java.util.*;

public class Main {
    static int H, N, M;
    static char[][][] building;
    static StringBuilder sb = new StringBuilder();

    static class Point {
        int z, y, x, time;

        public Point(int z, int y, int x, int time) {
            this.z = z;
            this.y = y;
            this.x = x;
            this.time = time;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            H = Integer.parseInt(st.nextToken()); // 층
            N = Integer.parseInt(st.nextToken()); // 행
            M = Integer.parseInt(st.nextToken()); // 열

            if (H == 0 && N == 0 && M == 0) { // 종료 조건
                System.out.println(sb);
                return;
            }

            building = new char[H][N][M];
            Point start = null;
            Point end = null;

            for (int i = 0; i < H; i++) {
                for (int j = 0; j < N; j++) {
                    String str = br.readLine();
                    for (int k = 0; k < M; k++) {
                        building[i][j][k] = str.charAt(k);

                        if (building[i][j][k] == 'S'){
                            start = new Point(i, j, k, 0);
                        } else if (building[i][j][k] == 'E') {
                            end = new Point(i, j, k, 0);
                        }
                    }
                }
                br.readLine();
            }

            bfs(start, end);
        }
    }

    static int[] dz = {-1, 1, 0, 0, 0, 0};
    static int[] dy = {0, 0, -1, 1, 0, 0};
    static int[] dx = {0, 0, 0, 0, -1, 1};

    public static void bfs(Point start, Point end) {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[][][] visited = new boolean[H][N][M];

        queue.offer(new Point(start.z, start.y, start.x, 0));
        visited[start.z][start.y][start.x] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (isArrived(p, end)) {
                sb.append("Escaped in ").append(p.time).append(" minute(s).").append("\n");
                return;
            }

            for (int i = 0; i < 6; i++) {
                int nz = p.z + dz[i];
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (!isValid(nz, ny, nx, visited)) continue;

                queue.offer(new Point(nz, ny, nx, p. time + 1));
                visited[nz][ny][nx] = true;
            }
        }

        sb.append("Trapped!").append("\n");
    }

    private static boolean isValid(int z, int y, int x, boolean[][][] visited) {

        if (z < 0 || y < 0 || x < 0) return false;

        if (z >= H || y >= N ||x >= M) return false;

        if(building[z][y][x] == '#' || visited[z][y][x]) return false;

        return true;
    }

    private static boolean isArrived(Point p, Point end) {
        return p.z == end.z && p.y == end.y && p.x == end.x;
    }

}