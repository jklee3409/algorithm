import java.io.*;
import java.util.*;

public class Main {

    static int N, M, K;
    static int[][] map;
    static boolean[][][] visited;

    static class Node {
        int y, x, dist, broken;

        Node(int y, int x, int dist, int broken) {
            this.y = y;
            this.x = x;
            this.dist = dist;
            this.broken = broken;
        }
    }

    static int[] dy = {1, -1, 0, 0};
    static int[] dx = {0, 0, 1, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        map = new int[N][M];
        visited = new boolean[N][M][K + 1];

        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < M; j++) {
                map[i][j] = line.charAt(j) - '0';
            }
        }

        System.out.println(bfs());
    }

    static int bfs() {
        Queue<Node> q = new LinkedList<>();
        q.offer(new Node(0, 0, 1, 0));
        visited[0][0][0] = true;

        while (!q.isEmpty()) {
            Node cur = q.poll();

            if (cur.y == N - 1 && cur.x == M - 1) {
                return cur.dist;
            }

            for (int d = 0; d < 4; d++) {
                int ny = cur.y + dy[d];
                int nx = cur.x + dx[d];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;

                // 빈 칸
                if (map[ny][nx] == 0 && !visited[ny][nx][cur.broken]) {
                    visited[ny][nx][cur.broken] = true;
                    q.offer(new Node(ny, nx, cur.dist + 1, cur.broken));
                }

                // 벽
                if (map[ny][nx] == 1 && cur.broken < K && !visited[ny][nx][cur.broken + 1]) {
                    visited[ny][nx][cur.broken + 1] = true;
                    q.offer(new Node(ny, nx, cur.dist + 1, cur.broken + 1));
                }
            }
        }

        return -1;
    }
}