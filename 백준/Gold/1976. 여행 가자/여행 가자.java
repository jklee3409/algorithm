import java.io.*;
import java.util.*;

public class Main {

    static int[] parent;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        int M = Integer.parseInt(br.readLine());

        parent = new int[N + 1];
        for (int i = 1; i <= N; i++) {
            parent[i] = i;
        }

        for (int i = 1; i <= N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            List<Integer> city = new ArrayList<>();
            city.add(i);

            for (int j = 1; j <= N; j++) {
                if (Integer.parseInt(st.nextToken()) == 1) {
                    city.add(j);
                }
            }

            for (int j = 0; j < city.size() - 1; j++) {
                union(city.get(j), city.get(j + 1));
            }
        }

        StringTokenizer st = new StringTokenizer(br.readLine());
        int first = Integer.parseInt(st.nextToken());

        int root = find(first);
        boolean possible = true;

        for (int i = 0; i < M - 1; i++) {
            int next = Integer.parseInt(st.nextToken());

            if (root != find(next)) {
                possible = false;
                break;
            }
        }

        System.out.println(possible ? "YES" : "NO");
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