import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());

        char[][] board = new char[n + 1][m + 1];
        for (int i = 1; i <= n; i++) {
            String line = br.readLine();
            for (int j = 1; j <= m; j++) {
                board[i][j] = line.charAt(j - 1);
            }
        }

        int[][] prefix = new int[n + 1][m + 1];

        // (1,1)이 'B'라고 가정한 체스판 기준으로
        // 다시 칠해야 하면 1, 아니면 0 저장
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                char expected;
                if ((i + j) % 2 == 0) {
                    expected = 'B';
                } else {
                    expected = 'W';
                }

                int mismatch = (board[i][j] == expected) ? 0 : 1;

                prefix[i][j] = prefix[i - 1][j]
                             + prefix[i][j - 1]
                             - prefix[i - 1][j - 1]
                             + mismatch;
            }
        }

        int answer = Integer.MAX_VALUE;

        for (int i = k; i <= n; i++) {
            for (int j = k; j <= m; j++) {
                int repaintB = prefix[i][j]
                             - prefix[i - k][j]
                             - prefix[i][j - k]
                             + prefix[i - k][j - k];

                int repaintW = k * k - repaintB;

                answer = Math.min(answer, Math.min(repaintB, repaintW));
            }
        }

        System.out.println(answer);
    }
}