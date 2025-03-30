import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] video;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        video = new int[N][N];

        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < N; j++) {
                video[i][j] = str.charAt(j) - '0';
            }
        }

        solution(N, 0, 0);
        System.out.println(sb);

        br.close();
    }

    public static void solution(int size, int r, int c) {

        // base camp
        if (isSameColor(size, r, c)) {
            int color = video[r][c];

            sb.append(color);

            return;
        }

        sb.append("(");

        // divide & conquer
        int newSize = size / 2;

        solution(newSize, r, c); // 좌상단
        solution(newSize, r, c + newSize); // 우상단
        solution(newSize, r + newSize, c); // 좌하단
        solution(newSize, r + newSize, c + newSize); // 우하단

        sb.append(")");
    }

    public static boolean isSameColor(int size, int r, int c) {
        int color = video[r][c];

        for (int i = r; i < r + size; i++) {
            for (int j = c; j < c + size; j++) {

                if (color != video[i][j]) return false;
            }
        }

        return true;
    }
}