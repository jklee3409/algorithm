import java.io.*;
import java.util.*;

public class Main {
    // 6면, 3행, 3열
    // 면 인덱스: U(0), D(1), F(2), B(3), L(4), R(5)
    static char[][][] cube = new char[6][3][3];
    static final int U = 0, D = 1, F = 2, B = 3, L = 4, R = 5;
    // 초기 색상: 위-흰, 아래-노, 앞-빨, 뒤-오, 왼-초, 오-파
    static char[] colors = {'w', 'y', 'r', 'o', 'g', 'b'};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            initCube();
            int n = Integer.parseInt(br.readLine());
            StringTokenizer st = new StringTokenizer(br.readLine());
            while (n-- > 0) {
                String cmd = st.nextToken();
                int face = getFace(cmd.charAt(0));
                char dir = cmd.charAt(1);
                rotate(face, dir);
            }
            printTop();
        }
    }

    // 큐브 초기화
    static void initCube() {
        for (int i = 0; i < 6; i++) {
            for (int r = 0; r < 3; r++) {
                Arrays.fill(cube[i][r], colors[i]);
            }
        }
    }
    
    static int getFace(char c) {
        switch (c) {
            case 'U': return U;
            case 'D': return D;
            case 'F': return F;
            case 'B': return B;
            case 'L': return L;
            case 'R': return R;
        }
        return -1;
    }

    // 회전 처리
    static void rotate(int face, char dir) {
        // 반시계(-)인 경우 시계방향(+) 3번과 동일
        int times = (dir == '+') ? 1 : 3;
        for (int k = 0; k < times; k++) {
            rotateSelf(face);
            rotateSides(face);
        }
    }

    // 해당 면 자체를 시계방향으로 90도 회전
    static void rotateSelf(int f) {
        char[][] temp = new char[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                temp[j][2 - i] = cube[f][i][j];
            }
        }
        for (int i = 0; i < 3; i++) {
            System.arraycopy(temp[i], 0, cube[f][i], 0, 3);
        }
    }

    // 옆면들을 시계방향으로 회전
    static void rotateSides(int f) {
        char[] temp = new char[3];
        switch (f) {
            case U:
                // F -> L -> B -> R -> F (Top Rows)
                System.arraycopy(cube[F][0], 0, temp, 0, 3);
                System.arraycopy(cube[R][0], 0, cube[F][0], 0, 3);
                System.arraycopy(cube[B][0], 0, cube[R][0], 0, 3);
                System.arraycopy(cube[L][0], 0, cube[B][0], 0, 3);
                System.arraycopy(temp, 0, cube[L][0], 0, 3);
                break;
            case D:
                // F -> R -> B -> L -> F (Bottom Rows)
                System.arraycopy(cube[F][2], 0, temp, 0, 3);
                System.arraycopy(cube[L][2], 0, cube[F][2], 0, 3);
                System.arraycopy(cube[B][2], 0, cube[L][2], 0, 3);
                System.arraycopy(cube[R][2], 0, cube[B][2], 0, 3);
                System.arraycopy(temp, 0, cube[R][2], 0, 3);
                break;
            case F:
                // U(row2) -> R(col0) -> D(row0) -> L(col2) -> U(row2)
                System.arraycopy(cube[U][2], 0, temp, 0, 3);
                for (int i = 0; i < 3; i++) cube[U][2][i] = cube[L][2 - i][2];
                for (int i = 0; i < 3; i++) cube[L][i][2] = cube[D][0][i];
                for (int i = 0; i < 3; i++) cube[D][0][i] = cube[R][2 - i][0];
                for (int i = 0; i < 3; i++) cube[R][i][0] = temp[i];
                break;
            case B:
                // U(row0) -> L(col0) -> D(row2) -> R(col2) -> U(row0)
                System.arraycopy(cube[U][0], 0, temp, 0, 3);
                for (int i = 0; i < 3; i++) cube[U][0][i] = cube[R][i][2];
                for (int i = 0; i < 3; i++) cube[R][i][2] = cube[D][2][2 - i];
                for (int i = 0; i < 3; i++) cube[D][2][i] = cube[L][i][0];
                for (int i = 0; i < 3; i++) cube[L][i][0] = temp[2 - i];
                break;
            case L:
                // U(col0) -> F(col0) -> D(col0) -> B(col2) -> U(col0)
                for (int i = 0; i < 3; i++) temp[i] = cube[U][i][0];
                for (int i = 0; i < 3; i++) cube[U][i][0] = cube[B][2 - i][2];
                for (int i = 0; i < 3; i++) cube[B][i][2] = cube[D][2 - i][0];
                for (int i = 0; i < 3; i++) cube[D][i][0] = cube[F][i][0];
                for (int i = 0; i < 3; i++) cube[F][i][0] = temp[i];
                break;
            case R:
                // U(col2) -> B(col0) -> D(col2) -> F(col2) -> U(col2)
                for (int i = 0; i < 3; i++) temp[i] = cube[U][i][2];
                for (int i = 0; i < 3; i++) cube[U][i][2] = cube[F][i][2];
                for (int i = 0; i < 3; i++) cube[F][i][2] = cube[D][i][2];
                for (int i = 0; i < 3; i++) cube[D][i][2] = cube[B][2 - i][0];
                for (int i = 0; i < 3; i++) cube[B][i][0] = temp[2 - i];
                break;
        }
    }

    static void printTop() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                sb.append(cube[U][i][j]);
            }
            sb.append('\n');
        }
        System.out.print(sb);
    }
}