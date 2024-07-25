import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        Deque<Integer> queue = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String input = st.nextToken();

            switch (input) {
                case "push":
                    int x = Integer.parseInt(st.nextToken());
                    queue.add(x);
                    break;
                case "pop":
                    sb.append(queue.isEmpty() ? -1 : queue.poll());
                    sb.append("\n");
                    break;
                case "size":
                    sb.append(queue.size()).append("\n");
                    break;
                case "empty":
                    sb.append(queue.isEmpty() ? 1 : 0);
                    sb.append("\n");
                    break;
                case "front":
                    sb.append(queue.isEmpty() ? -1 : queue.peek());
                    sb.append("\n");
                    break;
                case "back":
                    sb.append(queue.isEmpty() ? -1 : queue.peekLast());
                    sb.append("\n");
                    break;
            }
        }

        br.close();
        System.out.println(sb);
    }
}
