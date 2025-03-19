import java.io.*;
import java.util.*;

public class Main {
    static int K, N;
    static int[] lan;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        lan = new int[K];

        int maxLen = 0;
        for (int i = 0; i < K; i++) {
            lan[i] = Integer.parseInt(br.readLine());
            maxLen = Math.max(maxLen, lan[i]);
        }

        long high = maxLen, low = 1;
        long result = 0;

        while (low <= high) {
            long mid = (low + high) / 2;
            long cnt = 0;

            for (int i = 0; i < K; i++) {
                cnt += lan[i] / mid;
            }

            if (cnt >= N) {
                result = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println(result);
    }
}
