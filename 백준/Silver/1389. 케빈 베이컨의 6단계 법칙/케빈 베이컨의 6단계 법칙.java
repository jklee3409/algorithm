import java.io.*;
import java.util.*;

public class Main {
    static int N, M, MIN = Integer.MAX_VALUE, INSSA = 0;
    static List<List<Integer>> graph = new ArrayList<>();

    static class Friend {
        int num, step;

        Friend(int num, int step) {
            this.num = num;
            this.step = step;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>());
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            graph.get(a).add(b);
            graph.get(b).add(a);
        }

        for (int i = 1; i <= N; i++) {
            bfs(i);
        }

        System.out.println(INSSA);
    }

    static void bfs(int start) {
        Queue<Friend> queue = new ArrayDeque<>();
        boolean[] visited = new boolean[N + 1];
        queue.offer(new Friend(start, 0));
        visited[start] = true;

        int step = 0;
        while (!queue.isEmpty()) {
            Friend cur = queue.poll();

            step += cur.step;
            for (Integer friend : graph.get(cur.num)) {
                if (visited[friend]) continue;

                queue.offer(new Friend(friend, cur.step + 1));
                visited[friend] = true;
            }
        }

        if (step < MIN) {
            MIN = step;
            INSSA = start;
        }
    }
}
