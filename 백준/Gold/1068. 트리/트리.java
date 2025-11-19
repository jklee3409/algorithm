import java.io.*;
import java.util.*;

public class Main {
    static int N, REMOVE;
    static int ROOT = 0;
    static int CNT = 0;
    static boolean[] visited;
    static List<List<Integer>> graph = new ArrayList<>();

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        visited = new boolean[N];
        for (int i = 0; i < N; i++) graph.add(new ArrayList<>());

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int parent = Integer.parseInt(st.nextToken());

            if (parent == -1) {
                ROOT = i;
                continue;
            }

            graph.get(parent).add(i);
        }

        REMOVE = Integer.parseInt(br.readLine());
        graph.get(REMOVE).clear();

        dfs(ROOT);

        System.out.println(CNT);
    }

    static void dfs(int node) {
        if (isLeaf(node)) {
            CNT++;
            return;
        }

        visited[node] = true;

        for (Integer next :  graph.get(node)) {
            if (visited[next]) continue;
            dfs(next);
        }
    }

    static boolean isLeaf(int node) {
        if (graph.get(node).isEmpty() && node != REMOVE) return true;
        return graph.get(node).size() == 1 && graph.get(node).get(0) == REMOVE;
    }
}
