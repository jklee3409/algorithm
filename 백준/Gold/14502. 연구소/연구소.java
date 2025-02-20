import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static int maxSafeArea = 0;
    static int[][] map;
    static List<Point> emptyList = new ArrayList<>();

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static class Point {
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 행
        M = Integer.parseInt(st.nextToken()); // 열
        map = new int[N][M];

        // 지도 입력
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());

                // 벽을 세울 수 있는 공간 저장
                if (map[i][j] == 0) emptyList.add(new Point(i, j));
            }
        }

        comb(0, 0, new Point[3]);
        System.out.println(maxSafeArea);
    }

    public static void comb(int start, int depth, Point[] selected) {
        // 3개의 벽 위치를 구하면 안전 지대 개수 계산
        if (depth == 3) {
            int[][] temp = copy(); // map 을 임시 배열에 카피
            // 임시 배열에 구한 조합에 따라 벽을 세움
            for (Point point : selected) {
                temp[point.y][point.x] = 1;
            }
            maxSafeArea = Math.max(maxSafeArea, getSafeArea(temp));
            return;
        }

        for (int i = start; i < emptyList.size(); i++) {
            selected[depth] = emptyList.get(i);
            comb(i + 1, depth + 1, selected);
        }
    }

    // 3개의 벽을 세운 뒤 안전 지대 개수 계산
    public static int getSafeArea(int[][] temp) {
        Queue<Point> queue = new ArrayDeque<>();

        // 바이러스 위치 큐에 저장
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (temp[i][j] == 2) {
                    queue.offer(new Point(i, j));
                }
            }
        }

        // 바이러스 확산
        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                // 범위를 벗어나거나 벽인 경우 skip
                if (ny < 0 || nx < 0 || ny >= N || nx >= M || temp[ny][nx] != 0) continue;

                temp[ny][nx] = 2;
                queue.offer(new Point(ny, nx));
            }
        }

        int safeArea = 0;

        // 안전 지대 계산
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (temp[i][j] == 0) safeArea++;
            }
        }

        return safeArea;
    }

    // 2차원 배열 복사
    static int[][] copy() {
        int[][] newMap = new int[N][M];
        for (int i = 0; i < N; i++) {
            newMap[i] = Arrays.copyOf(map[i], M);
        }

        return newMap;
    }
}
