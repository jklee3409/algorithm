import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        int[][] flowers = new int[N][2];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());

            int m1 = Integer.parseInt(st.nextToken());
            int d1 = Integer.parseInt(st.nextToken());
            int m2 = Integer.parseInt(st.nextToken());
            int d2 = Integer.parseInt(st.nextToken());

            int start = m1 * 100 + d1;
            int end = m2 * 100 + d2;

            flowers[i][0] = start;
            flowers[i][1] = end;
        }

        Arrays.sort(flowers, (a, b) -> {
            if (a[0] == b[0]) return Integer.compare(b[1], a[1]);
            return Integer.compare(a[0], b[0]);
        });

        int target = 301;
        int count = 0;
        int idx = 0;

        while (target < 1201) {
            int maxEnd = target;

            while (idx < N && flowers[idx][0] <= target) {
                maxEnd = Math.max(maxEnd, flowers[idx][1]);
                idx++;
            }

            if (maxEnd == target) {
                System.out.println(0);
                return;
            }

            target = maxEnd;
            count++;
        }

        System.out.println(count);
    }
}