import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        int[] arr = new int[N + 1];
        int[] dp = new int[N + 1];

        for (int i = 1; i <= N; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        dp[1] = arr[1];
        if(1 < N) dp[2] = arr[1] + arr[2];

        for (int i = 3; i <= N; i++) {

            //dp[i - 2] + arr[i] = 비연속
            //dp[i - 3] + arr[i - 1] + arr[i] = 연속 (그러나 3잔 연속 마시는 것은 불가능하기 때문에 dp[i - 3]에 더해줌)
            //마지막 dp의 값이 항상 최댓값을 보장하는 것은 아님 (따라서 dp[i - 1]과의 비교가 필요함)
            dp[i] = Math.max(dp[i - 1], Math.max(dp[i - 2] + arr[i], dp[i - 3] + arr[i - 1] + arr[i]));
        }

        System.out.println(dp[N]);
    }
}
