import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        final int row = 100;
        final int col = 100;
        int[][] arr = new int[row][col];
        int sum = 0;

        int n = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < n; i++) {
            int r = sc.nextInt();
            int c = sc.nextInt();

            for (int j = r; j < r + 10; j++) {
                for (int k = c; k < c + 10; k++) {
                    arr[j][k] = 1;
                }
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (arr[i][j] == 1) {
                    sum++;
                }
            }
        }

        System.out.println(sum);

    }
}