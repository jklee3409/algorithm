import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int r, c, d;
    static int[][] room;
    static boolean[][] clean;
    //                 북  동 남  서
    static int[] dy = {-1, 0, 1, 0};
    static int[] dx = {0, 1, 0, -1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 행
        M = Integer.parseInt(st.nextToken()); // 열
        room = new int[N][M];
        clean = new boolean[N][M];

        st = new StringTokenizer(br.readLine());
        // 로봇 청소기 위치
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        // 로봇 청소기 초기 방향
        d = Integer.parseInt(st.nextToken());

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                room[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int cleanRoomCnt = 0;
        while (true) {
            // 1. 현재 칸이 청소되지 않은 경우, 청소한다.
            if (!clean[r][c]) {
                clean[r][c] = true;
                cleanRoomCnt++;
            }

            // 2. 주변 4칸 중 청소할 곳이 있는지 확인

            // 2-1. 청소할 곳이 없다면 후진 시도
            if (!hasUncleanSpace()) {
                int backDir = (d + 2) % 4;
                int ny = r + dy[backDir];
                int nx = c + dx[backDir];

                // 후진 가능하면 후진
                if (isValidPosition(ny, nx)) {
                    r = ny;
                    c = nx;
                    continue;
                } else { // 후진 불가능하면 종료
                    break;
                }
            }

            // 3. 청소할 곳이 있다면 반시계 방향으로 회전 후 탐색
            for (int i = 0; i < 4; i++) {
                d = (d + 3) % 4; // 반시계 방향으로 90도 회전

                int ny = r + dy[d];
                int nx = c + dx[d];

                // 앞쪽 칸이 청소되지 않은 빈 칸이라면 전진
                if (isValidPosition(ny, nx) && !clean[ny][nx]) {
                    r = ny;
                    c = nx;
                    break; //전진 후 1번으로 되돌아감.
                }
            }
        }

        System.out.println(cleanRoomCnt);
    }

    // 현재 위치에서 주변 4칸을 탐색하여 청소되지 않은 곳이 있는지 파악
    static boolean hasUncleanSpace() {
        for (int i = 0; i < 4; i++) {
            int ny = r + dy[i];
            int nx = c + dx[i];
            if (isValidPosition(ny, nx) && !clean[ny][nx]) {
                return true;
            }
        }

        return false;
    }

    static boolean isValidPosition(int ny, int nx) {
        return ny >= 0 && nx >= 0 && ny < N && nx < M  && room[ny][nx] == 0;
    }
}
