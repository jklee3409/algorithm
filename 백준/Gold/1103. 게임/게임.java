import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int[][] board;
    static int[][] dp;
    static boolean[][] visited;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new int[N][M];
        dp = new int[N][M];
        visited = new boolean[N][M];

        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < M; j++) {
                char c = input.charAt(j);

                if (c == 'H') board[i][j] = -1;
                else board[i][j] = c - '0';

                dp[i][j] = -1;
            }
        }

        System.out.println(dfs(0,0));
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static int dfs(int y, int x) {
        if (outOfRange(y, x) || isHole(y, x)) return 0;

        if (visited[y][x]) {
            System.out.println("-1");
            System.exit(0);
        }

        if (dp[y][x] != -1) return dp[y][x];

        dp[y][x] = 0;

        visited[y][x] = true;

        for (int d = 0; d < 4; d++) {
            int ny = y + dy[d] * board[y][x];
            int nx = x + dx[d] * board[y][x];

            dp[y][x] = Math.max(dp[y][x], dfs(ny, nx) + 1);
        }

        visited[y][x] = false;

        return dp[y][x];
    }

    private static boolean outOfRange(int y, int x) {
        return y < 0 || x < 0 || y >= N || x >= M;
    }

    private static boolean isHole(int y, int x) {
        return board[y][x] == -1;
    }
}