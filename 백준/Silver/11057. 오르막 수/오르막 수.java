import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        final int MOD = 10007;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[][] dp = new int[N + 1][10];

        // 1자리 수 초기화
        for (int i = 0; i <= 9; i++) {
            dp[1][i] = 1;
        }
        
        for (int i = 2; i <= N; i++) {
            for (int j = 0; j <= 9; j++) {
                for (int k = 0; k <= j; k++) {
                    dp[i][j] = (dp[i][j] + dp[i - 1][k]) % MOD;
                }
            }
        }

        // 길이 N인 오르막 수의 총합
        int sum = 0;
        for (int i = 0; i <= 9; i++) {
            sum = (sum + dp[N][i]) % MOD;
        }

        System.out.println(sum);
    }
}
