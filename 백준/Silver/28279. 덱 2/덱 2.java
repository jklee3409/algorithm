import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        Deque<Integer> deque = new ArrayDeque<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int input = Integer.parseInt(st.nextToken());

            switch (input) {
                case 1:
                    int x = Integer.parseInt(st.nextToken());
                    deque.addFirst(x);
                    break;
                case 2:
                    int y = Integer.parseInt(st.nextToken());
                    deque.addLast(y);
                    break;
                case 3:
                    sb.append(deque.isEmpty() ? -1 : deque.pollFirst());
                    sb.append("\n");
                    break;
                case 4:
                    sb.append(deque.isEmpty() ? -1 : deque.pollLast());
                    sb.append("\n");
                    break;
                case 5:
                    sb.append(deque.size()).append("\n");
                    break;
                case 6:
                    sb.append(deque.isEmpty() ? 1 : 0);
                    sb.append("\n");
                    break;
                case 7:
                    sb.append(deque.isEmpty() ? -1 : deque.peek());
                    sb.append("\n");
                    break;
                case 8:
                    sb.append(deque.isEmpty() ? -1 : deque.peekLast());
                    sb.append("\n");
                    break;
            }
        }

        br.close();
        System.out.println(sb);
    }
}
