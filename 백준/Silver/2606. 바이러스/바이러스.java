import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;


public class Main {
    static int N, M;
    static List<List<Integer>> graph = new ArrayList<>();
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine()); // 정점 개수
        M = Integer.parseInt(br.readLine()); // 간선 개수

        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>()); // 그래프 초기화
        visited = new boolean[N + 1];

        for (int i = 0; i < M; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(st.nextToken());
            int v2 = Integer.parseInt(st.nextToken());

            graph.get(v1).add(v2);
            graph.get(v2).add(v1);
        }

        // bfs 구현
        // 1번 컴퓨터를 통해 바이러스에 걸리는 컴퓨터 숫자를 구하는 것이므로
        // cnt 의 초기값은 1이 되어야 함
        int cnt = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(1);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            visited[v] = true;

            for (int neighbor : graph.get(v)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                    cnt++;
                }
            }
        }

        System.out.println(cnt);
    }
}