import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[] dp = new int[N + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 3; i <= N; i++) {
            if (dp[i - 3] != Integer.MAX_VALUE) dp[i] = Math.min(dp[i], dp[i - 3] + 1);

            if (i >= 5 && dp[i - 5] != Integer.MAX_VALUE) dp[i] = Math.min(dp[i], dp[i - 5] + 1);
        }

        System.out.println(dp[N] == Integer.MAX_VALUE ? -1 : dp[N]);
    }
}
