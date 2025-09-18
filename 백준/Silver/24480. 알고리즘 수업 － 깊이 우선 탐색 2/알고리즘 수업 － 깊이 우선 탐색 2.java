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
        for (int i = 0; i < N + 1; i++) graph.add(new ArrayList<>());
        visited = new boolean[N + 1];
        order = new int[N + 1];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        dfs(R);

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < N + 1; i++) {
            sb.append(order[i]).append("\n");
        }
        System.out.println(sb);
    }

    static void dfs(int R) {
        visited[R] = true;
        order[R] = ++cnt;

        graph.get(R).sort(Comparator.reverseOrder());
        for (Integer next : graph.get(R)) {
            if (visited[next]) continue;
            visited[R] = true;
            dfs(next);
        }
    }
}
