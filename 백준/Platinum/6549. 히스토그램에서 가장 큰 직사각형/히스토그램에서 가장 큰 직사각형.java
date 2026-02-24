import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        while (true) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(st.nextToken());

            if (n == 0) break;

            long[] h = new long[n + 1];
            for (int i = 0; i < n; i++) h[i] = Long.parseLong(st.nextToken());

            h[n] = 0;

            Stack<Integer> stack = new Stack<>();
            long max = 0;

            for (int i = 0; i <= n; i++) {
                while (!stack.isEmpty() && h[stack.peek()] > h[i]) {
                    long height = h[stack.pop()];

                    long width;
                    if (stack.isEmpty()) {
                        width = i;
                    } else {
                        width = i - stack.peek() - 1;
                    }

                    max = Math.max(max, height * width);
                }
                stack.push(i);
            }

            sb.append(max).append("\n");
        }

        System.out.print(sb);
    }
}