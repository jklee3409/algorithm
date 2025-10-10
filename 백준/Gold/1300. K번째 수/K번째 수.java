import java.io.*;
import java.util.*;

public class Main {
    static int N, K;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        K = Integer.parseInt(br.readLine());

        long low = 1L, high = (long) N * N;

        while (low < high) {
            long mid  = (low + high) / 2;

            if (countLE(mid) >= K) high = mid;
            else low = mid + 1;
        }
        System.out.println(low);
    }

    // i행 : i x 1, i x 2, i x 3, ... , i x N
    // -> 이 중에서 <= x 인 값
    // i x j <= x ---------> j <= x / i

    static long countLE(long x) {
        long cnt = 0;

        for (int i = 1; i < N + 1; i++) {
            cnt += Math.min(N, (int) (x / i));
            if ((long) i > x) break;
        }
        return cnt;
    }
}
