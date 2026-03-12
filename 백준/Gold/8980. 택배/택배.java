import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int C = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(br.readLine());

        int[][] boxes = new int[M][3]; // from, to, count

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            boxes[i][0] = Integer.parseInt(st.nextToken());
            boxes[i][1] = Integer.parseInt(st.nextToken());
            boxes[i][2] = Integer.parseInt(st.nextToken());
        }

        // 받는 마을 기준 오름차순, 같으면 보내는 마을 기준 오름차순
        Arrays.sort(boxes, (a, b) -> {
            if (a[1] == b[1]) return Integer.compare(a[0], b[0]);
            return Integer.compare(a[1], b[1]);
        });

        int[] load = new int[N + 1];
        int result = 0;

        for (int i = 0; i < M; i++) {
            int from = boxes[i][0];
            int to = boxes[i][1];
            int box = boxes[i][2];

            int maxLoad = 0;

            for (int j = from; j < to; j++) {
                maxLoad = Math.max(maxLoad, load[j]);
            }

            int canCarry = Math.min(box, C - maxLoad);

            for (int j = from; j < to; j++) {
                load[j] += canCarry;
            }

            result += canCarry;
        }

        System.out.println(result);
    }
}