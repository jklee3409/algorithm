import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int N, K;
    static int[][] belt; // [][0]: 내구도, [][1]: 로봇 여부 (0 없음, 1 있음)
    static int step = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        belt = new int[2 * N][2];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < 2 * N; i++) {
            belt[i][0] = Integer.parseInt(st.nextToken()); // 내구도
        }

        simulate();
        System.out.println(step);
    }

    static void simulate() {
        while (true) {
            step++;

            rotate();
            move();
            raise();

            if (checkEnd()) break;
        }
    }

    static void rotate() {
        int[] temp = belt[2 * N - 1]; // 맨 끝 칸 백업

        for (int i = 2 * N - 1; i > 0; i--) {
            belt[i] = belt[i - 1]; // 뒤에서부터 한 칸씩 오른쪽으로 미룸
        }

        belt[0] = temp; // 처음 칸에 마지막 칸을 넣어줌

        // 내리는 위치에 로봇이 있으면 내리기
        if (belt[N - 1][1] == 1) {
            belt[N - 1][1] = 0;
        }
    }

    static void move() {
        // N - 1: N번 칸
        for (int i = N - 2; i >= 0; i--) {

            if (belt[i][1] == 1 && belt[i + 1][1] == 0 && belt[i + 1][0] > 0) { // 현재 칸에 로봇이 있고, 다음 칸에 로봇이 없으면서 내구도가 0 이상이라면

                belt[i][1] = 0; // 현재 칸 로봇 제거
                belt[i + 1][1] = 1; // 다음 칸으로 옮기고
                belt[i + 1][0]--; // 내구도 감소
            }
        }

        // 내리는 위치에 로봇이 있으면 내리기
        if (belt[N - 1][1] == 1) {
            belt[N - 1][1] = 0;
        }
    }

    static void raise() {
        if (belt[0][0] > 0 && belt[0][1] == 0) {
            belt[0][1] = 1;
            belt[0][0]--;
        }
    }

    static boolean checkEnd() {
        int count = 0;
        for (int i = 0; i < 2 * N; i++) {
            if (belt[i][0] == 0) count++;
        }
        return count >= K;
    }
}
