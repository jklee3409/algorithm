import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while (T-- > 0) {
            int N = Integer.parseInt(br.readLine());
            long[][] dp = new long[N + 1][2];

            // 0 : 1 0
            // 1 : 0 1
            // 2 : 1 1
            // 3 : 1 2
            // 4 : 2 3
            // 5 : 3 5


            dp[0][0] = 1;
            if (N == 0) {
                sb.append(dp[0][0]).append(" ").append(dp[0][1]).append("\n");
                continue;
            }

            dp[1][1] = 1;
            if (N == 1) {
                sb.append(dp[1][0]).append(" ").append(dp[1][1]).append("\n");
                continue;
            }

            for (int i = 2; i <= N; i++) {
                dp[i][0] = dp[i - 1][0] + dp[i - 2][0];
                dp[i][1] = dp[i - 1][1] + dp[i - 2][1];
            }

            sb.append(dp[N][0]).append(" ").append(dp[N][1]).append("\n");
        }

        System.out.println(sb);
        br.close();
    }
}
