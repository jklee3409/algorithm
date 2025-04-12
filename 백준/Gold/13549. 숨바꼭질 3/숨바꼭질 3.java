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

            // 순간이동 먼저 처리
            int teleport = p.x * 2;
            if (teleport < 100001 && !visited[teleport]) {
                queue.addFirst(new Point(teleport, p.t));
                visited[teleport] = true;
            }

            // 걷기는 나중에 처리
            for (int next : new int[]{p.x - 1, p.x + 1}) {
                if (next < 0 || next >= 100001 || visited[next]) continue;
                queue.addLast(new Point(next, p.t + 1));
                visited[next] = true;
            }
        }
    }
}
