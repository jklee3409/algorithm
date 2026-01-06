import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] T, P, dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        T = new int[N + 2];
        P = new int[N + 2];
        dp = new int[N + 2];

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            T[i] = Integer.parseInt(st.nextToken());
            P[i] = Integer.parseInt(st.nextToken());
        }

        int max = 0;

        for (int i = 1; i <= N + 1; i++) {
            if (max < dp[i]) {
                max = dp[i];
            }

            if (i == N + 1) break;

            int next = i + T[i];

            if (next <= N + 1) {
                dp[next] = Math.max(dp[next], max + P[i]);
            }
        }

        System.out.println(max);
    }
}