import java.io.*;
import java.util.*;

public class Main {
    static int R, C, T;
    static int A1 = 0, A2 = 0;
    static int[][] room;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        T = Integer.parseInt(st.nextToken());
        room = new int[R][C];

        for (int i = 0; i < R; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < C; j++) {
                room[i][j] = Integer.parseInt(st.nextToken());

                if (room[i][j] == -1 && A1 == 0) A1 = i;
                else if (room[i][j] == -1 && A2 == 0) A2 = i;
            }
        }

        for (int i = 0; i < T; i++) {
            spread();
            purify();
        }

        System.out.println(calculate());
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void spread() {
        int[][] temp = new int[R][C];

        for (int y = 0; y < R; y++) {
            for (int x = 0; x < C; x++) {
                if (room[y][x] == -1) continue;

                int spreadCnt = 0; // 확산된 칸의 개수
                int spreadAmount = room[y][x] / 5; // 확산되는 미세먼지의 양

                for (int d = 0; d < 4; d++) {
                    int ny = y + dy[d];
                    int nx = x + dx[d];

                    if (isValidSpreadPos(ny, nx)) {
                        temp[ny][nx] += spreadAmount; // 임시 집에서 확산되는 미세먼지 양 저장
                        spreadCnt++;
                    }
                }

                room[y][x] -= spreadCnt * spreadAmount; // 기존 집에서 확산되는 미세먼지 양 차감
            }
        }

        // 합산
        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                if (room[i][j] == -1) continue;

                room[i][j] += temp[i][j];
            }
        }
    }

    static void purify() {
        int last1 = 0, last2 = 0;

        // 오른쪽으로 밀기
        {
            int[][] temp = new int[2][C];

            for (int i = 1; i < C - 1; i++) {
                temp[0][i + 1] = room[A1][i];
                temp[1][i + 1] = room[A2][i];

                room[A1][i] = 0;
                room[A2][i] = 0;
            }

            last1 = room[A1][C - 1];
            last2 = room[A2][C - 1];

            room[A1][C - 1] = 0;
            room[A2][C - 1] = 0;

            for (int i = 1; i < C; i++) {
                room[A1][i] = temp[0][i];
                room[A2][i] = temp[1][i];
            }
        }

        // 오른쪽 벽 끝에서 위아래로 확산
        {
            int[][] temp = new int[R][2];

            temp[A1 - 1][0] = last1;
            temp[A2 + 1][1] = last2;

            for (int i = A1 - 1; i > 0; i--) {
                temp[i - 1][0] = room[i][C - 1];
                room[i][C - 1] = 0;
            }

            for (int i = A2 + 1; i < R - 1; i++) {
                temp[i + 1][1] = room[i][C - 1];
                room[i][C - 1] = 0;
            }

            last1 = room[0][C - 1];
            last2 = room[R - 1][C - 1];

            room[0][C - 1] = 0;
            room[R - 1][C - 1] = 0;

            for (int i = A1 - 1; i >= 0; i--) {
                room[i][C - 1] = temp[i][0];
            }

            for (int i = A2 + 1; i < R; i++) {
                room[i][C - 1] = temp[i][1];
            }
        }

        // 왼쪽으로 밀기
        {
            int[][] temp = new int[2][C];

            temp[0][C - 2] = last1;
            temp[1][C - 2] = last2;

            for (int i = C - 2; i > 0; i--) {
                temp[0][i - 1] = room[0][i];
                temp[1][i - 1] = room[R - 1][i];
            }

            last1 = room[0][0];
            last2 = room[R - 1][0];

            room[0][0] = 0;
            room[R - 1][0] = 0;

            for (int i = C - 2; i >= 0; i--) {
                room[0][i] = temp[0][i];
                room[R - 1][i] = temp[1][i];
            }
        }

        // 왼쪽 벽 끝에서 위아래로 확산
        {
            int[][] temp = new int[R][2];

            temp[1][0] = last1;
            temp[R - 2][1] = last2;

            for (int i = 1; i < A1 - 1; i++) {
                temp[i + 1][0] = room[i][0];
            }

            for (int i = R - 2; i > A2 + 1; i--) {
                temp[i - 1][1] = room[i][0];
            }

            room[A1 - 1][0] = 0;
            room[A2 + 1][0] = 0;

            for (int i = 1; i < A1; i++) {
                room[i][0] = temp[i][0];
            }

            for (int i = R - 2; i > A2; i--) {
                room[i][0] = temp[i][1];
            }
        }
    }

    static boolean isValidSpreadPos(int y, int x) {
        if (y < 0 || x < 0 || y >= R || x >= C) return false; // 유효한 범위인지
        if (room[y][x] == -1) return false; // 공기청정기가 있는 칸인지

        return true;
    }

    static int calculate() {
        int result = 0;

        for (int i = 0; i < R; i++) {
            for (int j = 0; j < C; j++) {
                result += room[i][j];
            }
        }

        return result + 2;
    }

    static void printArr() {
        StringBuilder sb = new StringBuilder();

        for (int[] arr : room) {
            for (int i : arr) {
                sb.append(i).append(" ");
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }
}