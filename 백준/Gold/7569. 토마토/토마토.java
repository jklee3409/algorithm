import java.io.*;
import java.util.*;

public class Main {
    static int N, M, H;
    static int[][][] storage;
    static Queue<Tomato> tomatoes = new ArrayDeque<>();
    static boolean[][][] visited;

    static class Tomato{
        int z, y, x, days;

        public Tomato(int z, int y, int x, int days) {
            this.z = z;
            this.y = y;
            this.x = x;
            this.days = days;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        H = Integer.parseInt(st.nextToken());
        storage = new int[H][N][M];

        for (int i = 0; i < H; i++) {
            for (int j = 0; j < N; j++) {
                st = new StringTokenizer(br.readLine());
                for (int k = 0; k < M; k++) {
                    int tomato = Integer.parseInt(st.nextToken());
                    storage[i][j][k] = tomato;

                    if (tomato == 1) tomatoes.offer(new Tomato(i, j, k, 0)); // 익은 토마토 저장
                }
            }
        }

        bfs();
        br.close();
    }

    static int[] dz = {-1, 1, 0, 0, 0, 0};
    static int[] dy = {0, 0, -1, 1, 0, 0};
    static int[] dx = {0, 0, 0, 0, -1, 1};

    static void bfs() {
        int days = 0;

        while (!tomatoes.isEmpty()) {
            Tomato tomato = tomatoes.poll();
            days = tomato.days;

            for (int i = 0; i < 6; i++) {
                int nz = tomato.z + dz[i];
                int ny = tomato.y + dy[i];
                int nx = tomato.x + dx[i];

                if (nz < 0 || ny < 0 || nx < 0 || nz >= H || ny >= N || nx >= M) continue; // 범위 검사
                if (storage[nz][ny][nx] == -1) continue; // 빈 칸

                if (storage[nz][ny][nx] == 0) { // 안 익었으면
                    storage[nz][ny][nx] = 1; // 익히고
                    tomatoes.offer(new Tomato(nz, ny, nx, tomato.days + 1)); // days + 1
                }
            }
        }

        // 모든 토마토 익었는지
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < M; k++) {

                    if (storage[i][j][k] == 0) {
                        days = -1;
                        break;
                    }
                }
            }
        }

        System.out.println(days);
    }
}
