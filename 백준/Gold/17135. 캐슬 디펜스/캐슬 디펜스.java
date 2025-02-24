import java.io.*;
import java.util.*;

public class Main {
    static int N, M, D, maxKill = 0;
    static int[][] board, tempBoard;

    static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        // 동일한 적은 중복 count 하지 않기 위해 
        @Override
        public boolean equals(Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());
        board = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        combination(new int[3], 0, 0);
        System.out.println(maxKill);
    }

    // 궁수 위치 조합 생성
    static void combination(int[] positions, int count, int start) {
        if (count == 3) {
            simulate(positions);
            return;
        }

        for (int i = start; i < M; i++) {
            positions[count] = i;
            combination(positions, count + 1, i + 1);
        }
    }

    // 게임 시뮬레이션
    static void simulate(int[] positions) {
        tempBoard = new int[N][M];
        for (int i = 0; i < N; i++) {
            tempBoard[i] = board[i].clone();
        }

        int killCount = 0;

        for (int turn = 0; turn < N; turn++) {
            killCount += attack(positions);
            moveEnemies();
        }

        maxKill = Math.max(maxKill, killCount);
    }

    // 적 공격
    static int attack(int[] positions) {
        Set<Point> targets = new HashSet<>();

        for (int archer : positions) {
            Point target = findTarget(archer);

            if (target != null) {
                targets.add(target);
            }
        }

        // 동시에 적 제거
        for (Point p : targets) {
            tempBoard[p.x][p.y] = 0;
        }

        return targets.size();
    }

    // 가장 가까운 적 찾기
    static Point findTarget(int archerCol) {
        Queue<Point> queue = new LinkedList<>();
        boolean[][] visited = new boolean[N][M];

        // 성 바로 위 칸부터 탐색 시작
        int startX = N - 1;
        queue.add(new Point(startX, archerCol));
        visited[startX][archerCol] = true;

        // 시작 칸에 적이 있다면 바로 반환
        if (tempBoard[startX][archerCol] == 1) {
            return new Point(startX, archerCol);
        }

        // 탐색 순서는 왼쪽, 위, 오른쪽
        int[] dx = {0, -1, 0};
        int[] dy = {-1, 0, 1};
        int distance = 1; // 시작 칸까지의 거리는 1

        // BFS 수행
        while (!queue.isEmpty() && distance < D) {
            int size = queue.size();
            distance++;

            for (int i = 0; i < size; i++) {
                Point p = queue.poll();

                for (int dir = 0; dir < 3; dir++) {
                    int nx = p.x + dx[dir];
                    int ny = p.y + dy[dir];

                    if (nx >= 0 && ny >= 0 && nx < N && ny < M && !visited[nx][ny]) {
                        visited[nx][ny] = true;

                        if (tempBoard[nx][ny] == 1) {
                            return new Point(nx, ny);
                        }

                        queue.add(new Point(nx, ny));
                    }
                }
            }
        }
        return null;
    }

    // 적 이동
    static void moveEnemies() {
        for (int i = N - 1; i > 0; i--) {
            tempBoard[i] = tempBoard[i - 1].clone();
        }

        Arrays.fill(tempBoard[0], 0);
    }
}
