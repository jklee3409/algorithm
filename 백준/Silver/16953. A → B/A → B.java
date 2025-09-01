import java.io.*;
import java.util.*;

public class Main {
    static long A, B;

    public static class Num {
        long n, cnt;

        public Num(long n, long cnt) {
            this.n = n;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        A = Long.parseLong(st.nextToken());
        B = Long.parseLong(st.nextToken());

        System.out.println(bfs() == -1 ? -1 : bfs() + 1);
    }

    public static long bfs() {
        Queue<Num> queue = new ArrayDeque<>();
        queue.offer(new Num(A, 0));

        while (!queue.isEmpty()) {
            Num cur = queue.poll();
            
            if (B == cur.n) return cur.cnt;
            if (cur.n > B) continue;

            queue.offer(new Num(cur.n * 10 + 1, cur.cnt + 1));
            queue.offer(new Num(cur.n * 2, cur.cnt + 1));
        }

        return -1;
    }
}
