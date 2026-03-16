import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            int n = Integer.parseInt(br.readLine());

            int[] lastYear = new int[n];
            int[] indegree = new int[n + 1];
            boolean[][] graph = new boolean[n + 1][n + 1];

            st = new StringTokenizer(br.readLine());

            for (int i = 0; i < n; i++) {
                lastYear[i] = Integer.parseInt(st.nextToken());
            }

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int higher = lastYear[i];
                    int lower = lastYear[j];

                    if (!graph[higher][lower]) {
                        graph[higher][lower] = true;
                        indegree[lower]++;
                    }
                }
            }

            int m = Integer.parseInt(br.readLine());

            for (int i = 0; i < m; i++) {
                st = new StringTokenizer(br.readLine());

                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());

                if (graph[a][b]) {
                    graph[a][b] = false;
                    graph[b][a] = true;
                    indegree[b]--;
                    indegree[a]++;
                } else {
                    graph[b][a] = false;
                    graph[a][b] = true;
                    indegree[a]--;
                    indegree[b]++;
                }
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int i = 1; i <= n; i++) {
                if (indegree[i] == 0) q.offer(i);
            }

            boolean impossible = false;
            boolean uncertain = false;
            List<Integer> result = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (q.isEmpty()) {
                    impossible = true;
                    break;
                }

                if (q.size() > 1) {
                    uncertain = true;
                }

                Integer cur = q.poll();
                result.add(cur);

                for (int next = 1; next <= n; next++) {
                    if (graph[cur][next]) {

                        if (--indegree[next] == 0) {
                            q.offer(next);
                        }
                    }
                }
            }

            if (impossible) sb.append("IMPOSSIBLE");
            else if (uncertain) sb.append("?");
            else {
                for (Integer i : result) {
                    sb.append(i).append(" ");
                }
            }

            sb.append("\n");
        }

        System.out.println(sb);
    }
}