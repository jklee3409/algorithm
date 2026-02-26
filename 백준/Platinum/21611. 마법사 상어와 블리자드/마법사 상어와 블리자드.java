import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int[] balls;
    static int[][] mapIdx;
    static int ans = 0;

    static int[] dr = {0, -1, 1, 0, 0};
    static int[] dc = {0, 0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        int[][] temp = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                temp[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        balls = new int[N * N];
        mapIdx = new int[N][N];
        initMap();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N / 2 && j == N / 2) continue;
                balls[mapIdx[i][j]] = temp[i][j];
            }
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int d = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());

            blizzard(d, s);
            simulation();
            transform();
        }

        System.out.println(ans);
    }

    static void initMap() {
        int r = N / 2;
        int c = N / 2;
        int d = 0;
        int len = 1;
        int idx = 1;

        int[] sr = {0, 1, 0, -1};
        int[] sc = {-1, 0, 1, 0};

        while (true) {
            for (int k = 0; k < 2; k++) {
                for (int l = 0; l < len; l++) {
                    r += sr[d];
                    c += sc[d];
                    if (r < 0 || c < 0 || r >= N || c >= N) return;
                    mapIdx[r][c] = idx++;
                }
                d = (d + 1) % 4;
            }
            len++;
        }
    }

    static void blizzard(int d, int s) {
        int r = N / 2;
        int c = N / 2;

        for (int i = 0; i < s; i++) {
            r += dr[d];
            c += dc[d];
            if (r < 0 || r >= N || c < 0 || c >= N) break;
            int idx = mapIdx[r][c];
            balls[idx] = 0;
        }
    }

    static void simulation() {
        while (true) {
            move();
            boolean exploded = explode();
            if (!exploded) break;
        }
    }

    static void move() {
        int[] nextBalls = new int[N * N];
        int idx = 1;
        for (int i = 1; i < N * N; i++) {
            if (balls[i] != 0) {
                nextBalls[idx++] = balls[i];
            }
        }
        balls = nextBalls;
    }

    static boolean explode() {
        boolean flag = false;
        int start = 1;
        while (start < N * N && balls[start] != 0) {
            int end = start;
            while (end + 1 < N * N && balls[end + 1] == balls[start]) {
                end++;
            }

            int cnt = end - start + 1;
            if (cnt >= 4) {
                ans += cnt * balls[start];
                for (int k = start; k <= end; k++) {
                    balls[k] = 0;
                }
                flag = true;
            }
            start = end + 1;
        }
        return flag;
    }

    static void transform() {
        int[] nextBalls = new int[N * N];
        int idx = 1;
        int start = 1;

        while (start < N * N && balls[start] != 0) {
            int end = start;
            while (end + 1 < N * N && balls[end + 1] == balls[start]) {
                end++;
            }

            int cnt = end - start + 1;
            int num = balls[start];

            if (idx < N * N) nextBalls[idx++] = cnt;
            if (idx < N * N) nextBalls[idx++] = num;

            start = end + 1;
        }
        balls = nextBalls;
    }
}