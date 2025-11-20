import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int TIME = 0;
    static State[][] paper;
    static boolean[][] visited;

    static class State {
        int y, x;
        int state, cnt;

        State(int y, int x, int state, int cnt) {
            this.y = y;
            this.x = x;
            this.state = state;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        paper = new State[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                int state = Integer.parseInt(st.nextToken());
                paper[i][j] = new State(i, j, state, 0);
            }
        }

        while (!isEnd()) {
            // TODO: BFS(0, 0) 외부 공기만 탐색, 치즈 만나면 접촉 횟수 + 1
            bfs();

            // TODO: paper 전체 순회 -> 접촉 횟수 2 이상인 치즈 0으로 변경
            stateChange();

            TIME++;
        }

        System.out.println(TIME);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    static void bfs() {
        Queue<State> queue = new ArrayDeque<>();
        visited = new boolean[N][M];

        queue.offer(new State(0, 0, 0, 0));

        while (!queue.isEmpty()) {
            State cur = queue.poll();

            for (int i = 0; i < 4; i++) {
                int ny = cur.y + dy[i];
                int nx = cur.x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (paper[ny][nx].state == 1) {
                    paper[ny][nx].cnt++;
                    continue;
                }
                if (visited[ny][nx]) continue;

                queue.offer(new State(ny, nx, 0, 0));
                visited[ny][nx] = true;
            }
        }
    }

    static boolean isEnd() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (paper[i][j].state == 1) return false;
            }
        }

        return true;
    }

    static void stateChange() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (paper[i][j].state == 1 && paper[i][j].cnt >= 2) paper[i][j].state = 0;
                else if (paper[i][j].state == 1) paper[i][j].cnt = 0;
            }
        }
    }
}
