import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] result;
    static boolean[] visited;
    static List<List<Integer>> graph = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        N = Integer.parseInt(br.readLine());
        for (int i = 0; i < N + 1; i++) graph.add(new ArrayList<>());
        result = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int input = Integer.parseInt(st.nextToken());

                if (i == j) continue;
                if (input == 1) graph.get(i).add(j);
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                visited = new boolean[N];
                dfs(i, j, i, 1);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (int[] arr : result) {
            for (int i : arr) {
                sb.append(i).append(" ");
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }

    static void dfs(int src, int dst, int node, int depth) {
        if (depth != 1) visited[node] = true;
        
        if (dst == node && depth != 1) {
            result[src][dst] = 1;
            return;
        }

        for (Integer next : graph.get(node)) {
            if (visited[next]) continue;
            dfs(src, dst, next, depth + 1);
        }
    }
}
