import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int row = sc.nextInt();
        int col = sc.nextInt();

        int[][] A = new int[row][col];
        int[][] B = new int[row][col];
        int[][] sum = new int[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int num = sc.nextInt();
                A[i][j] = num;
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int num = sc.nextInt();
                B[i][j] = num;
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                sum[i][j] = A[i][j] + B[i][j];
            }
        }

        for (int[] var : sum) {
            for (int var2 : var) {
                System.out.print(var2 + " ");
            }
            System.out.println();
        }


    }
}