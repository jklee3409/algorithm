import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static char[][] board;
    static boolean cycle;

    static final int[] DY = {-1, 1, 0, 0};
    static final int[] DX = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        board = new char[N][M];

        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < M; j++) {
                board[i][j] = input.charAt(j);
            }
        }

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                
                boolean[][] visited = new boolean[N][M];
                visited[y][x] = true;

                dfs(-1, -1, y, x, visited, board[y][x]);

                if (cycle) {
                    System.out.println("Yes");
                    return;
                }
            }
        }

        System.out.println("No");
    }

    private static void dfs(int prevY, int prevX, int curY, int curX, boolean[][] visited, char color) {
        if (cycle) return;
        
        for (int d = 0; d < 4; d++) {
            int ny = curY + DY[d];
            int nx = curX + DX[d];

            if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;

            if (board[ny][nx] != color) continue;

            if (ny == prevY && nx == prevX) continue;

            if (visited[ny][nx]) {
                cycle = true;
                return;
            }

            visited[ny][nx] = true;
            
            dfs(curY, curX, ny, nx, visited, color);
        }
    }
}