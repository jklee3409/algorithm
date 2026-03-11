import java.io.*;
import java.util.*;

public class Main {

    static int N, E;
    static List<Edge>[] graph;

    static class Edge {
        int to, cost;

        public Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static class State implements Comparable<State> {
        int node, dist;

        public State(int node, int dist) {
            this.node = node;
            this.dist = dist;
        }

        @Override
        public int compareTo(State o) {
            return Integer.compare(this.dist, o.dist);
        }
    }

    // 최단 경로1: dist[1] -> dist[v1] -> dist[v2] -> dist[N]
    // 최단 경로2: dist[1] -> dist[v2] -> dist[v1] -> dist[N]

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());

        int[] distFrom1 = new int[N + 1];
        int[] distFromV1 = new int[N + 1];
        int[] distFromV2 = new int[N + 1];

        Arrays.fill(distFrom1, Integer.MAX_VALUE);
        Arrays.fill(distFromV1, Integer.MAX_VALUE);
        Arrays.fill(distFromV2, Integer.MAX_VALUE);

        graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());

            int n1 = Integer.parseInt(st.nextToken());
            int n2 = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            graph[n1].add(new Edge(n2, cost));
            graph[n2].add(new Edge(n1, cost));
        }

        st = new StringTokenizer(br.readLine());
        int v1 = Integer.parseInt(st.nextToken());
        int v2 = Integer.parseInt(st.nextToken());

        dijkstra(distFrom1, 1);
        dijkstra(distFromV1, v1);
        dijkstra(distFromV2, v2);
        
        long result1 = distFrom1[v1] + distFromV1[v2] + distFromV2[N];
        long result2 = distFrom1[v2] + distFromV2[v1] + distFromV1[N];

        int INF = Integer.MAX_VALUE;
        
        boolean path1 = distFrom1[v1] != INF && distFromV1[v2] != INF && distFromV2[N] != INF;
        boolean path2 = distFrom1[v2] != INF && distFromV2[v1] != INF && distFromV1[N] != INF;

        if (path1 && path2) {
            System.out.println(Math.min(result1, result2));
        }
        else if (path1) {
            System.out.println(result1);
        }
        else if (path2) {
            System.out.println(result2);
        }
        else {
            System.out.println(-1);
        }
    }

    private static void dijkstra(int[] dist, int start) {
        PriorityQueue<State> pq = new PriorityQueue<>();

        dist[start] = 0;
        pq.offer(new State(start, 0));

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (dist[cur.node] < cur.dist) continue;

            for (Edge next : graph[cur.node]) {

                if (dist[next.to] > dist[cur.node] + next.cost) {
                    dist[next.to] = dist[cur.node] + next.cost;
                    pq.offer(new State(next.to, dist[next.to]));
                }
            }
        }
    }
}