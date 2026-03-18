import java.io.*;
import java.util.*;

public class Main {

    static class Edge{
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());
            int C = Integer.parseInt(st.nextToken());

            edges.add(new Edge(A, B, C));
        }

        long[] dist = new long[N + 1];
        Arrays.fill(dist, Long.MAX_VALUE);

        dist[1] = 0;

        for (int i = 0; i < N - 1; i++) {
            boolean updated = false;

            for (Edge edge : edges) {
                if (dist[edge.from] == Long.MAX_VALUE) continue;

                if (dist[edge.to] > dist[edge.from] + edge.cost) {
                    dist[edge.to] = dist[edge.from] + edge.cost;
                    updated = true;
                }
            }

            if (!updated) break;
        }

        for (Edge edge : edges) {
            if (dist[edge.from] == Long.MAX_VALUE) continue;

            if (dist[edge.to] > dist[edge.from] + edge.cost) {
                System.out.println(-1);
                return;
            }
        }

        for (int i = 2; i <= N; i++) {
            if (dist[i] == Long.MAX_VALUE) sb.append(-1).append("\n");
            else sb.append(dist[i]).append("\n");
        }

        System.out.println(sb);
    }
}