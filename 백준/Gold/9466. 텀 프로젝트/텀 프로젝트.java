import java.io.*;
import java.util.*;

public class Main {

    static int N;
    static int[] pick;
    static int[] indegree;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            N = Integer.parseInt(br.readLine());

            pick = new int[N + 1];
            indegree = new int[N + 1];

            st = new StringTokenizer(br.readLine());

            for (int i = 1; i <= N; i++) {
                pick[i] = Integer.parseInt(st.nextToken());
                indegree[pick[i]]++;
            }

            Queue<Integer> q = new ArrayDeque<>();

            for (int i = 1; i <= N; i++) {
                if (indegree[i] == 0) {
                    q.offer(i);
                }
            }

            int notInTeam = 0;

            while (!q.isEmpty()) {
                int cur = q.poll();

                notInTeam++;

                int next = pick[cur];

                if (--indegree[next] == 0) {
                    q.offer(next);
                }
            }

            sb.append(notInTeam).append("\n");
        }

        System.out.println(sb);
    }
}