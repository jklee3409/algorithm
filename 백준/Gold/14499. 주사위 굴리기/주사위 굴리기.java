import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static int N, M, diceY, diceX, K;
    static int[][] map;
    static int[][] dice = new int[4][3];
    static int[] directions;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        diceY = Integer.parseInt(st.nextToken());
        diceX = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        directions = new int[K];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < K; i++) {
            directions[i] = Integer.parseInt(st.nextToken());
        }

        simulate();
        System.out.println(sb);
    }

    public static void simulate() {

        for (int dir : directions) {

            if (dir == 1) { // 동쪽
                if (isNotValidPosition(diceY, diceX + 1)) continue;

                rollRight();
                diceX++;

                diceAndMapUpdate();
                sb.append(getDiceTop()).append("\n");

            } else if (dir == 2) { // 서쪽
                if (isNotValidPosition(diceY, diceX - 1)) continue;

                rollLeft();
                diceX--;

                diceAndMapUpdate();
                sb.append(getDiceTop()).append("\n");

            } else if (dir == 3) { // 북쪽
                if (isNotValidPosition(diceY - 1, diceX)) continue;

                rollNorth();
                diceY--;

                diceAndMapUpdate();
                sb.append(getDiceTop()).append("\n");

            } else { // 남쪽
                if (isNotValidPosition(diceY + 1, diceX)) continue;

                rollSouth();
                diceY++;

                diceAndMapUpdate();
                sb.append(getDiceTop()).append("\n");
            }
        }
    }

    public static void rollRight() {
        int temp = dice[1][0];

        dice[1][0] = dice[3][1];
        dice[3][1] = dice[1][2];
        dice[1][2] = dice[1][1];
        dice[1][1] = temp;
    }

    public static void rollLeft() {
        int temp = dice[3][1];

        dice[3][1] = dice[1][0];
        dice[1][0] = dice[1][1];
        dice[1][1] = dice[1][2];
        dice[1][2] = temp;
    }

    public static void rollNorth() {
        int temp = dice[3][1];

        dice[3][1] = dice[0][1];
        dice[0][1] = dice[1][1];
        dice[1][1] = dice[2][1];
        dice[2][1] = temp;
    }

    public static void rollSouth() {
        int temp = dice[0][1];

        dice[0][1] = dice[3][1];
        dice[3][1] = dice[2][1];
        dice[2][1] = dice[1][1];
        dice[1][1] = temp;
    }

    public static int getDiceTop() {
        return dice[1][1];
    }

    public static void diceAndMapUpdate() {
        if (map[diceY][diceX] == 0) {
            map[diceY][diceX] = dice[3][1];

        } else {
            dice[3][1] = map[diceY][diceX];
            map[diceY][diceX] = 0;
        }
    }

    public static boolean isNotValidPosition(int y, int x) {

        return y < 0 || x < 0 || y >= N || x >= M;
    }
}