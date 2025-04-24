import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int[] input, target;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        input = new int[N];
        target = new int[M];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            input[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(input);
        solution(0);
        System.out.println(sb);

        br.close();
    }

    public static void solution(int depth) {
        // base camp
        if (depth == M) {
            for (int i : target) {
                sb.append(i).append(" ");
            }
            sb.append("\n");
            return;
        }

        int prev = -1;
        for (int i = 0; i < N; i++) {
            // input[i] == prev => 현재 depth 에서 같은 수가 나왔는지
            if (input[i] == prev) continue;

            target[depth] = input[i];
            prev = input[i];

            solution(depth + 1);
        }
    }
}
