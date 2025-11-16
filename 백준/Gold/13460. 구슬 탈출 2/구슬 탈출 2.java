import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static char[][] board;

    static class State {
        int ry, rx;
        int by, bx;
        int depth;

        public State(int ry, int rx, int by, int bx, int depth) {
            this.ry = ry;
            this.rx = rx;
            this.by = by;
            this.bx = bx;
            this.depth = depth;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        board = new char[N][M];

        int startRY = 0, startRX = 0, startBY = 0, startBX = 0;
        for (int i = 0; i < N; i++) {
            String str = br.readLine();
            for (int j = 0; j < M; j++) {
                board[i][j] = str.charAt(j);

                if (str.charAt(j) == 'R') {
                    startRY = i;
                    startRX = j;
                } else if (str.charAt(j) == 'B') {
                    startBY = i;
                    startBX = j;
                }
            }
        }

        System.out.println(bfs(startRY, startRX, startBY, startBX));
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static int bfs(int startRY, int startRX, int startBY, int startBX) {
        Queue<State> queue = new ArrayDeque<>();
        boolean[][][][] visited = new boolean[N][M][N][M];

        queue.offer(new State(startRY, startRX, startBY, startBX, 0));
        visited[startRY][startRX][startBY][startBX] = true;

        while (!queue.isEmpty()) {
            State cur = queue.poll();
            if (cur.depth >= 10) continue;

            for (int i = 0; i < 4; i++) {
                int nRY = cur.ry;
                int nRX = cur.rx;
                int nBY = cur.by;
                int nBX = cur.bx;

                int rCnt = 0;
                while (board[nRY + dy[i]][nRX + dx[i]] != '#' && board[nRY][nRX] != 'O') {
                    nRY += dy[i];
                    nRX += dx[i];
                    rCnt++;
                }

                int bCnt = 0;
                while (board[nBY + dy[i]][nBX + dx[i]] != '#' && board[nBY][nBX] != 'O') {
                    nBY += dy[i];
                    nBX += dx[i];
                    bCnt++;
                }

                if (board[nBY][nBX] == 'O') continue;
                if (visited[nRY][nRX][nBY][nBX]) continue;

                if (board[nRY][nRX] == 'O') return ++cur.depth;

                // 겹침 처리
                if (nRY == nBY && nRX == nBX) {
                    if (rCnt < bCnt) {
                        nBY -= dy[i];
                        nBX -= dx[i];
                    } else {
                        nRY -= dy[i];
                        nRX -= dx[i];
                    }
                }

                queue.offer(new State(nRY, nRX, nBY, nBX, cur.depth + 1));
                visited[nRY][nRX][nBY][nBX] = true;
            }
        }

        return -1;
    }
}
