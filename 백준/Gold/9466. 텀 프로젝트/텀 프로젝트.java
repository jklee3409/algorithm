import java.io.*;
import java.util.*;

public class Main {
    static int T, N;
    static int[] graph;
    static boolean[] visited; // 현재 DFS 경로 방문 여부
    static boolean[] finished; // 탐색 완료 여부
    static int count;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            N = Integer.parseInt(br.readLine());
            graph = new int[N + 1];
            visited = new boolean[N + 1];
            finished = new boolean[N + 1];
            count = 0;

            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= N; i++) {
                graph[i] = Integer.parseInt(st.nextToken());
            }

            simulate();
        }

        System.out.println(sb);
    }

    public static void simulate() {
        for (int i = 1; i <= N; i++) {
            if (visited[i]) continue;
            dfs(i);
        }
        sb.append(N - count).append("\n");
    }

    public static void dfs(int current) {
        visited[current] = true;
        int next = graph[current];

        if (!visited[next]) {
            dfs(next);

        } else if (!finished[next]) {
            // dfs 로 방문헸지만, 탐색이 완료되지 않았음 -> 사이클
            int temp = next;

            while (true) {
                count++;
                temp = graph[temp];

                if (temp == next) break;
            }
        }

        finished[current] = true;
    }
}