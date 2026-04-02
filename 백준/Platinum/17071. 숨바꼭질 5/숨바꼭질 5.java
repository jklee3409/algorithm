import java.io.*;
import java.util.*;

public class Main {

    static int N, K;
    static boolean[][] visited = new boolean[2][500001];

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        System.out.println(bfs(N));
    }

    private static int bfs(int start) {
        Queue<Integer> q = new ArrayDeque<>();

        int time = 0;
        q.offer(start);
        visited[0][start] = true;

        while (!q.isEmpty()) {
            int kPos = K + time * (time + 1) / 2;

            if (kPos > 500000) return -1;
            if (visited[time % 2][kPos]) return time;

            int size = q.size();
            int nextParity = (time + 1) % 2;

            for (int i = 0; i < size; i++) {
                int cur = q.poll();

                int[] dx = {cur, 1, -1};

                for (int d = 0; d < 3; d++) {
                    int next = cur + dx[d];

                    if (next < 0 || next > 500000 || visited[nextParity][next]) continue;

                    q.offer(next);
                    visited[nextParity][next] = true;
                }
            }

            time++;
        }

        return -1;
    }
}