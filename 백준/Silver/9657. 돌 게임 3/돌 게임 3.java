import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        boolean[] dp = new boolean[Math.max(5, n + 1)];
        dp[1] = true; // 상근
        dp[2] = false; // 창영
        dp[3] = true;
        dp[4] = true;

        for (int i = 5; i <= n; i++) {
            dp[i] = !dp[i - 1] || !dp[i - 3] || !dp[i - 4];
        }

        System.out.println(dp[n] ? "SK" : "CY");
    }
}
