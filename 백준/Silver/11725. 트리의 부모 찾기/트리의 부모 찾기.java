import java.io.*;
import java.util.*;

public class Main {
    static List<List<Integer>> graph = new ArrayList<>();
    static int[] parent;
    static int N;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N + 1; i++) graph.add(new ArrayList<>()); // 그래프 초기화
        parent = new int[N + 1];

        for (int i = 0; i < N - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            graph.get(a).add(b); // 무방향
            graph.get(b).add(a); // 무방향
        }

        bfs(1);

        StringBuilder sb = new StringBuilder();
        for (int i = 2; i < N + 1; i++) {
            sb.append(parent[i]).append("\n");
        }

        System.out.println(sb);
    }

    public static void bfs(int root) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(root);
        parent[root] = 0;

        while (!queue.isEmpty()) {
            int cur = queue.poll();

            for (Integer next : graph.get(cur)) {
                if (parent[next] == 0) {
                    parent[next] = cur;
                    queue.offer(next);
                }
            }
        }
    }
}
