import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int[][] board;

    static int maxBlack = 0;
    static int maxWhite = 0;

    static boolean[] diag1, diag2;

    // ▶ 색깔별 가능한 위치 리스트
    static List<int[]> blacks = new ArrayList<>();
    static List<int[]> whites = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        board = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
                // ▶ 분할: (i + j) % 2 == 0 은 검은 칸, 1 은 흰 칸
                if (board[i][j] == 1) {
                    if ((i + j) % 2 == 0) blacks.add(new int[]{i, j});
                    else                whites.add(new int[]{i, j});
                }
            }
        }

        diag1 = new boolean[2 * N];
        diag2 = new boolean[2 * N];

        // ▶ 검은 칸만 놓아보기
        dfs(blacks, 0, 0, true);
        // ▶ 배열 초기화 후, 흰 칸
        for (int i = 0; i < diag1.length; i++) diag1[i] = diag2[i] = false;
        dfs(whites, 0, 0, false);

        // ▶ 최종 합산
        System.out.println(maxBlack + maxWhite);
    }

    /**
     * @param list    : 검은 칸 혹은 흰 칸 리스트
     * @param idx     : 현재 탐색 중인 리스트 인덱스
     * @param count   : 지금까지 놓은 비숍 개수
     * @param isBlack : true→검은 칸, false→흰 칸
     */
    public static void dfs(List<int[]> list, int idx, int count, boolean isBlack) {
        // ▶ 남은 칸까지 최대치를 안 넘으면 중단
        int remain = list.size() - idx;
        if (isBlack) {
            if (count + remain <= maxBlack) return;
        } else {
            if (count + remain <= maxWhite) return;
        }

        // 기저 조건
        if (idx == list.size()) {
            if (isBlack) maxBlack = Math.max(maxBlack, count);
            else         maxWhite = Math.max(maxWhite, count);
            return;
        }

        int r = list.get(idx)[0];
        int c = list.get(idx)[1];
        int d1 = r + c;
        int d2 = r - c + (N - 1);

        // ▶ 1) 이 칸에 놓는 경우
        if (!diag1[d1] && !diag2[d2]) {
            diag1[d1] = diag2[d2] = true;
            dfs(list, idx + 1, count + 1, isBlack);
            diag1[d1] = diag2[d2] = false;  // ▶ 복원 (백트래킹)
        }

        // ▶ 2) 스킵
        dfs(list, idx + 1, count, isBlack);
    }
}
