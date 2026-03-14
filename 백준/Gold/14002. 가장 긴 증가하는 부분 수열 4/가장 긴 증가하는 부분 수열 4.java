import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());

        int[] arr = new int[N];
        int[] dp = new int[N];
        int[] path = new int[N];

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        for (int i = 0; i < N; i++) {

            path[i] = i;
            dp[i] = 1;

            for (int j = 0; j < i; j++) {

                if (arr[j] < arr[i] && dp[i] < dp[j] + 1) {
                    dp[i] = dp[j] + 1;
                    path[i] = j;
                }
            }
        }

        int max = 0, maxIdx = 0;

        for (int i = 0; i < N; i++) {
            if (max < dp[i]) {
                max = dp[i];
                maxIdx = i;
            }
        }

        sb.append(max).append("\n");

        List<Integer> pathList = new ArrayList<>();
        int idx = maxIdx;

        while (true) {
            pathList.add(arr[idx]);
            if (path[idx] == idx) break;
            idx = path[idx];
        }

        Collections.reverse(pathList);

        for (Integer i : pathList) {
            sb.append(i).append(" ");
        }

        System.out.println(sb);
    }
}