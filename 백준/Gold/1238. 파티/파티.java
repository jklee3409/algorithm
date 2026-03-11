import java.io.*;
import java.util.*;

public class Main {

    static int[] dist;
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

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int X = Integer.parseInt(st.nextToken());

        graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            graph[from].add(new Edge(to, cost));
        }

        int max = Integer.MIN_VALUE;

        for (int i = 1; i <= N; i++) {
            int cost = 0;

            dist = new int[N + 1];
            Arrays.fill(dist, Integer.MAX_VALUE);

            dijkstra(i);

            cost += dist[X];

            dist = new int[N + 1];
            Arrays.fill(dist, Integer.MAX_VALUE);

            dijkstra(X);

            cost += dist[i];

            max = Math.max(max, cost);
        }

        System.out.println(max);
    }

    private static void dijkstra(int start) {
        PriorityQueue<State> pq = new PriorityQueue<>();

        pq.offer(new State(start, 0));
        dist[start] = 0;

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