import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < N; i++) {
            pq.add(Integer.parseInt(br.readLine()));
        }

        List<Integer> list = new ArrayList<>();
        while (pq.size() > 1) {
            int sum = pq.poll() + pq.poll();
            pq.add(sum);
            list.add(sum);
        }

        int result = 0;
        for (Integer i : list) {
            result += i;
        }
        System.out.println(result);
    }
}
