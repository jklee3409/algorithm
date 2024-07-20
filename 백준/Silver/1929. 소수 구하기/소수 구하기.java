import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int M = Integer.parseInt(st.nextToken());
        int N = Integer.parseInt(st.nextToken());

        StringBuilder sb = new StringBuilder();
        for (int i = M; i <= N; i++) {
            if (prime(i)) {
                sb.append(i).append("\n");
            }
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
