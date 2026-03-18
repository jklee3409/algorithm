import java.io.*;
import java.util.*;

public class Main {

    static int N, M, K;
    static long[] dist;
    static List<Edge>[] reverseGraph;

    static class Edge{
        int to, cost;

        public Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }

    static class State implements Comparable<State> {
        int node;
        long dist;

        public State (int node, long dist) {
            this.node = node;
            this.dist = dist;
        }

        @Override
        public int compareTo(State o) {
            return Long.compare(this.dist, o.dist);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        dist = new long[N + 1];
        Arrays.fill(dist, Long.MAX_VALUE);

        reverseGraph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            reverseGraph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            reverseGraph[to].add(new Edge(from, cost));
        }

        List<Integer> startList = new ArrayList<>();

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < K; i++) {
            startList.add(Integer.parseInt(st.nextToken()));
        }

        dijkstra(startList);

        int maxIdx = 0;
        long maxDist = 0;

        for (int i = 1; i <= N; i++) {
            if (maxDist < dist[i]) {
                maxDist = dist[i];
                maxIdx = i;
            }
        }

        System.out.println(maxIdx);
        System.out.println(maxDist);
    }

    private static void dijkstra(List<Integer> startList) {
        PriorityQueue<State> pq = new PriorityQueue<>();

        for (Integer start : startList) {
            pq.offer(new State(start, 0));
            dist[start] = 0;
        }

        while (!pq.isEmpty()) {
            State cur = pq.poll();

            if (dist[cur.node] < cur.dist) continue;

            for (Edge next : reverseGraph[cur.node]) {

                if (dist[next.to] > dist[cur.node] + next.cost) {
                    dist[next.to] = dist[cur.node] + next.cost;
                    pq.offer(new State(next.to, dist[next.to]));
                }
            }
        }
    }
}