import java.io.*;

public class Main {
    static final int MOD = 1000000009;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        int[] input = new int[T];

        int maxN = 0;
        for (int i = 0; i < T; i++) {
            input[i] = Integer.parseInt(br.readLine());

            maxN = Math.max(maxN, input[i]);
        }

        long[] dp = new long[Math.max(4, maxN + 1)];
        dp[0] = 1;
        dp[1] = 1;
        dp[2] = 2;
        dp[3] = 4;

        for (int j = 4; j <= maxN; j++) {
            dp[j] = (dp[j - 1] + dp[j - 2] + dp[j - 3]) % MOD;
        }

        StringBuilder sb = new StringBuilder();
        for (int i : input) {
            sb.append(dp[i]).append("\n");
        }

        System.out.println(sb);
        br.close();
    }
}
