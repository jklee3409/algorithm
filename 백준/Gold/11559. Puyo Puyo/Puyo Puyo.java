import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Main {
    static int cascadeCnt = 0;
    static char[][] field = new char[12][6];

    static class Point {
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;

        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 12; i++) {
            String str = br.readLine();
            for (int j = 0; j < 6; j++) {
                field[i][j] = str.charAt(j);
            }
        }

        simulate();
        System.out.println(cascadeCnt);
    }

    public static void simulate() {

        while (true) {
            boolean cascaded = false;
            boolean[][] visited = new boolean[12][6];

            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 6; j++) {
                    if (visited[i][j] || field[i][j] == '.') continue;

                    boolean temp = bfs(i, j, visited, field[i][j]);

                    cascaded = cascaded || temp;
                }
            }

            if (!cascaded) return;
            else {
                cascadeCnt++;
                move();
            }
        }
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    // bfs 탐색 후 빈 공간으로 변환
    public static boolean bfs(int y, int x, boolean[][] visited, char color) {
        Queue<Point> queue = new ArrayDeque<>();

        queue.offer(new Point(y, x));
        visited[y][x] = true;

        int step = 1;
        List<Point> union = new ArrayList<>();
        union.add(new Point(y, x));

        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = p.y + dy[i];
                int nx = p.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= 12 || nx >= 6) continue;
                if (visited[ny][nx] || field[ny][nx] != color) continue;

                queue.offer(new Point(ny, nx));
                union.add(new Point(ny, nx));
                visited[ny][nx] = true;
                step++;
            }
        }

        // 4칸 이상 연결된 경우에만 연쇄
        if (step >= 4) {
            for (Point p : union) field[p.y][p.x] = '.';

            return true;
        }

        return false;
    }

    public static void move() {

        for (int i = 0; i < 6; i++) {
            int bottom = -1;
            for (int j = 11; j >= 0; j--) {

                if (field[j][i] == '.' && bottom == -1) bottom = j; // 최하단의 빈 공간
                else if (field[j][i] != '.' && bottom != -1) { // 뿌요 && 내려갈 공간이 있으면
                    field[bottom][i] = field[j][i];
                    field[j][i] = '.';
                    bottom--;
                }
            }
        }
    }
}