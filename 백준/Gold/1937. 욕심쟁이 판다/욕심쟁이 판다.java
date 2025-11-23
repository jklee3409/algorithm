import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int MAX = 0;
    static int[][] forest;
    static int[][] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        forest = new int[N][N];
        dp = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                forest[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                MAX = Math.max(MAX, dfs(i, j));
            }
        }

        System.out.println(MAX);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static int dfs(int y, int x) {
        if (dp[y][x] != 0) return dp[y][x];

        dp[y][x] = 1;

        for (int i = 0; i < 4; i++) {
            int ny = y + dy[i];
            int nx = x + dx[i];

            if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
            if (forest[ny][nx] <= forest[y][x]) continue; // 다음 지역의 대나무가 적으면 이동 X

            dp[y][x] = Math.max(dp[y][x], 1 + dfs(ny, nx));
        }

        return dp[y][x];
    }
}
