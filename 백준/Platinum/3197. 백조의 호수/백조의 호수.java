import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static char[][] lake;

    static Queue<int[]> swanQ = new ArrayDeque<>();
    static Queue<int[]> nextSwanQ = new ArrayDeque<>();
    static Queue<int[]> waterQ = new ArrayDeque<>();
    static Queue<int[]> nextWaterQ = new ArrayDeque<>();

    static boolean[][] swanVisited;
    static boolean[][] waterVisited;

    static int[] swan1, swan2;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        lake = new char[N][M];
        swanVisited = new boolean[N][M];
        waterVisited = new boolean[N][M];

        List<int[]> swans = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            String input = br.readLine();
            for (int j = 0; j < M; j++) {
                lake[i][j] = input.charAt(j);

                if (lake[i][j] != 'X') {
                    waterQ.offer(new int[]{i, j});
                    waterVisited[i][j] = true;
                }

                if (lake[i][j] == 'L') {
                    swans.add(new int[]{i, j});
                }
            }
        }

        swan1 = swans.get(0);
        swan2 = swans.get(1);

        swanQ.offer(swan1);
        swanVisited[swan1[0]][swan1[1]] = true;

        int day = 0;

        while (true) {
            if (moveSwan()) {
                System.out.println(day);
                return;
            }

            melt();
            swanQ = nextSwanQ;
            nextSwanQ = new ArrayDeque<>();
            waterQ = nextWaterQ;
            nextWaterQ = new ArrayDeque<>();
            
            day++;
        }
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static boolean moveSwan() {
        while (!swanQ.isEmpty()) {
            int[] cur = swanQ.poll();
            int y = cur[0];
            int x = cur[1];

            if (y == swan2[0] && x == swan2[1]) {
                return true;
            }

            for (int d = 0; d < 4; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (swanVisited[ny][nx]) continue;

                swanVisited[ny][nx] = true;

                if (lake[ny][nx] == 'X') {
                    nextSwanQ.offer(new int[]{ny, nx});
                } else {
                    swanQ.offer(new int[]{ny, nx});
                }
            }
        }
        return false;
    }

    private static void melt() {
        while (!waterQ.isEmpty()) {
            int[] cur = waterQ.poll();
            int y = cur[0];
            int x = cur[1];

            for (int d = 0; d < 4; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (waterVisited[ny][nx]) continue;

                waterVisited[ny][nx] = true;

                if (lake[ny][nx] == 'X') {
                    lake[ny][nx] = '.';
                    nextWaterQ.offer(new int[]{ny, nx});
                }
            }
        }
    }
}