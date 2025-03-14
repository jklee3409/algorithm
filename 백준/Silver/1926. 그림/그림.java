import java.io.*;
import java.util.*;

public class Main {
    static int n, m;
    static int[][] paper;
    static int pictureCnt = 0;
    static int maxExtent = 0;

    static class Node {
        private int y, x;

        public Node(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        paper = new int[n][m];

        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < m; j++) {
                paper[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        boolean[][] visited = new boolean[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                // 색칠된 부분 && 방문 x, bfs 시작
                if (paper[i][j] == 1 && !visited[i][j]) {
                    pictureCnt++;
                    maxExtent = Math.max(maxExtent, bfs(i, j, visited));
                }
            }
        }

        System.out.println(pictureCnt);
        System.out.println(maxExtent);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    // 현재 그림의 넓이 반환
    public static int bfs(int startY, int startX, boolean[][] visited) {
        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(new Node(startY, startX));
        visited[startY][startX] = true;

        int extent = 0;

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            extent++;

            for (int i = 0; i < 4; i++) {
                int ny = node.y + dy[i];
                int nx = node.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= n || nx >= m) continue;
                if (visited[ny][nx] || paper[ny][nx] == 0) continue;

                queue.offer(new Node(ny, nx));
                visited[ny][nx] = true;
            }
        }

        return extent;
    }
}
