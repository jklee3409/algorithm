import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static int[][] fishTank;
    static int[] fishes = new int[7]; // 1~6 크기 물고기 개수 저장
    static int[] dy = {-1, 1, 0, 0}; // 상, 하, 좌, 우
    static int[] dx = {0, 0, -1, 1};

    static class BabyShark {
        int y, x, size, eatCount, time;

        BabyShark(int y, int x) {
            this.y = y;
            this.x = x;
            this.size = 2;
            this.eatCount = 0;
            this.time = 0;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        fishTank = new int[N][N];

        BabyShark babyShark = null;

        // 입력 처리 및 초기 상태 저장
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int fish = Integer.parseInt(st.nextToken());
                fishTank[i][j] = fish;

                if (fish == 9) {
                    babyShark = new BabyShark(i, j);
                    fishTank[i][j] = 0; // 아기 상어의 위치는 빈칸으로 변경
                } else if (fish > 0) {
                    fishes[fish]++;
                }
            }
        }

        // BFS를 통한 물고기 탐색 및 이동
        System.out.println(simulateShark(babyShark));
    }

     // 아기 상어가 물고기를 먹을 때까지 반복 실행
    public static int simulateShark(BabyShark babyShark) {
        int totalTime = 0;

        while (true) {
            int[] nextFish = findNearestFish(babyShark);
            if (nextFish == null) break; // 먹을 물고기가 없으면 종료

            // 물고기 먹기 & 이동
            eatFish(babyShark, nextFish);

            // 이동한 거리만큼 총 시간 증가
            totalTime += nextFish[2];
        }

        return totalTime;
    }


     // BFS를 사용하여 먹을 수 있는 가장 가까운 물고기를 찾음
     // 반환 : {물고기 y 좌표, 물고기 x 좌표, 거리}
    public static int[] findNearestFish(BabyShark babyShark) {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> {
            if (a[2] != b[2]) return Integer.compare(a[2], b[2]); // 거리 우선
            if (a[0] != b[0]) return Integer.compare(a[0], b[0]); // 위쪽 우선
            return Integer.compare(a[1], b[1]); // 왼쪽 우선
        });

        boolean[][] visited = new boolean[N][N];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{babyShark.y, babyShark.x, 0}); // {y, x, 거리}
        visited[babyShark.y][babyShark.x] = true;

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int y = current[0], x = current[1], dist = current[2];

            for (int i = 0; i < 4; i++) {
                int ny = y + dy[i];
                int nx = x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N || visited[ny][nx]) continue;

                int fish = fishTank[ny][nx];
                if (fish > babyShark.size) continue; // 상어보다 큰 물고기는 이동 불가

                visited[ny][nx] = true;
                queue.offer(new int[]{ny, nx, dist + 1});

                if (fish > 0 && fish < babyShark.size) { // 먹을 수 있는 물고기 발견
                    pq.offer(new int[]{ny, nx, dist + 1});
                }
            }
        }

        return pq.isEmpty() ? null : pq.poll(); // 먹을 물고기가 없으면 null 반환
    }


     // 아기 상어가 물고기를 먹고 상태 업데이트
    public static void eatFish(BabyShark babyShark, int[] targetFish) {
        int fishY = targetFish[0], fishX = targetFish[1], fishDist = targetFish[2];

        // 상어 위치 이동 및 물고기 먹기
        babyShark.y = fishY;
        babyShark.x = fishX;
        fishTank[fishY][fishX] = 0;
        babyShark.eatCount++;

        // 물고기 개수 감소
        fishes[fishTank[fishY][fishX]]--;

        // 상어 크기 증가 체크
        if (babyShark.eatCount == babyShark.size) {
            babyShark.size++;
            babyShark.eatCount = 0;
        }
    }
}
