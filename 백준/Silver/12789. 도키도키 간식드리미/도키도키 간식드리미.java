import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        int N = Integer.parseInt(br.readLine());
        Queue<Integer> queue = new LinkedList<>();

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            queue.add(Integer.parseInt(st.nextToken()));
        }

        if (niceLine(queue)) {
            sb.append("Nice");
        } else {
            sb.append("Sad");
        }

        br.close();
        System.out.println(sb);
    }

    public static boolean niceLine(Queue<Integer> queue) {
        Stack<Integer> stack = new Stack<>();
        int min = 1;

        while (!queue.isEmpty()) {
            if (queue.peek() == min) {
                queue.poll();
                min++;
            } else {
                while (!stack.isEmpty() && stack.peek() == min) {
                    stack.pop();
                    min++;
                }
                stack.push(queue.poll());
                while (!stack.isEmpty() && stack.peek() == min) {
                    stack.pop();
                    min++;
                }
            }
        }

        while (!stack.isEmpty() && stack.peek() == min) {
            stack.pop();
            min++;
        }

        return stack.isEmpty();
    }
}
