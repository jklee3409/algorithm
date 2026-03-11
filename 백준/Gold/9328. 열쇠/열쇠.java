import java.io.*;
import java.util.*;

public class Main {

    static int T, N, M;
    static char[][] map;
    static Set<Character> keySet = new HashSet<>();

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            N = Integer.parseInt(st.nextToken());
            M = Integer.parseInt(st.nextToken());
            map = new char[N + 2][M + 2];

            for (int i = 0; i < N + 2; i++) {
                Arrays.fill(map[i], '.');
            }

            for (int i = 1; i <= N; i++) {
                String input = br.readLine();
                for (int j = 1; j <= M; j++) {
                    map[i][j] = input.charAt(j - 1);
                }
            }

            keySet = new HashSet<>();

            String readLine = br.readLine();
            if (readLine.charAt(0) != '0') {
                for (int i = 0; i < readLine.length(); i++) {
                    keySet.add(readLine.charAt(i));
                }
            }

            sb.append(bfs()).append("\n");
        }

        System.out.println(sb);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static int bfs() {
        int result = 0;

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[N + 2][M + 2];
        List<int[]>[] doors = new ArrayList[26];

        for (int i = 0; i < 26; i++) {
            doors[i] = new ArrayList<>();
        }

        queue.add(new int[]{0, 0});
        visited[0][0] = true;

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();

            for (int d = 0; d < 4; d++) {
                int ny = cur[0] + dy[d];
                int nx = cur[1] + dx[d];

                if (ny < 0 || nx < 0 || ny >= N + 2 || nx >= M + 2) continue;
                if (visited[ny][nx] || map[ny][nx] == '*') continue;

                char now = map[ny][nx];

                if (now == '.') {
                    queue.offer(new int[]{ny, nx});
                    visited[ny][nx] = true;
                }
                else if (now == '$') {
                    map[ny][nx] = '.';
                    result++;

                    queue.offer(new int[]{ny, nx});
                    visited[ny][nx] = true;
                }
                else if (Character.isUpperCase(now)) {
                    int idx = now - 'A';

                    if (keySet.contains(Character.toLowerCase(now))) {
                        queue.offer(new int[]{ny, nx});
                        visited[ny][nx] = true;
                    }
                    else {
                        doors[idx].add(new int[]{ny, nx});
                        visited[ny][nx] = true;
                    }
                }
                else if (Character.isLowerCase(now)) {
                    queue.offer(new int[]{ny, nx});
                    visited[ny][nx] = true;

                    if (!keySet.contains(now)) {
                        keySet.add(now);

                        int idx = now - 'a';
                        for (int[] door : doors[idx]) {
                            queue.offer(new int[]{door[0], door[1]});
                        }
                    }
                }
            }
        }

        return result;
    }
}