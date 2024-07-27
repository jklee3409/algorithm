import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        Queue<Integer> queue = new LinkedList<>();

        for (int i = 1; i < N + 1; i++) {
            queue.add(i);
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<");

        for (int i = 0; i < N; i++) {
            for (int j = 1; j < K; j++) {
                queue.offer(queue.poll());
            }
            if (i == N - 1) {
                sb.append(queue.poll());
            } else {
                sb.append(queue.poll()).append(", ");
            }
        }

        sb.append(">");
        br.close();
        System.out.println(sb);
    }
}
