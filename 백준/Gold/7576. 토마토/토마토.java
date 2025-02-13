import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int[][] box;
    static Queue<Tomato> queue = new ArrayDeque<>();

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        M = Integer.parseInt(st.nextToken());
        N = Integer.parseInt(st.nextToken());
        box = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                box[i][j] = Integer.parseInt(st.nextToken());

                // 익은 토마토는 모두 큐에 저장
                if (box[i][j] == 1) {
                    queue.offer(new Tomato(i, j, 0));
                }
            }
        }

        System.out.println(solution());
    }

    static int solution () {
        int days = 0;

        while (!queue.isEmpty()) {
            Tomato t = queue.poll();
            days = t.days;

            for (int i = 0; i < 4; i++) {
                int ny = t.y + dy[i];
                int nx = t.x + dx[i];

                if (ny >= 0 && ny < N && nx >= 0 && nx < M) {
                    if (box[ny][nx] == 0) { // 익지 않은 토마토
                        box[ny][nx] = 1; // 익히고
                        queue.offer(new Tomato(ny, nx, days + 1)); // 날짜 갱신해서 큐에 삽입
                    }
                }
            }
        }

        // 모든 토마토가 익었는지 확인
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (box[i][j] == 0) return -1;
            }
        }

        return days;
    }

    static class Tomato {
        int y, x, days;

        Tomato (int y, int x, int days) {
            this.y = y;
            this.x = x;
            this.days = days;
        }
    }
}