import java.io.*;
import java.util.*;

public class Main {
    static int R, C, M;
    static Shark[][] fishingSpot;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken()); // 행
        C = Integer.parseInt(st.nextToken()); // 열
        M = Integer.parseInt(st.nextToken()); // 상어 수
        fishingSpot = new Shark[R + 1][C + 1];

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int z = Integer.parseInt(st.nextToken());

            fishingSpot[r][c] = new Shark(r, c, s, d, z);
        }

        System.out.println(fishing());
    }

    public static int fishing() {
        int totalSize = 0;

        for (int i = 1; i <= C; i++) { // 1. 낚시 (왼쪽에서 오른쪽으로 이동)
            for (int j = 1; j <= R; j++) {
                if (fishingSpot[j][i] != null) {
                    totalSize += fishingSpot[j][i].size;
                    fishingSpot[j][i] = null;
                    break;
                }
            }
            moveAllSharks(); // 2. 모든 상어 이동
        }

        return totalSize;
    }

    static void moveAllSharks() {
        Shark[][] newFishingSpot = new Shark[R + 1][C + 1];

        for (int r = 1; r <= R; r++) {
            for (int c = 1; c <= C; c++) {
                if (fishingSpot[r][c] != null) {
                    Shark shark = fishingSpot[r][c];
                    shark.move();

                    // 같은 위치에 상어가 있다면 크기 비교 후 큰 상어만 저장
                    if (newFishingSpot[shark.r][shark.c] == null ||
                            newFishingSpot[shark.r][shark.c].size < shark.size) {
                        newFishingSpot[shark.r][shark.c] = shark;
                    }
                }
            }
        }

        fishingSpot = newFishingSpot;
    }

    static class Shark {
        int r, c; // 위치
        int speed, direction, size; // 속도, 방향, 크기

        public Shark(int r, int c, int speed, int direction, int size) {
            this.r = r;
            this.c = c;
            this.speed = speed;
            this.direction = direction;
            this.size = size;
        }

        public void move() {
            int[][] directions = {{0, 0}, {-1, 0}, {1, 0}, {0, 1}, {0, -1}}; // 1: 위, 2: 아래, 3: 오른쪽, 4: 왼쪽

            int moveDistance = speed;
            // 주기 계산 (다시 제자리에 위치하는 주기)
            if (direction == 1 || direction == 2) { // 위, 아래 이동 최적화
                moveDistance %= (R - 1) * 2;
            } else { // 왼쪽, 오른쪽 이동 최적화
                moveDistance %= (C - 1) * 2;
            }

            for (int i = 0; i < moveDistance; i++) {
                int nr = r + directions[direction][0];
                int nc = c + directions[direction][1];

                if (nr < 1 || nr > R || nc < 1 || nc > C) { // 벽에 부딪히면 방향 변경
                    direction = (direction % 2 == 1) ? direction + 1 : direction - 1;
                    nr = r + directions[direction][0];
                    nc = c + directions[direction][1];
                }

                r = nr;
                c = nc;
            }
        }
    }
}
