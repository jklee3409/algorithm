import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[][] time_table = new int[N][2];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            time_table[i][0] = start;
            time_table[i][1] = end;
        }

        Arrays.sort(time_table, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {

                // 종료 시간이 같을 경우 시작 시간이 빠른 순서대로 정렬 한다.
                if (o1[1] == o2[1]) {
                    return o1[0] - o2[0];
                }

                return o1[1] - o2[1];
            }
        });
        
        int count = 0;
        int prev_end_time = 0;

        for (int i = 0; i < N; i++) {

            // 직전 회의 종료 시간이 다음 회의 시작 시간과 같거나 더 작다면 갱신.
            if (prev_end_time <= time_table[i][0]) {
                prev_end_time = time_table[i][1];
                count++;
            }
        }

        System.out.println(count);
    }
}
