import java.io.*;
import java.util.*;

public class Main {

    static int N, M, K;
    static char[][] map;
    static int[][][] dp;
    static String target;

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new char[N][M];

        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = input.charAt(j);
            }
        }

        target = br.readLine();

        dp = new int[N][M][target.length()];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                Arrays.fill(dp[i][j], -1);
            }
        }

        int answer = 0;

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                if (map[y][x] == target.charAt(0)) {
                    answer += dfs(y, x, 0);
                }
            }
        }

        System.out.println(answer);
    }

    private static int dfs(int y, int x, int idx) {
        if (idx == target.length() - 1) return 1;

        if (dp[y][x][idx] != -1) return dp[y][x][idx];

        dp[y][x][idx] = 0;

        for (int d = 0; d < 4; d++) {
            for (int dist = 1; dist <= K; dist++) {
                int ny = y + dy[d] * dist;
                int nx = x + dx[d] * dist;

                if (outOfRange(ny, nx)) break;

                if (map[ny][nx] == target.charAt(idx + 1)) {
                    dp[y][x][idx] += dfs(ny, nx, idx + 1);
                }
            }
        }

        return dp[y][x][idx];
    }

    private static boolean outOfRange(int y, int x) {
        return y < 0 || x < 0 || y >= N || x >= M;
    }
}