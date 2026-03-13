import java.io.*;
import java.util.*;

public class Main {

    static StringBuilder sb = new StringBuilder();
    static List<Integer>[] graph;
    static int[] indegree;
    static int[] time;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            st = new StringTokenizer(br.readLine());

            int N = Integer.parseInt(st.nextToken());
            int K = Integer.parseInt(st.nextToken());

            indegree = new int[N + 1];
            time = new int[N + 1];

            graph = new ArrayList[N + 1];
            for (int i = 1; i <= N; i++) {
                graph[i] = new ArrayList<>();
            }

            st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= N; i++) {
                time[i] = Integer.parseInt(st.nextToken());
            }

            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());

                int A = Integer.parseInt(st.nextToken());
                int B = Integer.parseInt(st.nextToken());

                graph[A].add(B);
                indegree[B]++;
            }

            int W = Integer.parseInt(br.readLine());

            solution(N, W);
        }

        System.out.println(sb);;
    }

    private static void solution(int N, int W) {
        int[] dp = new int[N + 1];

        Queue<Integer> q = new ArrayDeque<>();

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

        sb.append(dp[W]).append("\n");
    }
}