import java.io.*;
import java.util.*;

public class Main {

    // dir -> 0: 가로, 1: 세로, 2: 대각선
    // dp[r][c][dir] -> 이 칸에 이 방향으로 도착 가능한 경우의 수

    // 점화식
    // dp[r][c][0] = dp[r][c - 1][0] + dp[r][c - 1][2]
    // dp[r][c][1] = dp[r - 1][c][1] + dp[r - 1][c][2]
    // dp[r][c][2] = dp[r - 1][c - 1][0] + dp[r - 1][c - 1][1] + dp[r - 1][c - 1][2]

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[][] house = new int[N + 1][N + 1];
        long[][][] dp = new long[N + 1][N + 1][3];

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 1; j <= N; j++) {
                house[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        dp[1][2][0] = 1; // 초기 상태

        for (int r = 1; r <= N; r++) {
            for (int c = 1; c <= N; c++) {
                if (r == 1 && c == 2) continue;

                if (house[r][c] == 0) {
                    dp[r][c][0] = dp[r][c - 1][0] + dp[r][c - 1][2];

                    if (r > 1) {
                        dp[r][c][1] = dp[r - 1][c][1] + dp[r - 1][c][2];
                    }
                }

                if (isValidPos(r, c, house, N)) {
                    dp[r][c][2] = dp[r - 1][c - 1][0] + dp[r - 1][c - 1][1] + dp[r - 1][c - 1][2];
                }
            }
        }

        System.out.println(dp[N][N][0] + dp[N][N][1] + dp[N][N][2]);
    }

    private static boolean isValidPos(int r, int c, int[][] house, int N) {
        if (r <= 0 || c <= 0 || r > N || c >  N) return false;
        if (house[r - 1][c] == 1 || house[r][c] == 1 || house[r][c - 1] == 1) return false;

        return true;
    }
}