import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        int[][] square = new int[N][M];

        int maxSquareSize = 0;
        
        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < M; j++) {
                square[i][j] = str.charAt(j) - '0';
                if (square[i][j] == 1) maxSquareSize = 1;
            }
        }

        // 첫 번째 행 열 제외
        for (int i = 1; i < N; i++) {
            for (int j = 1; j < M; j++) {
                if (square[i][j] == 1) {  // 사각형을 만들 수 있는 경우
                    square[i][j] = Math.min(Math.min(square[i - 1][j], square[i][j - 1]), square[i - 1][j - 1]) + 1; // 점화식
                }
                maxSquareSize = Math.max(maxSquareSize, square[i][j]); // 최댓값 갱신
            }
        }

        System.out.println(maxSquareSize * maxSquareSize);
        br.close();
    }
}
