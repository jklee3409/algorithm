import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] map, dp;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][M];
        dp = new int[N][M];
        for (int i = 0; i < N; i++) {
            Arrays.fill(dp[i], -1);
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println(dfs(0, 0));
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    // dp[y][x]: (y,x)에서 도착점까지 갈 수 있는 경우의 수
    static int dfs(int y, int x) {
        if (y == N - 1 && x == M - 1) return 1;

        if (dp[y][x] != -1) return dp[y][x];

        dp[y][x] = 0;

        for (int i = 0; i < 4; i++) {
            int ny = y + dy[i];
            int nx = x + dx[i];

            if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
            if (map[y][x] <= map[ny][nx]) continue;

            dp[y][x] += dfs(ny, nx);
        }

        return dp[y][x];
    }
}