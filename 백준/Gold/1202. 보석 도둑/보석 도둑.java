import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        int[][] jewels = new int[N][2];
        int[] bag = new int[K];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            jewels[i][0] = Integer.parseInt(st.nextToken()); // weight
            jewels[i][1] = Integer.parseInt(st.nextToken()); // price
        }

        for (int i = 0; i < K; i++) {
            bag[i] = Integer.parseInt(br.readLine());
        }

        Arrays.sort(jewels, (a, b) -> Integer.compare(a[0], b[0]));
        Arrays.sort(bag);
        
        int idx = 0;
        long result = 0;
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int i = 0; i < K; i++) {
            while (idx < N && jewels[idx][0] <= bag[i]) {
                pq.add(jewels[idx][1]);
                idx++;
            }

            if (!pq.isEmpty()) {
                result += pq.poll();
            }
        }

        System.out.println(result);
    }
}
