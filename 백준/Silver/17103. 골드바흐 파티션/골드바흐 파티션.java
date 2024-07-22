import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int T = Integer.parseInt(br.readLine());
        var primes = getPrimeArray();

        for (int i = 0; i < T; i++) {
            int N = Integer.parseInt(br.readLine());
            int count = 0;

            for (int j = 2; j <= N / 2; j++) {
                if (!primes[j] && !primes[N - j]) {
                    count++;
                }
            }

            sb.append(count).append("\n");
        }

        br.close();
        System.out.println(sb);
    }


    public static boolean[] getPrimeArray() {
        boolean[] arr = new boolean[1000001];

        for (int i = 2; i <= 1000000; i++) {
            if (!arr[i]) {
                for (int j = i * 2; j <= 1000000; j += i) {
                    arr[j] = true;
                }
            }
        }

        return arr;
    }
}
