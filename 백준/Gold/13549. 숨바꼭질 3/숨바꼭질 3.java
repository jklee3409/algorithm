import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {

    static int N, K;

    static class Point {
        int x, t;

        public Point(int x, int t) {
            this.x = x;
            this.t = t;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        bfs();
    }

    // 순간이동 (0초), 걷기 (1초) 의 가중치가 다름 => 0-1 bfs
    public static void bfs() {
        Deque<Point> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[100001];

        queue.offer(new Point(N, 0));
        visited[N] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.x == K) {
                System.out.println(p.t);
                return;
            }

            int[] dx = {p.x, -1, 1};

            for (int i = 0; i < 3; i++) {
                int nx = p.x + dx[i];

                if (nx < 0 || nx >= visited.length || visited[nx]) continue;

                if (i == 0) queue.addFirst(new Point(nx, p.t)); // 순간 이동은 0초
                else queue.offer(new Point(nx, p.t + 1));

                visited[nx] = true;
            }
        }
    }
}
