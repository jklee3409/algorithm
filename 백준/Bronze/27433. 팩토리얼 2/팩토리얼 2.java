import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long N = Long.parseLong(br.readLine());

        System.out.println(factorial(N));
    }

    public static long factorial(Long N) {
        if (N == 0) {
            return 1;
        } else {
            return N * factorial(N - 1);
        }
    }
}
