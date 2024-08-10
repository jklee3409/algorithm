import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static int N, count1 = 0, count2 = 0;
    public static int[] F;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        F = new int[N];

        fib(N);
        fibonacci(N);

        br.close();
        System.out.println(count1 + " " + count2);
    }

    public static int fib(int n) {
        if (n == 1 || n == 2) {
            count1++;
            return 1;
        } else {
            return (fib(n - 1) + fib(n - 2));
        }
    }

    public static int fibonacci(int n) {
        count2++;
        F[1] = F[2] =  1;

        for (int i = 3; i < n; i++) {
            F[i] = F[i - 1] + F[i - 2];
            count2++;
        }

        return F[n - 1];
    }
}
