import java.io.*;
import java.util.*;

public class Main {
    static int N, K;

    static class Point{
        int pos, time;

        public Point(int pos, int time) {
            this.pos = pos;
            this.time = time;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        bfs();
    }

    static void bfs() {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[100001];

        queue.offer(new Point(N, 0));
        visited[N] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.pos == K) {
                System.out.println(p.time);
                return;
            }

            int[] d = {p.pos, 1, -1};

            for (int i = 0; i < 3; i++) {
                int nPos = p.pos + d[i];

                if (nPos < 0 || nPos >= visited.length || visited[nPos]) continue;

                queue.offer(new Point(nPos, p.time + 1));
                visited[nPos] = true;
            }
        }
    }
}
