import java.io.*;
import java.util.Stack;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        int N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];
        int[] result = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < N; i++) {
            // 스택이 비어 있지 않고, 스택의 top 값이 현재 값보다 작다면 pop
            while (!stack.isEmpty() && arr[stack.peek()] < arr[i]) {
                stack.pop();
            }

            // 스택이 비어 있다면 0, 그렇지 않다면 인덱스 + 1 저장
            result[i] = stack.isEmpty() ? 0 : stack.peek() + 1;

            // 현재 인덱스를 스택에 push
            stack.push(i);
        }

        for (int i = 0; i < N; i++) {
            bw.write(result[i] + " ");
        }

        bw.flush();
        br.close();
        bw.close();
    }
}
