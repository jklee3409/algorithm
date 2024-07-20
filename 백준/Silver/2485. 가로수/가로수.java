import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];

        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        int gcd = 0;
        for (int i = 0; i < N - 1; i++) {
            int dis = arr[i + 1] - arr[i];
            gcd = gcd(dis, gcd);
        }

        int count = (arr[N - 1] - arr[0]) / gcd - arr.length + 1;

        br.close();
        System.out.println(count);
    }

    public static int gcd(int x, int y) {

        while (y != 0) {
            int r = x % y;

            x = y;
            y = r;
        }

        return x;
    }
}
