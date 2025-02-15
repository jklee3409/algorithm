import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Main {
    static int N, M;
    static List<List<Integer>> graph = new ArrayList<>();
    static boolean[] visited;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        for (int i = 0; i <= N; i++) graph.add(new ArrayList<>()); // 그래프 초기화
        visited = new boolean[N + 1];

        // 그래프에 간선 추가
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(st.nextToken());
            int v2 = Integer.parseInt(st.nextToken());

            // 무향 그래프
            graph.get(v1).add(v2);
            graph.get(v2).add(v1);
        }

        // 연결 요소 : 그래프 내에서 서로 연결되지 않은 요소들 (분리되어 있음)
        int componentCount = 0;

        // 방문하지 않은 노드가 있으면 연결 요소 개수++ (분리된 노드이기 때문)
        // dfs 로 탐색하면서 방문 기록
        for (int i = 1; i <= N; i++) {
            if (!visited[i]) {
                componentCount++;
                dfs(i);
            }
        }

        System.out.println(componentCount);
    }

    // dfs 로 그래프 탐색
    static void dfs(int node) {
        visited[node] = true;

        for (Integer neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                dfs(neighbor);
            }
        }
    }
}