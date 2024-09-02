import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static long[] p;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while (T-- > 0) {
            int N = Integer.parseInt(br.readLine());
            p = new long[N + 1];
            sb.append(padovan(N)).append("\n");
        }

        System.out.println(sb);
    }

    public static long padovan(int N) {

        if (N == 1) {
            p[1] = 1;
        } else if (N == 2) {
            p[1] = 1;
            p[2] = 1;
        } else {
            p[1] = 1;
            p[2] = 1;
            p[3] = 1;
        }

        for (int i = 4; i <= N; i++) {
            p[i] = p[i - 2] + p[i - 3];
        }

        return p[N];
    }

}
