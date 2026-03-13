import java.io.*;
import java.util.*;

public class Main {

    static List<Integer>[] graph;
    static int[] indegree;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        indegree = new int[N + 1];

        graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int num = Integer.parseInt(st.nextToken());

            List<Integer> temp = new ArrayList<>();

            for (int j = 0; j < num; j++) {
                temp.add(Integer.parseInt(st.nextToken()));
            }

            for (int j = 0; j < num - 1; j++) {
                for (int k = j + 1; k < num; k++) {
                    int cur = temp.get(j);
                    int next = temp.get(k);

                    graph[cur].add(next);
                    indegree[next]++;
                }
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        int n = 0;

        for (int i = 1; i <= N; i++) {
            if (indegree[i] == 0) {
                q.offer(i);
            }
        }

        StringBuilder sb = new StringBuilder();

        while (!q.isEmpty()) {
            Integer cur = q.poll();
            sb.append(cur).append("\n");
            n++;

            for (Integer next : graph[cur]) {
                indegree[next]--;

                if (indegree[next] == 0) {
                    q.offer(next);
                }
            }
        }

        System.out.println(N == n ? sb : 0);
    }
}