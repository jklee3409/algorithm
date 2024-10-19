import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 입력 처리
        int N = sc.nextInt();
        int M = sc.nextInt();
        int[] A = new int[N];

        for (int i = 0; i < N; i++) {
            A[i] = sc.nextInt();
        }

        // 누적합의 나머지 값 개수를 저장할 Map
        HashMap<Integer, Long> remainderCount = new HashMap<>();
        remainderCount.put(0, 1L); // 초기 상태 (누적합이 0일 때도 유효)

        long prefixSum = 0;
        long result = 0;

        // 배열 순회하며 누적합과 나머지 계산
        for (int i = 0; i < N; i++) {
            prefixSum += A[i]; // 누적합 갱신
            int remainder = (int) (prefixSum % M);

            // 음수 나머지 처리
            if (remainder < 0) remainder += M;

            // 동일한 나머지가 존재하면 해당 개수만큼 결과에 더함
            result += remainderCount.getOrDefault(remainder, 0L);

            // 현재 나머지 값의 개수 갱신
            remainderCount.put(remainder, remainderCount.getOrDefault(remainder, 0L) + 1);
        }

        // 결과 출력
        System.out.println(result);
    }
}
