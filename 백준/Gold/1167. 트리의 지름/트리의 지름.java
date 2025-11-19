import java.io.*;
import java.util.*;

public class Main {
    static int V;
    static int START = 1; // 루트에서 가장 먼 노드
    static int MAX = 0;
    static List<List<Node>> graph = new ArrayList<>();
    static boolean[] visited;

    static class Node {
        int n, weight;

        public Node(int n, int weight) {
            this.n = n;
            this.weight = weight;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        V = Integer.parseInt(br.readLine());
        for (int i = 0; i < V + 1; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < V; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int node = Integer.parseInt(st.nextToken());

            while (true) {
                int child = Integer.parseInt(st.nextToken());
                if (child == -1) break;
                int weight = Integer.parseInt(st.nextToken());

                graph.get(node).add(new Node(child, weight));
                graph.get(child).add(new Node(node, weight));
            }
        }

        visited = new boolean[V + 1];
        dfs(1, 0);

        MAX = 0;
        visited = new boolean[V + 1];

        dfs(START, 0);

        System.out.println(MAX);
    }

    static void dfs(int node, int dist) {
        visited[node] = true;

        if (MAX < dist) {
            MAX = dist;
            START = node;
        }

        for (Node next : graph.get(node)) {
            if (visited[next.n]) continue;
            dfs(next.n, dist + next.weight);
        }
    }
}
