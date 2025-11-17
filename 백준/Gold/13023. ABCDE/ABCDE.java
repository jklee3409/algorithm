import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static boolean result = false;
    static boolean[] visited;
    static List<List<Integer>> graph = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        visited = new boolean[N];
        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        for (int i = 0; i < N; i++) {
            visited[i] = true;
            dfs(i, 1);
            visited[i] = false;

            if (result) break;
        }

        if (result) System.out.println(1);
        else System.out.println(0);
    }

    static void dfs(int idx, int depth) {
        if (depth == 5) {
            if (!result) result = true;
            return;
        }

        for (Integer friend : graph.get(idx)) {
            if (visited[friend]) continue;
            visited[friend] = true;
            dfs(friend, depth + 1);
            visited[friend] = false;
        }
    }
}