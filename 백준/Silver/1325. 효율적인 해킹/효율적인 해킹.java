import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int MAX = 0;
    static List<Integer> result = new ArrayList<>();
    static boolean[] visited;
    static List<List<Integer>> graph = new ArrayList<>();

    static class Computer {
        int num, cnt;

        Computer(int num, int cnt) {
            this.num = num;
            this.cnt = cnt;
        }
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        for (int i = 0; i < N + 1; i++) graph.add(new ArrayList<>());

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());

            graph.get(B).add(A);
        }

        for (int i = 1; i < N + 1; i++) {
            visited = new boolean[N + 1];
            int cnt = bfs(i);

            if (MAX < cnt) { // 최댓값이 갱신되는 경우, 결과도 초기화
                MAX = cnt;
                result.clear();
                result.add(i);

            } else if (MAX == cnt) {
                result.add(i);
            }
        }

        StringBuilder sb = new StringBuilder();
        for (Integer i : result) {
            sb.append(i).append(" ");
        }

        System.out.println(sb);
    }

    static int bfs(int root) {
        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(root);
        visited[root] = true;

        int cnt = 0;
        while (!queue.isEmpty()) {
            Integer cur = queue.poll();

            for (Integer next : graph.get(cur)) {
                if (visited[next]) continue;

                queue.offer(next);
                visited[next] = true;
                cnt++;
            }
        }


        return cnt;
    }
}
