import java.io.*;
import java.util.*;

public class Main {

    static int N, M;

    static class Edge {
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

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        long[] dist = new long[N + 1];
        Arrays.fill(dist, Long.MAX_VALUE);

        dist[1] = 0;

        List<Edge> edges = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            edges.add(new Edge(from, to, cost));
        }

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

        StringBuilder sb = new StringBuilder();

        for (int i = 2; i <= N; i++) {
            sb.append(dist[i] == Long.MAX_VALUE ? -1 : dist[i]).append("\n");
        }

        System.out.println(sb);
    }
}