import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());

        String[] people = new String[N];
        
        st = new StringTokenizer(br.readLine());
        
        for (int i = 0; i < N; i++) {
            people[i] = st.nextToken();
        }

        Arrays.sort(people);

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < N; i++) {
            map.put(people[i], i + 1);
        }

        int[] indegree = new int[N + 1];
        List<Integer>[] graph = new ArrayList[N + 1];
        List<Integer>[] childList = new ArrayList[N + 1];

        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
            childList[i] = new ArrayList<>();
        }

        int M = Integer.parseInt(br.readLine());
        
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            String child = st.nextToken();
            String parent = st.nextToken();

            int p = map.get(parent);
            int c = map.get(child);

            graph[p].add(c);
            indegree[c]++;
        }

        Queue<Integer> q = new ArrayDeque<>();
        List<Integer> roots = new ArrayList<>();

        for (int i = 1; i <= N; i++) {
            if (indegree[i] == 0) {
                roots.add(i);
                q.offer(i);
            }
        }

        sb.append(roots.size()).append('\n');
        
        for (int root : roots) {
            sb.append(people[root - 1]).append(' ');
        }
        
        sb.append('\n');

        while (!q.isEmpty()) {
            int cur = q.poll();

            for (int next : graph[cur]) {
                if (indegree[next] == 1) {
                    childList[cur].add(next);
                }

                if (--indegree[next] == 0) {
                    q.offer(next);
                }
            }
        }

        for (int i = 1; i <= N; i++) {
            Collections.sort(childList[i]);

            sb.append(people[i - 1]).append(' ').append(childList[i].size()).append(' ');
            
            for (int child : childList[i]) {
                sb.append(people[child - 1]).append(' ');
            }
            
            sb.append('\n');
        }

        System.out.print(sb);
    }
}