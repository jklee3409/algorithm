import java.io.*;
import java.util.*;

public class Main {
    static int K;
    static final int RIGHT_SIDE = 3, LEFT_SIDE = 7;
    static int[][] rotation;
    static int[][] cogwheel = new int[5][9];

    static class Status {
        int num, dir;

        public Status(int num, int dir) {
            this.num = num;
            this.dir = dir;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        for (int i = 1; i <= 4; i++) {
            String input = br.readLine();
            for (int j = 1; j <= 8; j++) {
                int status = input.charAt(j - 1) - '0';
                cogwheel[i][j] = status;
            }
        }

        K = Integer.parseInt(br.readLine());
        rotation = new int[K][2];
        for (int i = 0; i < K; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            rotation[i][0] = Integer.parseInt(st.nextToken()); // 회전시킬 톱니바퀴 번호
            rotation[i][1] = Integer.parseInt(st.nextToken()); // 회전시킬 방향, 1: 시계, -1: 반시계
        }

        System.out.println(simulate());
    }

    // 전체를 시뮬레이션하는 함수
    //  1. rotation 사이즈만큼 반복문 횟수 정의
    //      1-1. check 호출
    //      1-2. rotate 진행
    //  2. 전체 회전 종료 후 점수 계산 (calculate 호출)

    // check (회전 여부를 판단하는 함수)
    //  입력값: 가장 처음 회전하는 톱니바퀴의 번호와 방향 입력
    //  리턴값: 전체 회전시켜야 하는 톱니바퀴의 번호와 방향 리턴 (1 ~ 4번 톰니바퀴의 맞닿은 부분을 확인)

    // rotate (회전 시키는 함수)
    //  입력값: 회전시켜야 하는 톱니바퀴의 번호와 방향
    //  동작: 해당 톱니바퀴를 회전 (배열에서 로테이션)

    // calculate (점수 계산하는 함수)
    //  동작: 1 ~ 4번 톱니바퀴를 정해진 규칙에 따라 점수 계산

    // cogwheel
    // 연결 방향: 1,3 - 2,7 / 2,3 - 3,7 / 3,3 - 4,7
    // index: 1 2 3 4 5 6 7 8 (1번이 12시 방향)
    // right rotate(시계): 8 1 2 3 4 5 6 7
    // left rotate(반시계): 2 3 4 5 6 7 8 1

    static int simulate() {
        for (int i = 0; i < K; i++) {
            List<Status> statuses = check(rotation[i][0], rotation[i][1]);

            for (Status status : statuses) {
                rotate(status.num, status.dir);
            }
        }

        return calculate();
    }

    static List<Status> check(int initNum, int initDir) {
        List<Status> result = new ArrayList<>();
        result.add(new Status(initNum, initDir));

        int left = initNum - 1;
        int right = initNum;
        int dir = initDir;

        while (left >= 1) {
            if (cogwheel[left][RIGHT_SIDE] != cogwheel[right][LEFT_SIDE]) {
                result.add(new Status(left, -dir));
            } else break;

            dir = -dir;
            right--;
            left--;
        }

        left = initNum;
        right = initNum + 1;
        dir = initDir;

        while (right <= 4) {
            if (cogwheel[right][LEFT_SIDE] != cogwheel[left][RIGHT_SIDE]) {
                result.add(new Status(right, -dir));
            } else break;

            dir = -dir;
            right++;
            left++;
        }

        return result;
    }

    static void rotate(int num, int dir) {
        if (dir == 1) { // 시계
            int last = cogwheel[num][8];
            for (int i = 8; i > 1; i--) {
                cogwheel[num][i] = cogwheel[num][i - 1];
            }
            cogwheel[num][1] = last;
        }

        if (dir == -1) { // 반시계
            int first = cogwheel[num][1];
            for (int i = 1; i < 8; i++) {
                cogwheel[num][i] = cogwheel[num][i + 1];
            }
            cogwheel[num][8] = first;
        }
    }

    static int calculate() {
        int result = 0;

        for (int i = 1; i <= 4; i++) {
            if (cogwheel[i][1] == 1 && i == 1) result += 1;
            else if (cogwheel[i][1] == 1 && i == 2) result += 2;
            else if (cogwheel[i][1] == 1 && i == 3) result += 4;
            else if (cogwheel[i][1] == 1) result += 8;
        }

        return result;
    }
}