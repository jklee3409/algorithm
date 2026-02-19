import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[] arr = new int[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) arr[i] = Integer.parseInt(st.nextToken());

        int left = 0, right = 0;
        int sum = 0, count = 0;

        while (true) {

            if (sum >= M) {
                if (sum == M) count++;

                sum -= arr[left++];
            }
            else {
                if (right == N) break;

                sum += arr[right++];
            }
        }

        System.out.println(count);
    }
}
