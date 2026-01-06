import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] arr, dp;

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        arr = new int[N];
        dp = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) arr[i] = Integer.parseInt(st.nextToken());

        dp[0] = arr[0];

        for (int i = 1; i < N; i++) {
            dp[i] = arr[i];

            for (int j = i - 1; j >= 0; j--) {
                if (arr[i] > arr[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + arr[i]);
                }
            }
        }

        int max = 0;
        for (int i = 0; i < N; i++) if (dp[i] > max) max = dp[i];

        System.out.println(max);
    }
}
