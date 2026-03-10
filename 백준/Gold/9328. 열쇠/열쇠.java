import java.io.*;
import java.util.*;

public class Main {

    static int N, M;
    static char[][] map;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine());

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

            Set<Character> keySet = new HashSet<>();

            String readLine = br.readLine();
            if (readLine.charAt(0) != '0') {
                for (int i = 0; i < readLine.length(); i++) {
                    keySet.add(readLine.charAt(i));
                }
            }

            sb.append(bfs(keySet)).append("\n");
        }

        System.out.println(sb);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static int bfs(Set<Character> keySet) {
        int result = 0;

        Queue<int[]> queue = new ArrayDeque<>();
        boolean[][] visited = new boolean[N + 2][M + 2];
        List<int[]>[] doorList = new ArrayList[26];

        for (int i = 0; i < 26; i++) {
            doorList[i] = new ArrayList<>();
        }

        queue.offer(new int[]{0, 0});
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
                    visited[ny][nx] = true;
                    queue.offer(new int[]{ny, nx});
                }
                else if (now == '$') {
                    visited[ny][nx] = true;
                    queue.offer(new int[]{ny, nx});
                    map[ny][nx] = '.';
                    result++;
                }
                else if (Character.isUpperCase(now)) {
                    int idx = now - 'A';

                    if (keySet.contains(Character.toLowerCase(now))) {
                        visited[ny][nx] = true;
                        queue.offer(new int[]{ny, nx});
                    } else {
                        visited[ny][nx] = true;
                        doorList[idx].add(new int[]{ny, nx});
                    }
                }
                else if (Character.isLowerCase(now)) {
                    visited[ny][nx] = true;
                    queue.offer(new int[]{ny, nx});

                    if (!keySet.contains(now)) {
                        keySet.add(now);

                        int idx = now - 'a';
                        for (int[] door : doorList[idx]) {
                            queue.offer(new int[]{door[0], door[1]});
                        }
                    }
                }
            }
        }

        return result;
    }
}