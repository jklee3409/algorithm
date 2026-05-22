import java.io.*;
import java.util.*;

public class Solution {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuffer sb = new StringBuffer();

        int T = 10;
        int idx = 1;

        while (T-- > 0) {

            int N = Integer.parseInt(br.readLine());
            int[] building = new int[N];

            StringTokenizer st = new StringTokenizer(br.readLine());

            for (int i = 0; i < N; i++) {
                building[i] = Integer.parseInt(st.nextToken());
            }

            int answer = 0;

            for (int i = 2; i < N - 2; i++) {
                int leftHigh = Math.max(building[i - 1], building[i - 2]);
                int rightHigh = Math.max(building[i + 1], building[i + 2]);
                int high = Math.max(leftHigh, rightHigh);

                int count = building[i] - high;

                if (count < 0) count = 0;

                answer += count;
            }

            sb.append("#").append(idx++).append(" ").append(answer).append("\n");
        }

        System.out.println(sb);
    }
}