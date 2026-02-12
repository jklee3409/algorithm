import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static Queue<Integer> left = new PriorityQueue<>(Collections.reverseOrder());
    static Queue<Integer> right = new PriorityQueue<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < N; i++) {
            int num = Integer.parseInt(br.readLine());
            addNumber(num);
            sb.append(getMedian()).append("\n");
        }

        System.out.println(sb);
    }

    private static void addNumber(int num) {
        if (left.size() == right.size()) left.add(num);
        else right.add(num);

        if (!right.isEmpty() && left.peek() > right.peek()) {
            int l = left.poll();
            int r = right.poll();

            left.add(r);
            right.add(l);
        }
    }


    private static int getMedian() {
        return left.peek();
    }
}