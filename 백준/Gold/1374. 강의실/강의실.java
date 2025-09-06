import java.io.*;
import java.util.*;

public class Main {

    static class Lecture {
        int num, start, end;

        Lecture(int num, int start, int end) {
            this.num = num;
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        Lecture[] lectures = new Lecture[N];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            lectures[i] = new Lecture(num, start, end);
        }

        Arrays.sort(lectures, Comparator.comparingInt(l -> l.start));

        PriorityQueue<Integer> pq = new PriorityQueue<>();

        pq.add(lectures[0].end);

        for (int i = 1; i < N; i++) {
            if (pq.peek() <= lectures[i].start) {
                pq.poll();
            }
            pq.add(lectures[i].end);
        }

        System.out.println(pq.size());
    }
}
