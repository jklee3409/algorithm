import java.io.*;
import java.util.*;

public class Solution {

    static int[] boxes;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuffer sb = new StringBuffer();
        StringTokenizer st;

        int T = 10;
        int number = 1;

        while (T-- > 0) {
            int dump = Integer.parseInt(br.readLine());

            boxes = new int[100];

            st = new StringTokenizer(br.readLine());

            for (int i = 0; i < 100; i++) {
                boxes[i] = Integer.parseInt(st.nextToken());
            }

            for (int i = 0; i < dump; i++) {
                Arrays.sort(boxes);

                if (boxes[99] - boxes[0] <= 1) break;

                boxes[99]--;
                boxes[0]++;
            }

            Arrays.sort(boxes);

            sb.append("#").append(number++).append(" ").append(boxes[99] - boxes[0]).append("\n");
        }

        System.out.println(sb);
    }
}