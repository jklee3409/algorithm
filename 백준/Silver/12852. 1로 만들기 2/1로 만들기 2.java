import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        int[] dp = new int[n + 1];      // 최소 연산 횟수 저장
        int[] prev = new int[n + 1];    // 경로 추적용

        dp[1] = 0;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + 1;
            prev[i] = i - 1;

            if (i % 2 == 0 && dp[i / 2] + 1 < dp[i]) {
                dp[i] = dp[i / 2] + 1;
                prev[i] = i / 2;
            }

            if (i % 3 == 0 && dp[i / 3] + 1 < dp[i]) {
                dp[i] = dp[i / 3] + 1;
                prev[i] = i / 3;
            }
        }

        // 출력
        System.out.println(dp[n]);

        // 경로 역추적
        StringBuilder sb = new StringBuilder();
        int current = n;
        while (current != 0) {
            sb.append(current).append(" ");
            current = prev[current];
        }

        System.out.println(sb);
    }
}
