import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();


        StringTokenizer st = new StringTokenizer(br.readLine());

        long x = Integer.parseInt(st.nextToken());
        long y = Integer.parseInt(st.nextToken());
        long r = gcd(x, y);

            sb.append(x * y / r).append("\n");


        br.close();
        System.out.println(sb);
    }

    public static long gcd(long x, long y) {

        while (y != 0) {
            long r = x % y;

            x = y;
            y = r;
        }
        return x;
    }

    }
