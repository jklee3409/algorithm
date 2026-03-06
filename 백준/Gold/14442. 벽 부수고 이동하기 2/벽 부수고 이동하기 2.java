import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K;
    static int[][] map;

    static class Node {
        int y, x, dist, broken;

        public Node(int y, int x, int dist, int broken) {
            this.y = y;
            this.x = x;
            this.dist = dist;
            this.broken = broken;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        map = new int[N][M];

        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < M; j++) {
                int num = input.charAt(j) - '0';
                map[i][j] = num;
            }
        }

        System.out.println(bfs());
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static int bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        boolean[][][] visited = new boolean[N][M][K + 1];

        queue.offer(new Node(0, 0, 0, 0));
        visited[0][0][0] = true;

        while (!queue.isEmpty()) {
            Node n = queue.poll();

            if (n.y == N - 1 && n.x == M - 1) return n.dist + 1;

            for (int i = 0; i < 4; i++) {
                int ny = n.y + dy[i];
                int nx = n.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;

                if (map[ny][nx] == 1) {
                    if (n.broken == K) continue;
                    if (visited[ny][nx][n.broken + 1]) continue;

                    queue.offer(new Node(ny, nx, n.dist + 1, n.broken + 1));
                    visited[ny][nx][n.broken + 1] = true;
                } else {
                    if (visited[ny][nx][n.broken]) continue;

                    queue.offer(new Node(ny, nx, n.dist + 1, n.broken));
                    visited[ny][nx][n.broken] = true;
                }
            }
        }

        return -1;
    }
}