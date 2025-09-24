import java.io.*;
import java.util.*;

public class Main {
    static final int INF = 1_000_000_000;
    static int V, E, K;
    static List<List<Edge>> g = new ArrayList<>();
    static int[] dist;
    static boolean[] visited;

    static class Edge {
        int to, w;
        Edge(int to, int w) { this.to = to; this.w = w; }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());

        K = Integer.parseInt(br.readLine().trim());

        for (int i = 0; i <= V; i++) g.add(new ArrayList<>());

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int w = Integer.parseInt(st.nextToken());
            g.get(u).add(new Edge(v, w));
        }

        dist = new int[V + 1];
        visited = new boolean[V + 1];
        Arrays.fill(dist, INF);
        dist[K] = 0;
        
        for (int iter = 0; iter < V; iter++) {
            int u = -1, best = INF;
            for (int v = 1; v <= V; v++) {
                if (!visited[v] && dist[v] < best) {
                    best = dist[v];
                    u = v;
                }
            }
            if (u == -1) break;   // 더 이상 도달 가능한 정점 없음
            visited[u] = true;

            for (Edge e : g.get(u)) {
                if (!visited[e.to] && dist[u] + e.w < dist[e.to]) {
                    dist[e.to] = dist[u] + e.w;
                }
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int v = 1; v <= V; v++) {
            if (dist[v] == INF) sb.append("INF\n");
            else sb.append(dist[v]).append('\n');
        }
        System.out.print(sb.toString());
    }
}
