import java.io.*;
import java.util.*;

public class Main {
    static List<List<Integer>> graph = new ArrayList<>();
    static boolean[] visited;
    static int[] order;
    static int cnt = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int R = Integer.parseInt(st.nextToken());

        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>());
        visited = new boolean[N + 1];
        order = new int[N + 1];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        bfs(R);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= N; i++) {
            sb.append(order[i]).append("\n");
        }
        System.out.println(sb);
    }

    static void bfs(int R) {
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(R);
        visited[R] = true;
        order[R] = ++cnt;

        while (!queue.isEmpty()) {
            int cur = queue.poll();
            graph.get(cur).sort(Comparator.reverseOrder());
            for (int next : graph.get(cur)) {
                if (visited[next]) continue;
                queue.offer(next);
                visited[next] = true;
                order[next] = ++cnt;
            }
        }
    }
}
