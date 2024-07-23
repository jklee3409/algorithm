import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < T; i++) {
            String s = br.readLine();
            if (is_VPS(s)) {
                sb.append("YES").append("\n");
            } else {
                sb.append("NO").append("\n");
            }
        }

        br.close();
        System.out.println(sb);
    }

    public static boolean is_VPS(String s) {
        boolean check= true;
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push('(');
            } else if(!stack.isEmpty() && s.charAt(i) == ')' && stack.peek() == '('){
                stack.pop();
            } else {
                check = false;
                break;
            }
        }

        if (!stack.isEmpty()) {
            check = false;
        }

        return check;
    }
}
