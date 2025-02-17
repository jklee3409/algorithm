import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int R, C;
    static char[][] graph;
    static int count = 0;
    static int[] dy = {-1, 0, 1}; // ↗ → ↘ 방향 우선 탐색

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        graph = new char[R][C];

        for (int i = 0; i < R; i++) {
            String str = br.readLine();
            for (int j = 0; j < C; j++) {
                graph[i][j] = str.charAt(j);
            }
        }

        for (int i = 0; i < R; i++) {
            if (dfs(i, 0)) { // 첫 번째 열에서 출발
                count++;
            }
        }

        System.out.println(count);
    }

    static boolean dfs(int y, int x) {
        if (x == C - 1) {
            return true; // 마지막 열에 도착하면 파이프 설치 완료
        }

        for (int i = 0; i < 3; i++) { // ↗ → ↘ 방향 우선 탐색
            int ny = y + dy[i];
            int nx = x + 1;

            if (ny >= 0 && ny < R && graph[ny][nx] == '.') { // 범위 체크 및 빈칸 확인
                graph[ny][nx] = 'O'; // 방문 표시
                if (dfs(ny, nx)) return true; // 경로 찾으면 바로 종료
            }
        }
        return false;
    }
}
