import java.io.*;
import java.util.*;

public class Main {
    static int N, K, L;
    static int TIME = 0;
    static int[][] board;
    static List<MoveInfo> moveInfoList = new ArrayList<>();

    static class Point {
        int y, x;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    static class MoveInfo {
        int x;
        char dir;

        public MoveInfo(int x, char dir) {
            this.x = x;
            this.dir = dir;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        K = Integer.parseInt(br.readLine());
        board = new int[N + 1][N + 1];

        for (int i = 0; i < K; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());

            board[y][x] = 1; // 사과
        }

        L = Integer.parseInt(br.readLine());
        for (int i = 0; i < L; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            char dir = st.nextToken().charAt(0);

            moveInfoList.add(new MoveInfo(x, dir));
        }

        move();

        System.out.println(TIME);
    }

    // 뱀의 위치 좌표는 큐에 넣고 관리
    //  이동한 곳에 사과가 없으면 큐 맨 앞의 좌표를 꺼내고, board 에서 해당 좌표를 0 처리
    // 뱀이 있는 곳은 -1 로 기록

    // move (void)
    // 입력: List<MoveInfo>
    // 동작: 뱀의 이동 진행 및 시간 업데이트
    //      1. 뱀의 좌표를 기록할 Queue 생성
    //      2. 현재 방향에 따라 이동 진행
    //      3. 새로운 좌표에 대해 check 호출
    //      4. update 호출

    // check (boolean)
    // 입력: y, x 좌표
    // 동작: 이동한 위치가 벽 or 뱀 or 사과인지 체크
    // 반환: 벽 or 뱀이라면 false 반환
    // 반환: 사과 또는 빈 공간이라면 true 반환

    // update (void)
    // 입력: queue, y,x 좌표
    // 동작: 뱀의 꼬리 상태 업데이트
    //      board[y][x] == 0 이라면, poll 후 board[poll.y][poll.x] = 0

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void move() {
        Deque<Point> points = new ArrayDeque<>();
        points.add(new Point(1, 1));
        board[1][1] = -1;

        int dir = 3; // 처음에는 오른쪽으로 직진
        for (MoveInfo moveInfo : moveInfoList) {

            while (TIME < moveInfo.x) {
                int ny = points.getLast().y + dy[dir];
                int nx = points.getLast().x + dx[dir];

                if (!check(ny, nx)) {
                    TIME++;
                    return;
                }

                points.add(new Point(ny, nx));
                update(points, ny, nx);
                board[ny][nx] = -1;

                TIME++;
            }

            dir = updateDir(dir, moveInfo.dir);
        }

        while (true) {
            int ny = points.getLast().y + dy[dir];
            int nx = points.getLast().x + dx[dir];

            if (!check(ny, nx)) {
                TIME++;
                return;
            }

            points.add(new Point(ny, nx));
            update(points, ny, nx);
            board[ny][nx] = -1;

            TIME++;
        }
    }

    static int updateDir(int curDir, char changeDir) {
        if (changeDir == 'L') {
            if (curDir == 0) return 2;
            else if (curDir == 1) return 3;
            else if (curDir == 2) return 1;
            else return 0;
        } else {
            if (curDir == 0) return 3;
            else if (curDir == 1) return 2;
            else if (curDir == 2) return 0;
            else return 1;
        }
    }

    static boolean check(int y, int x) {
        if (y < 1 || x < 1 || y > N || x > N) return false;
        if (board[y][x] == -1) return false;

        return true;
    }

    static void update(Queue<Point> points, int y, int x) {
        if (board[y][x] == 0) {
            Point first = points.poll();
            board[first.y][first.x] = 0;
        }
    }
}