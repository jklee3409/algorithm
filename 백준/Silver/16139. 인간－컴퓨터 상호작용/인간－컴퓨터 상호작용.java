import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s = br.readLine();
        int N = Integer.parseInt(br.readLine());

        int[][] alpha = new int[s.length() + 1][26];
        for (int i = 1; i <= s.length(); i++) {

            int search = s.charAt(i - 1) - 'a';

            for (int j = 0; j < 26; j++) {

                int before = alpha[i - 1][j];
                alpha[i][j] = (j == search ? before + 1 : before);
            }
        }

        StringBuilder sb = new StringBuilder();
        while (N-- > 0) {

            StringTokenizer st = new StringTokenizer(br.readLine());

            int find = st.nextToken().charAt(0) - 'a';
            int start = Integer.parseInt(st.nextToken()) + 1;
            int end = Integer.parseInt(st.nextToken()) + 1;

            sb.append(alpha[end][find] - alpha[start - 1][find]).append("\n");
        }

        br.close();
        System.out.println(sb);
    }
}
