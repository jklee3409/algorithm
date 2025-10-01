import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static final int MOD = 1000;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        long B = Long.parseLong(st.nextToken());

        int[][] A = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                A[i][j] = Integer.parseInt(st.nextToken()) % MOD;
            }
        }

        int[][] ans = pow(A, B);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sb.append(ans[i][j] % MOD).append(' ');
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }

    static int[][] mul(int[][] A, int[][] B) {
        int[][] C = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int k = 0; k < N; k++) {
                int a = A[i][k];
                if (a == 0) continue;
                for (int j = 0; j < N; j++) {
                    C[i][j] = (int)((C[i][j] + (long) a * B[k][j]) % MOD);
                }
            }
        }
        return C;
    }

    static int[][] identity() {
        int[][] I = new int[N][N];
        for (int i = 0; i < N; i++) I[i][i] = 1;
        return I;
    }

    static int[][] pow(int[][] A, long b) {
        int[][] result = identity();
        int[][] base = A;
        while (b > 0) {
            if ((b & 1) == 1) result = mul(result, base);
            base = mul(base, base);
            b >>= 1;
        }
        return result;
    }
}
