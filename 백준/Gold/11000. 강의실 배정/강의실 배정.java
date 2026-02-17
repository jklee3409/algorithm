import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        int[][] classes = new int[n][2];

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            classes[i][0] = start;
            classes[i][1] = end;
        }

        Arrays.sort(classes, Comparator.comparingInt(a -> a[0]));

        PriorityQueue<Integer> pq = new PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            if (!pq.isEmpty() && pq.peek() <= classes[i][0]) {
                pq.poll();
            }

            pq.offer(classes[i][1]);
        }

        System.out.println(pq.size());
    }
}
