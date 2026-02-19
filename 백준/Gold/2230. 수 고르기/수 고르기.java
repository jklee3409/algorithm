import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        long M = Integer.parseInt(st.nextToken());

        int[] arr = new int[N];
        for (int i = 0; i < N; i++) arr[i] = Integer.parseInt(br.readLine());

        Arrays.sort(arr);

        int left = 0, right = 0;
        long min = Long.MAX_VALUE;

        while (left < N && right < N) {
            long sub = arr[right] - arr[left];

            if (sub == M) {
                min = M;
                break;
            } else if (sub < M) right++;
            else left++;

            if (M <= sub && sub < min) min = sub;
        }

        System.out.println(min);
    }
}
