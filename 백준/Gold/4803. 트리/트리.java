import java.io.*;
import java.util.*;

public class Main {
    static int n, m;
    static ArrayList<Integer>[] graph;
    static boolean[] visited;
    static boolean isTree;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int caseNum = 1;

        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());

            if (n == 0 && m == 0) break;

            graph = new ArrayList[n + 1];
            for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();
            visited = new boolean[n + 1];

            for (int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                
                graph[u].add(v);
                graph[v].add(u);
            }

            int treeCount = 0;
            for (int i = 1; i <= n; i++) {
                if (!visited[i]) {
                    isTree = true;
                    dfs(0, i);
                    if (isTree) {
                        treeCount++;
                    }
                }
            }

            sb.append("Case ").append(caseNum).append(": ");
            if (treeCount == 0) {
                sb.append("No trees.\n");
            } else if (treeCount == 1) {
                sb.append("There is one tree.\n");
            } else {
                sb.append("A forest of ").append(treeCount).append(" trees.\n");
            }
            caseNum++;
        }
        System.out.print(sb);
    }

    static void dfs(int prev, int curr) {
        visited[curr] = true;

        for (int next : graph[curr]) {
            if (next == prev) continue;

            if (visited[next]) {
                isTree = false;
            } else {
                dfs(curr, next);
            }
        }
    }
}