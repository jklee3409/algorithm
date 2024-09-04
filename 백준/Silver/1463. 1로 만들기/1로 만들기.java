import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] dp = new int[N + 1];

        // DP 테이블 (각 숫자를 1로 만드는 데 필요한 최소 연산 회수)
        for (int i = 2; i <= N; i++) {
            // 1을 빼는 경우
            dp[i] = dp[i - 1] + 1;
            // 2로 나누어 떨어지는 경우
            if(i % 2 == 0) dp[i] = Math.min(dp[i], dp[i / 2] + 1);
            // 3으로 나누어 떨어지는 경우
            if(i % 3 == 0) dp[i] = Math.min(dp[i], dp[i / 3] + 1);
        }

        System.out.println(dp[N]);
    }
}