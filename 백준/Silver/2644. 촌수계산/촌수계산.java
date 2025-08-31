import java.io.*;
import java.util.*;

public class Main {
    static List<List<Integer>> graph = new ArrayList<>();
    static int[] parent;
    static int N, M, A, B;

    public static class Point {
        int n, len;

        public Point(int n, int len) {
            this.n = n;
            this.len = len;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N + 1; i++) graph.add(new ArrayList<>());
        parent = new int[N + 1];

        StringTokenizer st = new StringTokenizer(br.readLine());
        A = Integer.parseInt(st.nextToken());
        B = Integer.parseInt(st.nextToken());

        M = Integer.parseInt(br.readLine());

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());

            graph.get(x).add(y);
            graph.get(y).add(x);
        }

        System.out.println(bfs());
    }

    public static int bfs() {
        Queue<Point> queue = new ArrayDeque<>();
        queue.offer(new Point(A, 0));

        while (!queue.isEmpty()) {
            Point cur = queue.poll();

            if (cur.n == B) return cur.len;

            for (Integer next : graph.get(cur.n)) {
                if (parent[next] == 0) {
                    parent[next] = cur.n;
                    queue.offer(new Point(next, cur.len + 1));
                }
            }
        }

        return -1;
    }
}
