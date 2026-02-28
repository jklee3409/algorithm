import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(st.nextToken());
        int W = Integer.parseInt(st.nextToken());

        int[] trees = new int[T + 1];
        int[][] dp = new int[T + 1][W + 1];

        for (int i = 1; i <= T; i++) {
            trees[i] = Integer.parseInt(br.readLine());
        }

        for (int t = 1; t <= T; t++) {
            for (int w = 0; w <= W; w++) {

                // 현재 위치
                int currentTree = (w % 2 == 0) ? 1 : 2;

                // 이동 안 한 경우
                dp[t][w] = dp[t - 1][w];

                // 이동한 경우
                if (w > 0) {
                    dp[t][w] = Math.max(dp[t][w], dp[t - 1][w - 1]);
                }

                // 자두 받기
                if (trees[t] == currentTree) {
                    dp[t][w]++;
                }
            }
        }

        int answer = 0;
        for (int w = 0; w <= W; w++) {
            answer = Math.max(answer, dp[T][w]);
        }

        System.out.println(answer);
    }
}