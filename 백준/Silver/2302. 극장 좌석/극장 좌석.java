import java.io.*;

public class Main {
    static int[] dp = new int[41];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine()); // 좌석 수
        int M = Integer.parseInt(br.readLine()); // VIP 수

        int[] vip = new int[M];
        for (int i = 0; i < M; i++) {
            vip[i] = Integer.parseInt(br.readLine());
        }

        // 피보나치 dp 세팅
        dp[0] = 1;
        dp[1] = 1;
        for (int i = 2; i <= N; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }

        int result = 1;

        // 첫 번째 VIP 전까지 구간
        if (M > 0) {
            result *= dp[vip[0] - 1];
        } else {
            result *= dp[N]; // VIP 없으면 전체 구간
        }

        // VIP 사이 구간
        for (int i = 1; i < M; i++) {
            int gap = vip[i] - vip[i - 1] - 1;
            result *= dp[gap];
        }

        // 마지막 VIP 뒤 구간
        if (M > 0 && vip[M - 1] < N) {
            result *= dp[N - vip[M - 1]];
        }

        System.out.println(result);
    }
}
