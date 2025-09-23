import java.io.*;
import java.util.*;

public class Main {
    public static final int HUNDRED = 100;
    static int[] map = new int[HUNDRED + 1];
    static int[] ladder = new int[HUNDRED + 1];
    static int[] snake = new int[HUNDRED + 1];

    static class Point {
        int pos, cnt;

        public Point(int pos, int cnt) {
            this.pos = pos;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken()); // 사다리 수
        int M = Integer.parseInt(st.nextToken()); // 뱀 수

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            ladder[start] = end;
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());

            snake[start] = end;
        }

        System.out.println(bfs());
    }

    static int[] dice = {1, 2, 3, 4, 5, 6};

    public static int bfs() {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[HUNDRED + 1];

        queue.offer(new Point(1, 0));
        visited[1] = true;

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            if (p.pos == 100) return p.cnt;

            for (int i = 0; i < 6; i++) {
                int nPos = p.pos + dice[i];

                if (nPos > 100 || visited[nPos]) continue;

                if (ladder[nPos] != 0) nPos = ladder[nPos];
                else if (snake[nPos] != 0) nPos = snake[nPos];

                queue.offer(new Point(nPos, p.cnt + 1));
                visited[nPos] = true;
            }
        }

        return 0;
    }
}
