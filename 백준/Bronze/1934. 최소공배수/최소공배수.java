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
            StringTokenizer st = new StringTokenizer(br.readLine());

            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int r = gcd(x, y);

            sb.append(x * y / r).append("\n");
        }

        br.close();
        System.out.println(sb);
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
