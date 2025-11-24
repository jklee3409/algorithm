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
        for (int i = 0; i < N; i++) graph.add(new ArrayList<>());
        result = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int input = Integer.parseInt(st.nextToken());
                if (input == 1) graph.get(i).add(j);
            }
        }

        for (int i = 0; i < N; i++) {
            visited = new boolean[N];
            dfs(i, i);
        }

        StringBuilder sb = new StringBuilder();
        for (int[] arr : result) {
            for (int i : arr) sb.append(i).append(" ");
            sb.append("\n");
        }
        System.out.print(sb);
    }

    static void dfs(int src, int node) {
        for (int next : graph.get(node)) {
            if (visited[next])  continue;

            visited[next] = true;
            result[src][next] = 1;  
            dfs(src, next);
        }
    }
}
