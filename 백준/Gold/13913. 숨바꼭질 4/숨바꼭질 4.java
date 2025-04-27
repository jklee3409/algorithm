import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
    static int N, K;
    static boolean[] visited = new boolean[100001];
    static int[] parent = new int[100001];
    static StringBuilder sb = new StringBuilder();

    static class Point {
        int x, time;

        public Point(int x, int time) {
            this.x = x;
            this.time = time;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        System.out.println(bfs());

        Stack<Integer> stack = new Stack<>();
        for (int i = K; i != -1; i = parent[i]) {
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            sb.append(stack.pop()).append(" ");
        }

        System.out.println(sb);
        br.close();
    }

    public static int bfs() {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(N, 0));
        visited[N] = true;
        parent[N] = -1;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.x == K) return p.time;

            int[] dx = {p.x, 1, -1};
            for (int i = 0; i < 3; i++) {
                int nx = p.x + dx[i];

                if (nx < 0 || nx >= visited.length || visited[nx]) continue;

                queue.offer(new Point(nx, p.time + 1));
                visited[nx] = true;
                parent[nx] = p.x;
            }
        }

        return -1;
    }
}
