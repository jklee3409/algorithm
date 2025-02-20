import java.io.*;
import java.util.*;

public class Main {

    static int K, W, H;
    static int[][] board;
    // 말 이동 횟수에 따라 다른 방문 상태 기록
    static boolean[][][] visited; // [y][x][말 이동 횟수]

    static int[] ky = {-1, -2, -2, -1, 1, 2, 2, 1};
    static int[] kx = {-2, -1, 1, 2, -2, -1, 1, 2};

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        K = Integer.parseInt(br.readLine());

        StringTokenizer st = new StringTokenizer(br.readLine());
        W = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());
        board = new int[H][W];
        visited = new boolean[H][W][K + 1];

        for (int i = 0; i < H; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < W; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        System.out.println(bfs());
    }

    public static int bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(0, 0, 0, 0));
        visited[0][0][0] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int y = current.y;
            int x = current.x;
            int kCount = current.kCount;
            int moveCount = current.moveCount;

            // 목적지 도달 체크
            if (y == H - 1 && x == W - 1) {
                return moveCount;
            }

            // 말처럼 이동 (K 번 가능)
            if (kCount < K) {
                for (int i = 0; i < 8; i++) {
                    int ny = y + ky[i];
                    int nx = x + kx[i];

                    if (ny < 0 || nx < 0 || ny >= H || nx >= W) continue;
                    if (board[ny][nx] == 1 || visited[ny][nx][kCount + 1]) continue;

                    queue.offer(new Node(ny, nx, kCount + 1, moveCount + 1));
                    visited[ny][nx][kCount + 1] = true;
                }
            }

            // 일반 이동 (4방 탐색)
            for (int i = 0; i < 4; i++) {
                int ny = y + dy[i];
                int nx = x + dx[i];

                if (ny < 0 || nx < 0 || ny >= H || nx >= W) continue;
                if (board[ny][nx] == 1 || visited[ny][nx][kCount]) continue;

                queue.offer(new Node(ny, nx, kCount, moveCount + 1));
                visited[ny][nx][kCount] = true;
            }
        }

        return -1; // 도달 불가능한 경우
    }

    static class Node {
        int y, x, kCount, moveCount;

        public Node(int y, int x, int kCount, int moveCount) {
            this.y = y;
            this.x = x;
            this.kCount = kCount;
            this.moveCount = moveCount;
        }
    }
}
