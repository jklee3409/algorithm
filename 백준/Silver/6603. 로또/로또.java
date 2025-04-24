import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static int K;
    static int[] lotto;
    static int[] target = new int[6];
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            K = Integer.parseInt(st.nextToken());

            if (K == 0) break;

            lotto = new int[K];
            for (int i = 0; i < K; i++) {
                lotto[i] = Integer.parseInt(st.nextToken());
            }

            solution(0, 0);
            sb.append("\n");
        }

        System.out.println(sb);
    }

    public static void solution(int depth, int start) {
        if (depth == 6) {
            for (int i : target) {
                sb.append(i).append(" ");
            }
            sb.append("\n");
            return;
        }

        for (int i = start; i < K; i++) {
            target[depth] = lotto[i];

            solution(depth + 1, i + 1);
        }
    }
}
