import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        while (true) {
            String s = br.readLine();
            if (".".equals(s)) {
                break;
            }

            sb.append(isValidParentheses(s) ? "yes" : "no").append("\n");
        }

        System.out.println(sb);
    }

    public static boolean isValidParentheses(String s) {
        char[] stack = new char[s.length()];  // 스택을 배열로 직접 구현
        int size = 0;  // 현재 스택 크기 (top 위치)

        for (char c : s.toCharArray()) {
            if (c == '(' || c == '[') {
                stack[size++] = c;  // push 연산
            } else if (c == ')' || c == ']') {
                if (size == 0 || !isMatchingPair(stack[size - 1], c)) {
                    return false;
                }
                size--;  // pop 연산
            }
        }

        return size == 0;
    }

    private static boolean isMatchingPair(char open, char close) {
        return (open == '(' && close == ')') || (open == '[' && close == ']');
    }
}
