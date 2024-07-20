import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            long n = Long.parseLong(br.readLine());

            while (true) {
                if (prime(n)) {
                    sb.append(n).append("\n");
                    break;
                } else {
                    n++;
                }

            }
        }

        br.close();
        System.out.println(sb);
    }

    public static boolean prime(long num) {
        if (num == 0 || num == 1) {
            return false;
        } else if (num == 2) {
            return true;
        }

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if(num % i == 0) {
                return  false;
            }
        }

        return true;
    }
}
