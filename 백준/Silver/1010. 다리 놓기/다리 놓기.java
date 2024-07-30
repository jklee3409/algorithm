import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int[][] arr = new int[30][30];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < T; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());

            sb.append((N == 0 || M == 0) ? 0 : Combination(M, N));
            sb.append("\n");
        }

        br.close();
        System.out.println(sb);
    }

    public static int Combination(int N, int K){
        for (int i = 0; i <= N; i++) {
            for (int j = 0; j <= Math.min(i, K); j++) {
                 if(j == 0 || i == j) arr[i][j] = 1;
                 else arr[i][j] = arr[i - 1][j - 1] + arr[i - 1][j];
            }
        }

        return arr[N][K];
    }
}
