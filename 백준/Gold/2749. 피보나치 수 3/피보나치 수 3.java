import java.io.*;

public class Main {

    static final int MOD = 1_000_000;
    static final int PISANO = 1_500_000;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());

        int index = (int) (n % PISANO);

        long[] dp = new long[PISANO + 1];
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= index; i++) {
            dp[i] = (dp[i - 1] + dp[i - 2]) % MOD;
        }

        System.out.println(dp[index]);
    }
}