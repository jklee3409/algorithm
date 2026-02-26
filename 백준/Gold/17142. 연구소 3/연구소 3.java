import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static Status[][] laboratory;
    static List<Status> virusList = new ArrayList<>();
    static List<Status> selected = new ArrayList<>();
    static int answer = Integer.MAX_VALUE;
    static int emptyCount = 0;

    static class Status {
        int y, x;
        int info; // 0: 빈 칸, 1: 벽, 2: 바이러스

        public Status(int y, int x, int info) {
            this.y = y;
            this.x = x;
            this.info = info;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        laboratory = new Status[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                int info = Integer.parseInt(st.nextToken());
                laboratory[i][j] = new Status(i, j, info);

                if (info == 2) {
                    virusList.add(laboratory[i][j]);
                }
                if (info == 0) {
                    emptyCount++;
                }
            }
        }

        if (emptyCount == 0) {
            System.out.println(0);
            return;
        }

        combination(0, 0);

        System.out.println(answer == Integer.MAX_VALUE ? -1 : answer);
    }

    private static void combination(int start, int depth) {
        if (depth == M) {
            int result = spread();
            if (result != -1) answer = Math.min(answer, result);
            return;
        }

        for (int i = start; i < virusList.size(); i++) {
            selected.add(virusList.get(i));
            combination(i + 1, depth + 1);
            selected.remove(selected.size() - 1);
        }
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static int spread() {
        Queue<Status> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[N][N];

        int remain = emptyCount;
        int time = 0;

        // 초기 활성 바이러스
        for (Status virus : selected) {
            queue.offer(new Status(virus.y, virus.x, 2));
            visited[virus.y][virus.x] = true;
        }

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int s = 0; s < size; s++) {
                Status cur = queue.poll();

                for (int i = 0; i < 4; i++) {
                    int ny = cur.y + dy[i];
                    int nx = cur.x + dx[i];

                    if (ny < 0 || nx < 0 || ny >= N || nx >= N) continue;
                    if (laboratory[ny][nx].info == 1 || visited[ny][nx]) continue;

                    visited[ny][nx] = true;
                    queue.offer(new Status(ny, nx, 2));

                    if (laboratory[ny][nx].info == 0) {
                        remain--;
                    }
                }
            }

            time++;

            if (remain == 0) {
                return time;
            }
        }

        return -1;
    }
}