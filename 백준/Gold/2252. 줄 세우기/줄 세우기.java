import java.io.*;
import java.util.*;

public class Main {

    static List<Integer>[] graph;
    static int[] indegree;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        graph = new ArrayList[N + 1];
        indegree = new int[N + 1];

        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());

            graph[A].add(B);
            indegree[B]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();

        for (int i = 1; i <= N; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }

        StringBuilder sb = new StringBuilder();

        while (!queue.isEmpty()) {
            int cur = queue.poll();
            sb.append(cur).append(" ");

            for (Integer next : graph[cur]) {
                indegree[next]--;

                if (indegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }

        System.out.println(sb);
    }
}