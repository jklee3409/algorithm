import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());

            if (x == 0 && pq.isEmpty()) {
                sb.append(0).append("\n");
                continue;
            } else if (x == 0) {
                sb.append(pq.poll()).append("\n");
                continue;
            }

            pq.add(x);
        }

        System.out.println(sb);
    }
}
