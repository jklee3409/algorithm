import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;


public class Main {
    static int N;
    static int[][] cave;
    static StringBuilder sb = new StringBuilder();

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int idx = 1;

        while (true) {
            N = Integer.parseInt(br.readLine());
            cave = new int[N][N];

            if (N == 0) break;

            for (int i = 0; i < N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int j = 0; j < N; j++) {
                    cave[i][j] = Integer.parseInt(st.nextToken());
                }
            }

            dijkstra(idx);
            idx++;
        }

        System.out.println(sb);
    }

    static class Node implements Comparable<Node>{
        int y, x, cost;

        public Node(int y, int x, int cost) {
            this.y = y;
            this.x = x;
            this.cost = cost;
        }

        @Override
        public int compareTo(Node o) {
            return this.cost - o.cost;
        }
    }

    public static void dijkstra(int idx) {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        int[][] distance = new int[N][N];
        for (int i = 0; i < N; i++) Arrays.fill(distance[i], Integer.MAX_VALUE);
        distance[0][0] = cave[0][0];

        pq.offer(new Node(0, 0, cave[0][0]));

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.cost > distance[current.y][current.x]) continue;

            for (int i = 0; i < 4; i++) {
                int ny = current.y + dy[i];
                int nx = current.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N|| nx >= N) continue;

                int newCost = cave[ny][nx] + distance[current.y][current.x];

                if (newCost < distance[ny][nx]) {
                    distance[ny][nx] = newCost;
                    pq.offer(new Node(ny, nx, newCost));
                }
            }
        }

       sb.append("Problem ").append(idx).append(": ").append(distance[N - 1][N - 1]).append("\n");
    }
}