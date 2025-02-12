import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int[][] paper;
    static int[] result = new int[3]; // -1 -> 0, 0 -> 1, 1 -> 2

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        paper = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                paper[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        solution(0, 0, N);

        for (int i : result) {
            System.out.println(i);;
        }
    }

    static void solution(int x, int y, int size) {
        // 기저 조건
        // 해당 영역이 모두 같은 숫자인 경우
        if (check(x, y, size)){
            result[paper[x][y] + 1]++;
            return;
        }

        int newSize = size / 3;

        // 구역을 9개로 쪼갬 -> 행과 열을 3개씩 나눈다
        // 각 구역에 대한 재귀 호출
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                solution(x + i * newSize, y + j * newSize, newSize);
            }
        }
    }

    // 해당 영역을 순회 하면서 모두 같은 숫자로 이루어져 있는지 확인
    static boolean check(int x, int y, int size) {
        int first = paper[x][y];

        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                if (first != paper[i][j]) return false;
            }
        }

        return true;
    }
}