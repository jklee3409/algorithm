import java.io.*;
import java.util.*;

public class Main {
    static List<List<Integer>> graph;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int K = Integer.parseInt(br.readLine());

        while (K-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int V = Integer.parseInt(st.nextToken());
            int E = Integer.parseInt(st.nextToken());

            graph = new ArrayList<>();
            for (int i = 0; i < V + 1; i++) graph.add(new ArrayList<>());

            for (int i = 0; i < E; i++) {
                st = new StringTokenizer(br.readLine());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());

                graph.get(a).add(b);
                graph.get(b).add(a);
            }

            boolean result = bfs(V);

            if (result) sb.append("YES\n");
            else sb.append("NO\n");
        }

        System.out.println(sb);
    }

    static boolean bfs(int V) {
        Queue<Integer> queue = new ArrayDeque<>();
        int[] color = new int[V + 1];

        for (int i = 1; i < V + 1; i++) {
            if (color[i] == 0) {
                color[i] = 1;
                queue.add(i);

                while (!queue.isEmpty()) {
                    int cur = queue.poll();

                    for (Integer next : graph.get(cur)) {
                        if (color[next] == 0) {
                            color[next] = -color[cur];
                            queue.offer(next);

                        } else if (color[next] == color[cur]) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
