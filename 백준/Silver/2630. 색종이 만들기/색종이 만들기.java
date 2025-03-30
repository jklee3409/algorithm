import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] paper;
    static int whiteCnt = 0, blueCnt = 0;

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

        solution(N, 0, 0);

        System.out.println(whiteCnt);
        System.out.println(blueCnt);
    }

    public static void solution(int size, int r, int c) {

        // base camp
        // 분할된 영역이 모두 같은 색인 경우
        if (isSameColor(size, r, c)) {
            int color = paper[r][c];

            if (color == 1) blueCnt++;
            else  whiteCnt++;

            return;
        }

        // divide & conquer
        int newSize = size / 2;

        solution(newSize, r, c); // 좌상단
        solution(newSize, r, c + newSize); // 우상단
        solution(newSize, r + newSize, c); // 좌하단
        solution(newSize, r + newSize, c + newSize); // 우하단
    }

    private static boolean isSameColor(int size, int r, int c) {
        int color = paper[r][c];

        for (int i = r; i < r + size; i++) {
            for (int j = c; j < c + size; j++) {
                if (color != paper[i][j]) return false;
            }
        }

        return true;
    }
}