import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K, X;
    static List<List<Integer>> graph = new ArrayList<>();
    static List<Integer> result = new ArrayList<>();

    static class Node {
        int n, dis;

        public Node(int n, int dis) {
            this.n = n;
            this.dis = dis;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 도시의 개수
        M = Integer.parseInt(st.nextToken()); // 도로의 개수
        K = Integer.parseInt(st.nextToken()); // 거리 정보
        X = Integer.parseInt(st.nextToken()); // 출발 도시
        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>()); // 그래프 초기화

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            graph.get(a).add(b);
        }

        bfs();

        if (result.isEmpty()) {
            System.out.println(-1);
            return;
        }

        result.sort(Comparator.naturalOrder());

        StringBuilder sb = new StringBuilder();
        for (Integer i : result) {
            sb.append(i).append("\n");
        }

        System.out.println(sb);
    }

    static void bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[N + 1];

        queue.offer(new Node(X, 0));
        visited[X] = true;

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            if (node.dis == K) result.add(node.n);

            for (Integer next : graph.get(node.n)) {
                if (visited[next]) continue;

                queue.offer(new Node(next, node.dis + 1));
                visited[next] = true;
            }
        }
    }
}
