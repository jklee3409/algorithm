import java.io.*;
import java.util.*;

public class Main {

    static class Node {
        int value;
        int index;

        Node(int value, int index) {
            this.value = value;
            this.index = index;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int L = Integer.parseInt(st.nextToken());

        Deque<Node> deque = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(st.nextToken());

            // 1. 뒤에서 현재 값보다 큰 값 제거
            while (!deque.isEmpty() && deque.peekLast().value > num) {
                deque.pollLast();
            }

            // 2. 현재 값 추가
            deque.addLast(new Node(num, i));

            // 3. 윈도우 범위 벗어난 값 제거
            if (deque.peekFirst().index <= i - L) {
                deque.pollFirst();
            }

            // 4. 최소값 저장
            sb.append(deque.peekFirst().value).append(" ");
        }

        System.out.println(sb);
    }
}