import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static char[][] board;
    static boolean cycle;

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

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                char c = board[i][j];

                boolean[][] visited = new boolean[N][M];
                visited[i][j] = true;

                dfs(new int[]{0, 0}, new int[]{i, j}, visited, c);

                if (cycle) {
                    System.out.println("Yes");
                    return;
                }
            }
        }

        System.out.println("No");
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static void dfs(int[] prev, int[] cur, boolean[][] visited, char c) {
        int curY = cur[0], curX = cur[1];
        int prevY = prev[0], prevX = prev[1];

        for (int d = 0; d < 4; d++) {
            int ny = curY + dy[d];
            int nx = curX + dx[d];

            if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
            if (board[ny][nx] != c) continue;

            if ((ny != prevY && nx != prevX) && visited[ny][nx]) {
                cycle = true;
                return;
            }

            if (visited[ny][nx]) continue;

            visited[ny][nx] = true;

            dfs(new int[]{curY, curX}, new int[]{ny, nx}, visited, c);
        }
    }
}