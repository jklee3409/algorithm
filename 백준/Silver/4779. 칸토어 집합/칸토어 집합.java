import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{

    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";

        while ((input = br.readLine()) != null) {
            int N = Integer.parseInt(input);
            int len = (int) Math.pow(3, N);

            sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append("-");
            }

            cantor(0, len);
            System.out.println(sb);
        }

    }

    public static void cantor(int start, int len) {

        if (len == 1) return;

        int div_len = len / 3;

        for (int i = start + div_len; i < start + (2 * div_len); i++) {
            sb.setCharAt(i, ' ');
        }

        cantor(start, div_len);
        cantor(start + (2 * div_len), div_len);
    }
}
