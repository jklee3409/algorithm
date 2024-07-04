import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        final int row = 5;
        final int col = 15;
        String[][] arr = new String[row][col];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < row; i++) {
            String s = sc.nextLine();
            for (int j = 0; j < s.length(); j++) {
                arr[i][j] = String.valueOf(s.charAt(j));
            }
        }

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                if (arr[j][i] != null) {
                    result.append(arr[j][i]);
                } else {
                    continue;
                }
            }
        }

        System.out.println(result);
    }
}