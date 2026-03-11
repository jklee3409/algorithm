import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int[][] map;
    static int[][] dist;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        map = new int[N][M];
        dist = new int[N][M];

        for (int i = 0; i < N; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = input.charAt(j) - '0';
            }
        }

        bfs();

        System.out.println(dist[N - 1][M - 1]);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static void bfs() {
        Deque<int[]> deque = new ArrayDeque<>();

        deque.addFirst(new int[]{0, 0});
        dist[0][0] = 0;

        while (!deque.isEmpty()) {
            int[] cur = deque.poll();

            int y = cur[0];
            int x = cur[1];

            for (int d = 0; d < 4; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;

                int cost = map[ny][nx] == 1 ? 1 : 0;

                if (dist[ny][nx] > dist[y][x] + cost) {
                    dist[ny][nx] = dist[y][x] + cost;

                    if (cost == 0) deque.addFirst(new int[]{ny, nx});
                    else deque.addLast(new int[]{ny, nx});
                }
            }
        }
    }
}