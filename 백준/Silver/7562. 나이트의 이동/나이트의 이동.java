import java.io.*;
import java.util.*;

public class Main {
    static int T, N;
    static int[][] board;
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringBuilder sb = new StringBuilder();

    static class Point{
        int y, x, moveCnt;

        public Point(int y, int x, int moveCnt) {
            this.y = y;
            this.x = x;
            this.moveCnt = moveCnt;
        }
    }

    public static void main(String[] args) throws IOException {
        T = Integer.parseInt(br.readLine());

        for (int i = 0; i < T; i++) {
            simulate();
        }

        System.out.println(sb);
        br.close();
    }

    public static void simulate() throws IOException {
        N = Integer.parseInt(br.readLine());
        board = new int[N][N];

        
        StringTokenizer st = new StringTokenizer(br.readLine());
        int startY = Integer.parseInt(st.nextToken());
        int startX = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int targetY = Integer.parseInt(st.nextToken());
        int targetX = Integer.parseInt(st.nextToken());

        int ret = bfs(startY, startX, targetY, targetX);
        sb.append(ret).append("\n");
    }

    // 나이트 이동 방향
    static int[] dy = {-1, -2, -2, -1, 1, 2, 2, 1};
    static int[] dx = {-2, -1, 1, 2, 2, 1, -1, -2};

    public static int bfs(int startY, int startX, int targetY, int targetX) {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][N];

        queue.offer(new Point(startY, startX, 0));
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.y == targetY && p.x == targetX) return p.moveCnt;

            for (int i = 0; i < 8; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N || visited[ny][nx]) continue;

                queue.offer(new Point(ny, nx, p.moveCnt + 1));
                visited[ny][nx] = true;
            }
        }

        return  -1;
    }
}
