import java.io.*;
import java.util.*;

public class Main {
    static StringBuilder result = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            String p = br.readLine();
            int n = Integer.parseInt(br.readLine());
            String inputStr = br.readLine();

            Deque<Integer> deque = parser(inputStr);
            solution(deque, p);
        }

        System.out.println(result);
    }

    private static Deque<Integer> parser(String inputStr) {
        Deque<Integer> deque = new ArrayDeque<>();
        String sub = inputStr.substring(1, inputStr.length() - 1);
        if (sub.isEmpty()) return deque;

        String[] parts = sub.split(",");
        for (String s : parts) {
            deque.add(Integer.parseInt(s));
        }
        return deque;
    }

    private static void solution(Deque<Integer> deque, String p) {
        boolean reverse = false;

        for (char cmd : p.toCharArray()) {
            if (cmd == 'R') {
                reverse = !reverse;
            } else if (cmd == 'D') {
                if (deque.isEmpty()) {
                    result.append("error\n");
                    return;
                }
                if (!reverse) {
                    deque.pollFirst();
                } else {
                    deque.pollLast();
                }
            }
        }

        makeResult(deque, reverse);
    }

    private static void makeResult(Deque<Integer> deque, boolean reverse) {
        result.append("[");
        if (!deque.isEmpty()) {
            if (!reverse) {
                while (!deque.isEmpty()) {
                    result.append(deque.pollFirst());
                    if (!deque.isEmpty()) result.append(",");
                }
            } else {
                while (!deque.isEmpty()) {
                    result.append(deque.pollLast());
                    if (!deque.isEmpty()) result.append(",");
                }
            }
        }
        result.append("]\n");
    }
}
