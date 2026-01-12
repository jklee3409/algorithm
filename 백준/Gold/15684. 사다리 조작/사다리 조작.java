import java.io.*;
import java.util.*;

public class Main {
    static int N, M, H;
    static int[][] map;
    static boolean finish = false;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());

        map = new int[H + 1][N + 1];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            map[a][b] = 1;
        }

        for (int i = 0; i <= 3; i++) {
            dfs(0, i, 1, 1);
            if (finish) return;
        }

        System.out.println(-1);
    }

    static void dfs(int cnt, int target, int r, int c) {
        if (finish) return;

        if (cnt == target) {
            if (check()) {
                System.out.println(target);
                finish = true;
            }
            return;
        }

        for (int i = r; i <= H; i++) {
            int startC = (i == r) ? c : 1;
            for (int j = startC; j < N; j++) {
                if (map[i][j] == 0 && map[i][j - 1] == 0 && map[i][j + 1] == 0) {
                    map[i][j] = 1;
                    dfs(cnt + 1, target, i, j + 2);
                    map[i][j] = 0;
                }
            }
        }
    }

    static boolean check() {
        for (int i = 1; i <= N; i++) {
            int pos = i;
            for (int j = 1; j <= H; j++) {
                if (map[j][pos] == 1) {
                    pos++;
                } else if (map[j][pos - 1] == 1) {
                    pos--;
                }
            }
            if (pos != i) return false;
        }
        return true;
    }
}