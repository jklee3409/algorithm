import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int A1 = 0, A2 = 0, B1 = 0, B2 = 0;

        StringTokenizer st = new StringTokenizer(br.readLine());
        A1 = Integer.parseInt(st.nextToken());
        B1 = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        A2 = Integer.parseInt(st.nextToken());
        B2 = Integer.parseInt(st.nextToken());

        int r = gcd(B1, B2);

        int sumB = (B1 * B2) / r;

        A1 = A1 * (sumB / B1);
        A2 = A2 * (sumB / B2);

        int sumA = A1 + A2;

        r = gcd(sumA, sumB);

        sb.append(sumA / r).append(" ").append(sumB / r);

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
