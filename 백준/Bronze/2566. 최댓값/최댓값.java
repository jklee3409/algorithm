import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        final int row = 9;
        final int col = 9;
        int[][] arr = new int[row][col];
        int max = 0, max_row = 1, max_col = 1;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                arr[i][j] = sc.nextInt();
                if (max < arr[i][j]) {
                    max = arr[i][j];
                    max_row = i + 1;
                    max_col = j + 1;
                }
            }
        }

        System.out.println(max);
        System.out.println(max_row + " " + max_col);

    }
}