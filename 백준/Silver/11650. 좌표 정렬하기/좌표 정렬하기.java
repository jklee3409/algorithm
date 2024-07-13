import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main{

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        int[][] arr = new int[N][2];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            arr[i][0] = Integer.parseInt(st.nextToken());
            arr[i][1] = Integer.parseInt(st.nextToken());
        }

        br.close();

        // 람다식을 통해서 Arrays.sort 가 2차원 배열을 정렬할 수 있도록 확장.
        Arrays.sort(arr, (e1, e2) ->{

            // e1 - e2 의 결과가 음수 => e1이 더 작음을 의미.
            // e1 - e2 의 결과가 0 => 두 수는 동일.
            // e1 - e2 의 결과가 양수 => e1이 더 큰 수임을 의미.
            
            // Comparator 인터페이스의 compare 메서드 구현.
            if (e1[0] == e2[0]) {
                return e1[1] - e2[1];
            } else {
                return e1[0] - e2[0];
            }
        });

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < N; i++) {
            sb.append(arr[i][0]).append(" ").append(arr[i][1]).append("\n");
        }

        System.out.println(sb);
    }
}