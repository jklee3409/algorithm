import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static int[][] board = new int[9][9];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 9; i++) {
            String str = br.readLine();
            for (int j = 0; j < 9; j++) {
                board[i][j] = str.charAt(j) - '0';
            }
        }

        solve(); // 스도쿠 풀이 시작

        // 결과 출력
        printBoard();
    }

    static boolean solve() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) { // 빈 칸 발견
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(i, j, num)) { // 유효한 숫자인지 체크
                            board[i][j] = num;

                            if (solve()) return true; // 다음 단계 진행

                            board[i][j] = 0; // 백트래킹
                        }
                    }
                    return false; // 가능한 숫자가 없으면 실패
                }
            }
        }
        return true; // 모든 빈 칸이 채워졌다면 성공
    }

    static boolean isValid(int row, int col, int num) {
        // 행과 열 체크
        for (int i = 0; i < 9; i++) {
            if (board[row][i] == num || board[i][col] == num) {
                return false;
            }
        }

        // 3x3 사각형 체크
        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[startRow + i][startCol + j] == num) {
                    return false;
                }
            }
        }

        return true; // 유효한 숫자임
    }

    static void printBoard() {
        for (int[] row : board) {
            for (int num : row) {
                System.out.print(num);
            }
            System.out.println();
        }
    }
}
