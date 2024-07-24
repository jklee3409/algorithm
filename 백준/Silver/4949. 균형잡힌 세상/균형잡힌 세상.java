import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        while (true) {
            String s = br.readLine();
            if (Objects.equals(s, ".")) {
                break;
            }

            if (is_VPS(s)) {
                sb.append("yes").append("\n");
            } else {
                sb.append("no").append("\n");
            }
        }

        br.close();
        System.out.println(sb);
    }

    public static boolean is_VPS(String s) {
        boolean check;
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            char now = s.charAt(i);
            if (now == '(' || now == '[') {
                stack.push(now);
            } else if((!stack.isEmpty() && now == ')' && stack.peek() == '(') || (!stack.isEmpty() && now == ']' && stack.peek() == '[')){
                stack.pop();
            } else {
                if(now == ')' || now == ']'){
                    return false;
                }
            }
        }

        check = stack.isEmpty();

        return check;
    }
}
