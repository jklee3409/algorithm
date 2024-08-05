import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static int N, count = 0;
    public static int[] col;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        col = new int[N + 1];

        N_Queen(0);
        System.out.println(count);
    }

    public static void N_Queen(int i) {

        if (promising(i)) {
            if (i == N) {
                count++;
            }
            else {
                for (int j = 1; j <= N; j++) {
                    col[i + 1] = j;
                    N_Queen(i + 1);
                }
            }
        }
    }

    public static boolean promising(int i) {
        boolean check = true;
        int k = 1;

        while (k < i && check) {
            if (col[i] == col[k] || Math.abs(col[i] - col[k]) == i - k) {
                check = false;
            }
            k++;
        }
        return check;
    }

}
