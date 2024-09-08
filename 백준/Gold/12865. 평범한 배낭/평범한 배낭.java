import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        int[] dp = new int[K + 1];
        int[][] bag = new int[N + 1][2];

        for (int i = 1; i <= N; i++) {
            st = new StringTokenizer(br.readLine());
            int weight = Integer.parseInt(st.nextToken());
            int value = Integer.parseInt(st.nextToken());

            bag[i][0] = weight;
            bag[i][1] = value;
        }

        for (int i = 1; i <= N; i++) {
            int weight = bag[i][0];
            int value = bag[i][1];

            for (int w = K; w >= weight; w--) {
                dp[w] = Math.max(dp[w], dp[w - weight] + value);
            }
        }

        System.out.println(dp[K]);
    }
}
