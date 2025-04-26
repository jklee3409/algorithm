import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;

public class Main {
    static int COUNT = 0;
    static char[][] classRoom = new char[5][5];
    static boolean[][] mark = new boolean[5][5];
    static Point[] comb = new Point[7];

    static class Point {
        int r, c;

        public Point(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 0; i < 5; i++) {
            String str = br.readLine();
            for (int j = 0; j < 5; j++) {
                classRoom[i][j] = str.charAt(j);
            }
        }

        solution(0, 0, 0);
        System.out.println(COUNT);
    }

    public static void solution(int depth, int start, int sCount) {
        if (depth == 7) {
            if (sCount >= 4 && isConnected()) COUNT++;
            return;
        }

        for (int i = start; i < 25; i++) {
            int r = i / 5;
            int c = i % 5;
            comb[depth] = new Point(r, c);

            mark[r][c] = true;
            if (classRoom[r][c] == 'S') sCount++;

            solution(depth + 1, i + 1, sCount);

            if (classRoom[r][c] == 'S') sCount--;
            mark[r][c] = false;
        }
    }

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    public static boolean isConnected() {
        Queue<Point> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[5][5];

        queue.add(comb[0]);
        visited[comb[0].r][comb[0].c] = true;

        int visitCount = 1;
        while (!queue.isEmpty()) {
            Point p = queue.poll();

            for (int i = 0; i < 4; i++) {
                int nr = p.r + dr[i];
                int nc = p.c + dc[i];

                if (nr < 0 || nc < 0 || nr >= 5 || nc >= 5) continue;
                if (visited[nr][nc] || !mark[nr][nc]) continue;

                queue.offer(new Point(nr, nc));
                visited[nr][nc] = true;
                visitCount++;
            }
        }

        return visitCount == 7;
    }
}
