import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int[] A;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        A = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            A[i] = Integer.parseInt(st.nextToken());
        }

        M = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());

        Arrays.sort(A);
        for (int i = 0; i < M; i++) {
            int input = Integer.parseInt(st.nextToken());
            System.out.println(binarySearch(input));
        }
    }

    static int binarySearch(int n) {
        int left = 0, right = A.length - 1;

        while (left <= right) {
            int mid = (left + right) / 2;

            if (n == A[mid]) {
                return 1;
            } else if (n < A[mid]) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }

        return 0;
    }
}