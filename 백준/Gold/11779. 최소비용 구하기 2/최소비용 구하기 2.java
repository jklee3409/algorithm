import java.io.*;
import java.util.*;

public class Main {

    static int[] dist;
    static int[] prev;
    static List<Edge>[] graph;

    static class Edge{
        int to, cost;

        public Edge(int to, int cost){
            this.to = to;
            this.cost = cost;
        }
    }

    static class State implements Comparable<State>{
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

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());

        dist = new int[n + 1];
        prev = new int[n + 1];
        graph = new ArrayList[n + 1];

        Arrays.fill(dist, Integer.MAX_VALUE);

        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            graph[from].add(new Edge(to, cost));
        }

        st = new StringTokenizer(br.readLine());
        int start = Integer.parseInt(st.nextToken());
        int destination = Integer.parseInt(st.nextToken());

        dijkstra(start);

        List<Integer> path = getPath(start, destination);

        sb.append(dist[destination]).append("\n");
        sb.append(path.size()).append("\n");

        for (int node : path) {
            sb.append(node).append(" ");
        }

        System.out.println(sb);
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
                    prev[next.to] = cur.node;
                    pq.offer(new State(next.to, dist[next.to]));
                }
            }
        }
    }

    private static List<Integer> getPath(int start, int destination) {
        List<Integer> path = new ArrayList<>();
        int cur = destination;

        while (cur != start) {
            path.add(cur);
            cur = prev[cur];
        }
        path.add(start);

        Collections.reverse(path);
        return path;
    }
}