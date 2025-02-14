import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.StringTokenizer;


public class Main {
    static int V, E;
    private static boolean[] visited;
    static PriorityQueue<Node> pq = new PriorityQueue<>();
    static List<List<Node>> graph = new ArrayList<>();

    static class Node implements Comparable<Node>{
        int vertex, weight;

        public Node(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public int compareTo(Node o) {
            return this.weight - o.weight;
        }
    }

    static long prim() {
        visited = new boolean[V + 1];
        pq.offer(new Node(1, 0));

        long totalWeight = 0;
        int count = 0;

        while (!pq.isEmpty() && count < V) {
            Node current = pq.poll();

            if (visited[current.vertex]) continue;

            visited[current.vertex] = true;
            totalWeight += current.weight;
            count ++;

            for (Node next : graph.get(current.vertex)) {
                if (!visited[next.vertex]) {
                    pq.offer(next);
                }
            }
        }

        return totalWeight;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        V = Integer.parseInt(st.nextToken());
        E = Integer.parseInt(st.nextToken());
        for (int i = 0; i <= V; i++) {
            graph.add(new ArrayList<>());
        }

        for (int i = 0; i < E; i++) {
            st = new StringTokenizer(br.readLine());

            int v1 = Integer.parseInt(st.nextToken());
            int v2 = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());

            graph.get(v1).add(new Node(v2, weight));
            graph.get(v2).add(new Node(v1, weight));
        }

        System.out.println(prim());
    }
}