import java.io.*;
import java.util.*;

public class Main {
    static int R, C;
    static char[][] map;
    static int[][] fireTime;

    static class Fire {
        int y, x, time;
        
        public Fire(int y, int x, int time) {
            this.y = y;
            this.x = x;
            this.time = time;
        }
    }

    static class Node {
        int y, x, time;
        
        public Node(int y, int x, int time) {
            this.y = y;
            this.x = x;
            this.time = time;
        }
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        map = new char[R][C];
        fireTime = new int[R][C];

        List<int[]> fires = new ArrayList<>();
        int startJY = 0, startJX = 0;

        for (int i = 0; i < R; i++) {
            String input = br.readLine();
            for (int j = 0; j < C; j++) {
                char c = input.charAt(j);
                map[i][j] = c;
                fireTime[i][j] = -1;

                if (c == 'J') {
                    startJY = i;
                    startJX = j;
                } else if (c == 'F') {
                    fires.add(new int[]{i, j});
                }
            }
        }

        setFireTime(fires);
        int result = escape(startJY, startJX);

        System.out.println(result == -1 ? "IMPOSSIBLE" : result);
    }

    private static void setFireTime(List<int[]> fires) {
        Queue<Fire> queue = new ArrayDeque<>();

        for (int[] f : fires) {
            queue.offer(new Fire(f[0], f[1], 0));
            fireTime[f[0]][f[1]] = 0;
        }

        while (!queue.isEmpty()) {
            Fire f = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = f.y + dy[i];
                int nx = f.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= R || nx >= C) continue;
                if (map[ny][nx] == '#') continue;
                if (fireTime[ny][nx] != -1) continue;

                fireTime[ny][nx] = f.time + 1;
                queue.offer(new Fire(ny, nx, f.time + 1));
            }
        }
    }

    private static int escape(int startY, int startX) {
        Queue<Node> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[R][C];

        queue.offer(new Node(startY, startX, 0));
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            Node n = queue.poll();
            
            if (n.y == 0 || n.y == R - 1 || n.x == 0 || n.x == C - 1) return n.time + 1;

            for (int i = 0; i < 4; i++) {
                int ny = n.y + dy[i];
                int nx = n.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= R || nx >= C) continue;
                if (visited[ny][nx] || map[ny][nx] == '#') continue;

                int nextTime = n.time + 1;
                
                if (fireTime[ny][nx] != -1 && fireTime[ny][nx] <= nextTime) continue;

                visited[ny][nx] = true;
                queue.offer(new Node(ny, nx, nextTime));
            }
        }

        return -1;
    }
}