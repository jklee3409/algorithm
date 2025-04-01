import java.io.*;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        int[] T = new int[N + 1]; // 기간
        int[] P = new int[N + 1]; // 금액

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            T[i] = Integer.parseInt(st.nextToken());
            P[i] = Integer.parseInt(st.nextToken());
        }

        int[] dp = new int[N + 2];

        for (int i = N; i >= 1; i--) {
            int endDay = i + T[i]; // 상담이 끝나는 날

            if (endDay <= N + 1) { // 상담이 가능하면
                dp[i] = Math.max(P[i] + dp[endDay], dp[i + 1]); // 상담 vs 안함
                
            } else { // 상담이 불가능하면
                dp[i] = dp[i + 1]; // 건너뜀
            }
        }

        System.out.println(dp[1]);
        br.close();
    }
}
