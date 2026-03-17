import java.io.*;

public class Main {

    static char[][] board;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        board = new char[N][2 * N - 1];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 2 * N - 1; j++) {
                board[i][j] = ' ';
            }
        }

        draw(0, N - 1, N);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < 2 * N - 1; j++) {
                sb.append(board[i][j]);
            }
            sb.append('\n');
        }

        System.out.print(sb);
    }

    static void draw(int x, int y, int size) {
        if (size == 3) {
            board[x][y] = '*';
            board[x + 1][y - 1] = '*';
            board[x + 1][y + 1] = '*';

            for (int i = -2; i <= 2; i++) {
                board[x + 2][y + i] = '*';
            }
            return;
        }

        int newSize = size / 2;

        draw(x, y, newSize); // 위
        draw(x + newSize, y - newSize, newSize); // 왼쪽 아래
        draw(x + newSize, y + newSize, newSize); // 오른쪽 아래
    }
}