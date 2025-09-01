import java.io.*;
import java.util.*;

public class Main {
    static String input;
    static Stack<Character> stack = new Stack<>();
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        input = br.readLine();

        solution();
        System.out.println(sb);
    }

    public static void solution() {
        boolean isTag = false;

        for (int i = 0; i < input.length(); i++) {
            char cur = input.charAt(i);

            if (cur == '<') {
                while (!stack.isEmpty()) {
                    sb.append(stack.pop());
                }
                isTag = true;
                sb.append(cur);

            } else if (cur == '>') {
                isTag = false;
                sb.append(cur);

            } else if (isTag) { // 태그 내부
                sb.append(cur);

            } else { // 태그 외부
                if (cur == ' ') {
                    while (!stack.isEmpty()) {
                        sb.append(stack.pop());
                    }
                    sb.append(' ');
                } else {
                    stack.push(cur);
                }
            }
        }

        while (!stack.isEmpty()) {
            sb.append(stack.pop());
        }
    }
}
