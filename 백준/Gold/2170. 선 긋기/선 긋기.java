import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] lines;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        lines = new int[N][2];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            lines[i][0] = start;
            lines[i][1] = end;
        }

        // 시작 기준 정렬
        Arrays.sort(lines, Comparator.comparingInt((int[] o) -> o[0]).thenComparing(o -> o[1]));

        int result = 0;
        int start = lines[0][0], end = lines[0][1];

        for (int i = 1; i < N; i++) {
            int curStart = lines[i][0];
            int curEnd = lines[i][1];

            // 새로운 선이 시작되는 경우
            if (end < curStart) {
                result += (end - start);

                start = curStart;
                end = curEnd;
            }
            // 선을 덮어쓰는 경우
            else {
                end = Math.max(end, curEnd);
            }
        }

        result += (end - start);

        System.out.println(result);
    }
}