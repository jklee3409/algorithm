import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static int N;
    static String[] input;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        input = new String[N];

        for (int i = 0; i < N; i++) {
            input[i] = br.readLine();
        }

        StringBuilder sb = new StringBuilder();
        if (N == 1) {
            sb.append(input[0]);
            System.out.println(sb);
            return;
        }

        int len = input[0].length();

        for (int i = 0; i < len; i++) {
            boolean check = true;

            for (int j = 1; j < N; j++) {
                char cur = input[j].charAt(i);
                char prev = input[j - 1].charAt(i);

                if (cur != prev) {
                    sb.append("?");
                    check = false;
                    break;
                }
            }

            if (check) sb.append(input[0].charAt(i));
        }

        System.out.println(sb);
    }
}
