import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int MAX = 0;      // 가장 먼 거리
    static int NODE = 1;     // 가장 먼 노드 번호
    static boolean[] visited;
    static List<List<Node>> graph = new ArrayList<>();

    static class Node {
        int num, weight;

        public Node(int num, int weight) {
            this.num = num;
            this.weight = weight;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < N - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int parent = Integer.parseInt(st.nextToken());
            int child = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());

            graph.get(parent).add(new Node(child, weight));
            graph.get(child).add(new Node(parent, weight));
        }

        visited = new boolean[N + 1];
        MAX = 0;
        dfs(1, 0);

        int start = NODE;

        visited = new boolean[N + 1];
        MAX = 0;
        dfs(start, 0);

        System.out.println(MAX);
    }

    static void dfs(int cur, int dist) {
        visited[cur] = true;

        if (dist > MAX) {
            MAX = dist;
            NODE = cur;
        }

        for (Node next : graph.get(cur)) {
            if (visited[next.num]) continue;
            dfs(next.num, dist + next.weight);
        }
    }
}
