import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        while (true) {
            int n = Integer.parseInt(br.readLine());
            if (n == 0) {
                break;
            }

            int count = 0;
            for (int i = n + 1; i <= 2 * n; i++) {
                if (prime(i)) {
                    count++;
                }
            }

            sb.append(count).append("\n");
        }

        br.close();
        System.out.println(sb);
    }

    public static boolean prime(int num) {
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
