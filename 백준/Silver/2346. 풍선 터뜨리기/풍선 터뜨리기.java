import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 1; i <= N; i++) {
            deque.add(i);
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        int[] arr = new int[N];
        for (int i = 0; i < N; i++) {
            int K = Integer.parseInt(st.nextToken());
            arr[i] = K;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(1).append(" ");

        for (int i = 0; i < N - 1; i++) {
            int rotate = arr[deque.peek() - 1];
            deque.poll();
            if (0 < rotate) {
                rotate--;
                for (int j = 0; j < rotate; j++) {
                    deque.addLast(deque.poll());
                }
                sb.append(deque.peek()).append(" ");
            } else {
                for (int k = rotate; k < 0; k++) {
                    deque.addFirst(deque.pollLast());
                }
                sb.append(deque.peek()).append(" ");
            }
        }

        br.close();
        System.out.println(sb);
    }
}
