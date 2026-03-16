import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());

        Map<String, Integer> map = new HashMap<>();
        String[] people = new String[N];
        int[] indegree = new int[N + 1];

        List<Integer>[] graph = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            graph[i] = new ArrayList<>();
        }

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < N; i++) {
            people[i] = st.nextToken();
        }

        Arrays.sort(people);

        for (int i = 0; i < N; i++) {
            map.put(people[i], i + 1);
        }

        int M = Integer.parseInt(br.readLine());

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            String b = st.nextToken();
            String a = st.nextToken();

            int A = map.get(a);
            int B = map.get(b);

            graph[A].add(B);
            indegree[B]++;
        }

        Queue<Integer> q = new ArrayDeque<>();

        int fatherCnt = 0;
        List<String> faterList = new ArrayList<>();

        for (int i = 1; i <= N; i++) {
            if (indegree[i] == 0) {
                fatherCnt++;
                faterList.add(people[i - 1]);
                q.offer(i);
            }
        }

        sb.append(fatherCnt).append("\n");

        Collections.sort(faterList);

        for (String father : faterList) {
            sb.append(father).append(" ");
        }

        sb.append("\n");

        List<String>[] childList = new ArrayList[N + 1];
        for (int i = 1; i <= N; i++) {
            childList[i] = new ArrayList<>();
        }

        while (!q.isEmpty()) {
            Integer cur = q.poll();

            for (Integer next : graph[cur]) {

                if (indegree[next] == 1) {
                    String child = people[next - 1];
                    childList[cur].add(child);
                }

                if (--indegree[next] == 0) {
                    q.offer(next);
                }
            }
        }

        for (int i = 1; i <= N; i++) {
            Collections.sort(childList[i]);

            sb.append(people[i - 1]).append(" ").append(childList[i].size()).append(" ");

            for (String child : childList[i]) {
                sb.append(child).append(" ");
            }

            sb.append("\n");
        }

        System.out.println(sb);
    }
}