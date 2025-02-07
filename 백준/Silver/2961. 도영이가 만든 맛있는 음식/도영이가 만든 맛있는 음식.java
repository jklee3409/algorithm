import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static int N;
    static int[][] arr;
    static boolean[] isSelected;
    static int min = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        isSelected = new boolean[N];
        arr = new int[N][2];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            arr[i][0] = Integer.parseInt(st.nextToken());
            arr[i][1] = Integer.parseInt(st.nextToken());
        }

        subSet(0, 1, 0);
        System.out.println(min);
    }

    // 신 맛 : 곱
    // 쓴 맛 : 합
    // 가장 적은 차이 출력
    static void subSet (int idx, int s, int b) {
        if (idx == N) {
            int selCnt = 0;
            for (int i = 0; i < N; i++) {
                if (isSelected[i]) selCnt++;
            }
            // 선택된 재료가 없으면 그대로 종료
            if (selCnt == 0) return;

            min = Math.min(min, Math.abs(s - b));
            return;
        }

        // 재료를 선택한 경우 각각 신맛과 쓴맛을 계산
        isSelected[idx] = true;
        subSet(idx + 1, s * arr[idx][0], b + arr[idx][1]);

        // 재료를 선택하지 않은 경우 그대로 재귀 호출
        isSelected[idx] = false;
        subSet(idx + 1, s, b);
    }
}

