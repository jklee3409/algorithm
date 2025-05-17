import java.io.*;
import java.util.*;

public class Main {
    static int N, L, R;
    static int[][] map;
    static boolean[][] visited;

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static class Point {
        int y, x;

        Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        L = Integer.parseInt(st.nextToken());
        R = Integer.parseInt(st.nextToken());

        map = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int day = 0;

        while (true) {
            visited = new boolean[N][N];
            boolean moved = false;

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (!visited[i][j]) {
                        List<Point> union = new ArrayList<>();
                        int populationSum = bfs(i, j, union);

                        if (union.size() > 1) {
                            redistribute(union, populationSum);
                            moved = true;
                        }
                    }
                }
            }

            if (!moved) break;
            day++;
        }

        System.out.println(day);
    }

    // 연합을 구성하고 인구 총합 반환
    static int bfs(int y, int x, List<Point> union) {
        Queue<Point> queue = new ArrayDeque<>();
        queue.offer(new Point(y, x));
        visited[y][x] = true;
        union.add(new Point(y, x));

        int sum = map[y][x];

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int d = 0; d < 4; d++) {
                int ny = p.y + dy[d];
                int nx = p.x + dx[d];

                if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                if (visited[ny][nx]) continue;

                int diff = Math.abs(map[p.y][p.x] - map[ny][nx]);

                if (diff >= L && diff <= R) {
                    visited[ny][nx] = true;
                    queue.offer(new Point(ny, nx));

                    union.add(new Point(ny, nx));
                    sum += map[ny][nx];
                }
            }
        }

        return sum;
    }

    // 연합 내 인구 재분배
    static void redistribute(List<Point> union, int totalPopulation) {
        int newPopulation = totalPopulation / union.size();
        for (Point p : union) {
            map[p.y][p.x] = newPopulation;
        }
    }
}
