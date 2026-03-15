import java.io.*;
import java.util.*;

public class Main {

    static int[] time;
    static int[] indegree;
    static List<Integer>[] graph;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        Integer N = Integer.parseInt(br.readLine());

        time = new int[N + 1];
        indegree = new int[N + 1];

        graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int a = 1; a <= N; a++) {
            st = new StringTokenizer(br.readLine());

            time[a] = Integer.parseInt(st.nextToken());
            int m = Integer.parseInt(st.nextToken());

            while (m-- > 0) {
                int b = Integer.parseInt(st.nextToken());

                graph[a].add(b);
                indegree[b]++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        int[] dp = new int[N + 1];

        for (int i = 1; i <= N; i++) {
            if (indegree[i] == 0) {
                dp[i] = time[i];
                q.offer(i);
            }
        }

        while (!q.isEmpty()) {
            Integer cur = q.poll();

            for (Integer next : graph[cur]) {
                dp[next] = Math.max(dp[next], dp[cur] + time[next]);

                if (--indegree[next] == 0) {
                    q.offer(next);
                }
            }
        }

        int max = 0;
        for (int i = 1; i <= N; i++) {
            max = Math.max(max, dp[i]);
        }

        System.out.println(max);
    }
}