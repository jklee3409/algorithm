import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int[] f;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        f = new int[N + 1];

        System.out.println(fib(N));
    }

    public static int fib(int N) {

        if(1 < N){
            f[1] = 1;
            f[2] = 2;
        } else {
            f[1] = 1;
        }

        for (int i = 3; i <= N; i++) {
            f[i] = (f[i - 1] + f[i - 2]) % 15746;
        }

        return f[N];
    }

}
