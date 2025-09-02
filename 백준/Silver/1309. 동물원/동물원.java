import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int MOD = 9901;
        int N = Integer.parseInt(br.readLine());
        int[][] dp = new int[N + 1][3];
        // 0: 배치 안함, 1: 왼쪽 배치, 2: 오른쪽 배치

        dp[1][0] = 1;
        dp[1][1] = 1;
        dp[1][2] = 1;

        for (int i = 2; i < N + 1; i++) {
            dp[i][0] = (dp[i-1][0] + dp[i-1][1] + dp[i-1][2]) % MOD;
            dp[i][1] = (dp[i-1][0] + dp[i-1][2]) % MOD;
            dp[i][2] = (dp[i-1][0] + dp[i-1][1]) % MOD;
        }

        int result = (dp[N][0] + dp[N][1] + dp[N][2]) % MOD;
        System.out.println(result);
    }
}
