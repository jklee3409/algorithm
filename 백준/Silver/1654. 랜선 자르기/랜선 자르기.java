import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int N, K;
    static int[] arr;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        K = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        arr = new int[K];

        long max = 0;
        for (int i = 0; i < K; i++) {
            arr[i] = Integer.parseInt(br.readLine());
            if (max < arr[i]) max = arr[i];
        }

        System.out.println(solution(max));
    }

    static long solution (long max) {
        long min = 1, mid = 0, result = 0;

        while (min <= max) {
            long count = 0;
            mid = (min + max) / 2;

            for (int i = 0; i < K; i++) {
                count += arr[i] / mid;
            }

            if (count < N) {
                max = mid - 1;
            } else {
                result = mid;
                min = mid + 1;
            }
        }

        return result;
    }
}