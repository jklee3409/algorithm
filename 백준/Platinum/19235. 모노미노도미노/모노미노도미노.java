import java.io.*;
import java.util.*;

public class Main {

    static int N;
    static int score = 0;
    static int blockId = 1;
    static int[][] green = new int[6][4];
    static int[][] blue = new int[6][4];

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int t = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());

            drop(green, t, y, blockId);

            int tBlue = t;
            if (t == 2) tBlue = 3;
            else if (t == 3) tBlue = 2;
            drop(blue, tBlue, x, blockId);

            blockId++;

            process(green);
            process(blue);
        }

        System.out.println(score);
        System.out.println(count());
    }

    static void drop(int[][] board, int t, int col, int id) {
        int r = 0;
        if (t == 1) {
            while (r + 1 < 6 && board[r + 1][col] == 0) r++;
            board[r][col] = id;
        } else if (t == 2) {
            while (r + 1 < 6 && board[r + 1][col] == 0 && board[r + 1][col + 1] == 0) r++;
            board[r][col] = id;
            board[r][col + 1] = id;
        } else {
            while (r + 2 < 6 && board[r + 2][col] == 0) r++;
            board[r][col] = id;
            board[r + 1][col] = id;
        }
    }

    static void process(int[][] board) {
        while (true) {
            boolean removed = false;
            for (int i = 2; i < 6; i++) {
                boolean full = true;
                for (int j = 0; j < 4; j++) {
                    if (board[i][j] == 0) {
                        full = false;
                        break;
                    }
                }
                if (full) {
                    removed = true;
                    score++;
                    for (int j = 0; j < 4; j++) board[i][j] = 0;
                }
            }

            if (removed) gravity(board);
            else break;
        }

        int cnt = 0;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] != 0) {
                    cnt++;
                    break;
                }
            }
        }

        if (cnt > 0) {
            while (cnt-- > 0) {
                for (int i = 5; i > 0; i--) {
                    System.arraycopy(board[i - 1], 0, board[i], 0, 4);
                }
                Arrays.fill(board[0], 0);
            }
        }
    }

    static void gravity(int[][] board) {
        for (int i = 4; i >= 0; i--) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) continue;

                int id = board[i][j];
                if (j + 1 < 4 && board[i][j + 1] == id) {
                    board[i][j] = 0;
                    board[i][j + 1] = 0;
                    int r = i;
                    while (r + 1 < 6 && board[r + 1][j] == 0 && board[r + 1][j + 1] == 0) r++;
                    board[r][j] = id;
                    board[r][j + 1] = id;
                } else if (i - 1 >= 0 && board[i - 1][j] == id) {
                    board[i][j] = 0;
                    board[i - 1][j] = 0;
                    int r = i;
                    while (r + 1 < 6 && board[r + 1][j] == 0) r++;
                    board[r][j] = id;
                    board[r - 1][j] = id;
                } else {
                    if (j - 1 >= 0 && board[i][j - 1] == id) continue;
                    
                    board[i][j] = 0;
                    int r = i;
                    while (r + 1 < 6 && board[r + 1][j] == 0) r++;
                    board[r][j] = id;
                }
            }
        }
    }

    static int count() {
        int cnt = 0;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 4; j++) {
                if (green[i][j] != 0) cnt++;
                if (blue[i][j] != 0) cnt++;
            }
        }
        return cnt;
    }
}