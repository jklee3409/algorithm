import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        sc.nextLine();
        int[] c_arr = {25, 10, 5, 1};
        int[][] result_arr = new int[N][4];

        for (int i = 0; i < N; i++) {
            int C = sc.nextInt();

            while (C > 0) {
                if (C >= 25) {
                    result_arr[i][0]++;
                    C -= 25;
                } else if (C >= 10) {
                    result_arr[i][1]++;
                    C -= 10;
                } else if (C >= 5) {
                    result_arr[i][2]++;
                    C -= 5;
                }else {
                    result_arr[i][3]++;
                    C -= 1;
                }
            }
        }

        for (int[] arr : result_arr) {
            for (int var : arr) {
                System.out.print(var + " ");
            }
            System.out.println();
        }
    }
}