import java.io.*;
import java.util.*;

public class Main {

    static String target = "123456780";

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 3; j++) {
                sb.append(st.nextToken());
            }
        }

        String input = sb.toString();

        System.out.println(bfs(input));
    }

    private static int bfs(String s) {
        Queue<String> q = new ArrayDeque<>();
        Map<String, Integer> dist = new HashMap<>();

        q.offer(s);
        dist.put(s, 0);

        while (!q.isEmpty()) {
            String cur = q.poll();

            if (cur.equals(target)) return dist.get(cur);

            int zeroIdx = cur.indexOf('0');
            int y = zeroIdx / 3;
            int x = zeroIdx % 3;

            for (int d = 0; d < 4; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];

                if (ny < 0 || nx < 0 || ny >= 3 || nx >= 3) continue;

                int nextIdx = ny * 3 + nx;

                String next = swap(cur, zeroIdx, nextIdx);

                if (dist.containsKey(next)) continue;

                dist.put(next, dist.get(cur) + 1);
                q.offer(next);
            }
        }

        return -1;
    }

    private static String swap(String s, int i, int j) {
        char[] arr = s.toCharArray();

        char temp = arr[j];
        arr[j] = arr[i];
        arr[i] = temp;

        return new String(arr);
    }
}