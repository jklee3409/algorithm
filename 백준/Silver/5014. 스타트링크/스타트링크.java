import java.io.*;
import java.util.*;

public class Main {
    static int F, S, G, U, D;

    static class Point {
        int floor, cnt;

        public Point(int floor, int cnt) {
            this.floor = floor;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        F = Integer.parseInt(st.nextToken()); // 건물의 층 수
        S = Integer.parseInt(st.nextToken()); // 강호의 위치
        G = Integer.parseInt(st.nextToken()); // 스타트링크 위치
        U = Integer.parseInt(st.nextToken()); // Up
        D = Integer.parseInt(st.nextToken()); // Down

        bfs();
        br.close();
    }

    public static void bfs() {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[F + 1];

        queue.offer(new Point(S, 0));
        visited[S] = true;

        int[] dy = {U, -D};

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.floor == G) {
                System.out.println(p.cnt);
                return;
            }

            for (int i = 0; i < 2; i++) {
                int ny = p.floor + dy[i];

                if (ny < 1 || ny > F || visited[ny]) continue;

                queue.offer(new Point(ny, p.cnt + 1));
                visited[ny] = true;
            }
        }

        System.out.println("use the stairs");
    }
}
