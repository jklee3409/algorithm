import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int TIME = 0, CNT = 0;
    static Node[][] board;

    static class Node {
        private int y, x, status;
        private boolean isOutside;

        public Node(int y, int x, int status, boolean isOutside) {
            this.y = y;
            this.x = x;
            this.status = status;
            this.isOutside = isOutside;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        board = new Node[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                board[i][j] = new Node(i, j, Integer.parseInt(st.nextToken()), false);
            }
        }

        while (!isEmpty()) {
            bfs();
            finalCheeseCntCount();
            changeStatus();
            TIME++;
        }

        System.out.println(TIME);
        System.out.println(CNT);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void bfs() {
        Queue<Node> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][M];

        queue.offer(board[0][0]);
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            Node node = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = node.y + dy[i];
                int nx = node.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (visited[ny][nx]) continue;

                if (board[ny][nx].status == 1) {
                    board[ny][nx].isOutside = true;
                    visited[ny][nx] = true;
                    continue;
                }

                queue.offer(new Node(ny, nx, board[ny][nx].status, board[ny][nx].isOutside));
                visited[ny][nx] = true;
            }
        }
    }

    static void changeStatus() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j].status == 1 && board[i][j].isOutside) board[i][j].status = 0;
            }
        }
    }

    static boolean isEmpty() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (board[i][j].status == 1) return false;
            }
        }

        return true;
    }

    static void finalCheeseCntCount() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {

                if (board[i][j].status == 1 && board[i][j].isOutside) {
                    CNT++;
                } else if (board[i][j].status == 1 && !board[i][j].isOutside){
                    CNT = 0;
                    return;
                }
            }
        }
    }
}
