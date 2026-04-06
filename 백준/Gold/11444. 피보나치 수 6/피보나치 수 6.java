import java.io.*;

public class Main {
    static final long MOD = 1000000007L;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long n = Long.parseLong(br.readLine());

        if (n == 0) {
            System.out.println(0);
            return;
        }

        long[][] base = {
            {1, 1},
            {1, 0}
        };

        long[][] result = power(base, n);
        System.out.println(result[0][1]);
    }

    static long[][] power(long[][] matrix, long exp) {
        if (exp == 1) {
            return matrix;
        }

        long[][] half = power(matrix, exp / 2);
        long[][] result = multiply(half, half);

        if (exp % 2 == 1) {
            result = multiply(result, matrix);
        }

        return result;
    }

    static long[][] multiply(long[][] a, long[][] b) {
        long[][] result = new long[2][2];

        result[0][0] = (a[0][0] * b[0][0] + a[0][1] * b[1][0]) % MOD;
        result[0][1] = (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % MOD;
        result[1][0] = (a[1][0] * b[0][0] + a[1][1] * b[1][0]) % MOD;
        result[1][1] = (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % MOD;

        return result;
    }
}