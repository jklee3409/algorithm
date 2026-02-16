import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[] arr;
    static StringBuffer sb = new StringBuffer();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        arr = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        solution();
        System.out.println(sb);
    }

    private static void solution() {
        int left = 0;
        int right = N - 1;

        int solution1 = 0;
        int solution2 = 0;

        int min = Integer.MAX_VALUE;
        while (left < right) {
            int sum = arr[left] + arr[right];

            if (Math.abs(sum) < min) {
                min = Math.abs(sum);
                solution1 = arr[left];
                solution2 = arr[right];
            }

            if (sum < 0) left++;
            else right--;
        }

        sb.append(solution1).append(" ").append(solution2);
    }
}