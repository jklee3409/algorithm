import java.io.*;
import java.util.*;

public class Main {
    static int N, M, cctvCnt = 0, MIN = Integer.MAX_VALUE;
    static Point[][] room;
    static ArrayList<CCTV> cctvList = new ArrayList<>();

    static class Point {
        int value, dir;

        public Point(int value, int dir) {
            this.value = value;
            this.dir = dir;
        }
    }

    static class CCTV {
        int y, x, type;

        public CCTV(int y, int x, int type) {
            this.y = y;
            this.x = x;
            this.type = type;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        room = new Point[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int cur = Integer.parseInt(st.nextToken());

                if (1 <= cur && cur <= 5) {
                    room[i][j] = new Point(cur, 1);
                    cctvList.add(new CCTV(i, j, cur));
                    cctvCnt++;
                } else {
                    room[i][j] = new Point(cur, 0);
                }
            }
        }

        simulate(0);
        System.out.println(MIN);
    }

    public static void simulate(int depth) {
        if (depth == cctvCnt) {
            Point[][] temp = deepCopy();
            MIN = Math.min(MIN, getBlindSpot(temp));
            return;
        }

        CCTV cctv = cctvList.get(depth);
        int y = cctv.y;
        int x = cctv.x;
        int type = cctv.type;
        
        int rotations = 4;
        if (type == 2) rotations = 2;  // 2번 CCTV는 2가지 방향만 고려
        if (type == 5) rotations = 1;  // 5번 CCTV는 1가지 방향만 고려

        for (int dir = 1; dir <= rotations; dir++) {
            room[y][x].dir = dir;
            simulate(depth + 1);
        }
    }

    public static int getBlindSpot(Point[][] temp) {

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                int value = temp[y][x].value;

                if (value < 1 || value > 5) continue;

                int dir = temp[y][x].dir;

                if (value == 1) {

                    if (dir == 1) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else if (dir == 2) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else if (dir == 3) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }
                    }

                } else if (value == 2) {

                    if (dir == 1 || dir == 3) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    }
                } else if (value == 3) {

                    if (dir == 1) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else if (dir == 2) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else if (dir == 3) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }
                    }

                } else if (value == 4){

                    if (dir == 1) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else if (dir == 2) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else if (dir == 3) {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            nx++;

                            if (nx >= M) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                    } else {

                        int ny = y;
                        int nx = x;
                        while (true) {
                            ny++;

                            if (ny >= N) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            nx--;

                            if (nx < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }

                        ny = y;
                        nx = x;
                        while (true) {
                            ny--;

                            if (ny < 0) break;
                            if (temp[ny][nx].value == 6) break;
                            if (temp[ny][nx].value == 0)
                                temp[ny][nx].value = -1;
                        }
                    }

                } else { // value == 5

                    int ny = y;
                    int nx = x;
                    while (true) {
                        ny++;

                        if (ny >= N) break;
                        if (temp[ny][nx].value == 6) break;
                        if (temp[ny][nx].value == 0)
                            temp[ny][nx].value = -1;
                    }

                    ny = y;
                    nx = x;
                    while (true) {
                        nx++;

                        if (nx >= M) break;
                        if (temp[ny][nx].value == 6) break;
                        if (temp[ny][nx].value == 0)
                            temp[ny][nx].value = -1;
                    }

                    ny = y;
                    nx = x;
                    while (true) {
                        ny--;

                        if (ny < 0) break;
                        if (temp[ny][nx].value == 6) break;
                        if (temp[ny][nx].value == 0)
                            temp[ny][nx].value = -1;
                    }

                    ny = y;
                    nx = x;
                    while (true) {
                        nx--;

                        if (nx < 0) break;
                        if (temp[ny][nx].value == 6) break;
                        if (temp[ny][nx].value == 0)
                            temp[ny][nx].value = -1;
                    }
                }
            }
        }

        int cnt = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (temp[i][j].value == 0) cnt++;
            }
        }

        return cnt;
    }

    public static Point[][] deepCopy() {
        Point[][] temp = new Point[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                temp[i][j] = new Point(room[i][j].value, room[i][j].dir);
            }
        }
        return temp;
    }
}