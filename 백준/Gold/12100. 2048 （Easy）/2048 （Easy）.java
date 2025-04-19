import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int max = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        int[][] board = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        simulate(0, board);
        System.out.println(max);
    }

    // 시뮬레이션
    public static void simulate(int depth, int[][] inputBoard) {
        if (depth == 5) {
            max = Math.max(max, getMax(inputBoard));
            return;
        }

        for (int dir = 0; dir < 4; dir++) {
            int[][] rotated = rotate(inputBoard, dir);
            int[][] moved = move(rotated);
            simulate(depth + 1, moved);
        }
    }

    // 이동 (왼쪽 기준)
    public static int[][] move(int[][] board) {
        int[][] newBoard = new int[N][N];

        for (int y = 0; y < N; y++) {
            int[] newRow = new int[N];
            int idx = 0;
            int prev = 0;

            for (int x = 0; x < N; x++) {
                if (board[y][x] == 0) continue;

                if (prev == 0) {
                    prev = board[y][x];
                } else if (prev == board[y][x]) {
                    newRow[idx++] = prev * 2;
                    prev = 0;
                } else {
                    newRow[idx++] = prev;
                    prev = board[y][x];
                }
            }

            if (prev != 0) {
                newRow[idx] = prev;
            }

            newBoard[y] = newRow;
        }

        return newBoard;
    }

    // 회전 처리: dir(0:상, 1:하, 2:좌, 3:우)
    public static int[][] rotate(int[][] board, int dir) {
        int[][] result = deepCopy(board);

        if (dir == 0) { // 위쪽 → 반시계 90도
            result = rotateLeft(result);
        } else if (dir == 1) { // 아래쪽 → 시계 90도
            result = rotateRight(result);
        } else if (dir == 3) { // 오른쪽 → 180도
            result = rotate180(result);
        }
        return result;
    }

    public static int[][] rotateLeft(int[][] board) {
        int[][] result = new int[N][N];
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                result[N - 1 - x][y] = board[y][x];
            }
        }
        return result;
    }

    public static int[][] rotateRight(int[][] board) {
        int[][] result = new int[N][N];
        for (int y = 0; y < N; y++) {
            for (int x = 0; x < N; x++) {
                result[x][N - 1 - y] = board[y][x];
            }
        }
        return result;
    }

    public static int[][] rotate180(int[][] board) {
        return rotateRight(rotateRight(board));
    }

    public static int getMax(int[][] board) {
        int maxValue = 0;
        
        for (int[] row : board) {
            for (int num : row) {
                maxValue = Math.max(maxValue, num);
            }
        }
        return maxValue;
    }

    public static int[][] deepCopy(int[][] board) {
        int[][] result = new int[N][N];
        for (int i = 0; i < N; i++) {
            result[i] = board[i].clone();
        }
        return result;
    }
}
