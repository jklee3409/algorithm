import java.io.*;
import java.util.*;

public class Main {
    static int[] parent;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        int truthCount = Integer.parseInt(st.nextToken());

        parent = new int[N + 1];
        for (int i = 1; i < N + 1; i++) parent[i] = i;

        int[] truth = new int[truthCount];
        for (int i = 0; i < truthCount; i++) truth[i] = Integer.parseInt(st.nextToken());

        List<int[]> parties = new ArrayList<>();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int size = Integer.parseInt(st.nextToken());
            int[] party = new int[size];

            for (int j = 0; j < size; j++) {
                party[j] = Integer.parseInt(st.nextToken());
            }

            for (int j = 0; j < size - 1; j++) {
                union(party[j], party[j + 1]);
            }

            parties.add(party);
        }

        int result = 0;
        for (int[] party : parties) {
            boolean possible = true;

            for (int person : party) {

                for (int t : truth) {

                    if (find(person) == find(t)) {
                        possible = false;
                        break;
                    }
                }

                if (!possible) break;
            }

            if (possible) result++;
        }

        System.out.println(result);
    }

    private static int find(int x) {
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    private static void union(int a, int b) {
        int pa = find(a);
        int pb = find(b);

        if (pa != pb) parent[pb] = pa;
    }
}