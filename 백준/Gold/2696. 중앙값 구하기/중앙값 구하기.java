import java.io.*;
import java.util.*;

public class Main {
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();
            int M = Integer.parseInt(br.readLine());
            int size = M / 2 + 1;

            sb.append(size).append("\n");
            if (M > 10) {
                int repeat = (9 + M) / 10;

                for (int i = 0; i < repeat; i++) {
                    StringTokenizer st = new StringTokenizer(br.readLine());
                    for (int j = i * 10; j < Math.min(M, (i + 1) * 10); j++) {
                        solution(maxHeap, minHeap, Integer.parseInt(st.nextToken()), j);
                    }
                }

            } else {
                StringTokenizer st = new StringTokenizer(br.readLine());
                for (int i = 0; i < M; i++) {
                    solution(maxHeap, minHeap, Integer.parseInt(st.nextToken()), i);
                }
            }

            sb.append("\n");
        }

        System.out.println(sb);
    }

    static void solution(PriorityQueue<Integer> maxHeap, PriorityQueue<Integer> minHeap, int input, int idx) {
        idx++;
        maxHeap.add(input);

        if (!maxHeap.isEmpty() && !minHeap.isEmpty()) {
            if (maxHeap.peek() > minHeap.peek()) minHeap.add(maxHeap.poll());
        }

        if (maxHeap.size() > minHeap.size() + 1) minHeap.add(maxHeap.poll());
        else if (maxHeap.size() < minHeap.size()) maxHeap.add(minHeap.poll());

        if (idx != 1 && idx % 20 == 1) sb.append("\n");
        if (idx % 2 != 0) sb.append(maxHeap.peek()).append(" ");
    }
}
