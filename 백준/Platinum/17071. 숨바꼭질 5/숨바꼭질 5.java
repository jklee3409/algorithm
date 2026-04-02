import java.io.*;
import java.util.*;

public class Main {

    static boolean[][] visited = new boolean[2][500001];

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        System.out.println(bfs(N, K));
    }

    private static int bfs(int start, int K) {
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[]{start, 0}); // 0: 수빈이 위치, 1: 시간
        visited[0][start] = true;

        while (!q.isEmpty()) {
            int[] p = q.poll();

            int time = p[1];

            int kPos = K + time * (time + 1) / 2;
            if (kPos > 500000) return -1;

            if (visited[time % 2][kPos]) return time;

            int[] dx = {p[0], 1, -1};

            for (int d = 0; d < 3; d++) {
                int nx = p[0] + dx[d];
                int nextTime = time + 1;

                if (nx < 0 || nx > 500000 || visited[nextTime % 2][nx]) continue;

                q.offer(new int[]{nx, nextTime});
                visited[nextTime % 2][nx] = true;
            }
        }

        return -1;
    }
}