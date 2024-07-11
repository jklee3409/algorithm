import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.StringTokenizer;

public class Main{

    public static boolean[][] arr;
    public static int min = 64;

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        arr = new boolean[N][M];

        for (int i = 0; i < N; i++) {
            String str = br.readLine();

            for (int j = 0; j < M; j++) {
                if (str.charAt(j) == 'W') {
                    arr[i][j] = true;
                } else {
                    arr[i][j] = false;
                }
            }
        }

        int N_row = N - 7;
        int M_col = M - 7;

        for (int i = 0; i < N_row; i++) {
            for (int j = 0; j < M_col; j++) {
                find(i, j);
            }
        }

        System.out.println(min);

    }

    public static void find(int x, int y) {
        int x_end = x + 8;
        int y_end = y + 8;
        int count  = 0;

        boolean TF = arr[x][y];

        for (int i = x; i < x_end; i++) {
            for (int j = y; j < y_end; j++) {

                if (arr[i][j] != TF) {
                    count++;
                }

                TF = !TF;
            }
            TF = !TF;
        }

        count = Math.min(count, 64 - count);

        if (count < min) {
            min = count;
        }
    }
}