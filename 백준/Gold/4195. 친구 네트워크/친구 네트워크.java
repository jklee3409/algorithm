import java.io.*;
import java.util.*;

public class Main {

    static int[] parent;
    static int[] size;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int T = Integer.parseInt(br.readLine());

        while (T-- > 0) {
            int F = Integer.parseInt(br.readLine());

            parent = new int[F * 2];
            size = new int[F * 2];

            for (int i = 0; i < F * 2; i++) {
                parent[i] = i;
                size[i] = 1;
            }

            int idx = 0;
            Map<String, Integer> map = new HashMap<>();

            for (int i = 0; i < F; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());

                String a = st.nextToken();
                String b = st.nextToken();

                if (!map.containsKey(a)) map.put(a, idx++);
                if (!map.containsKey(b)) map.put(b, idx++);

                int result = union(map.get(a), map.get(b));
                sb.append(result).append("\n");
            }
        }

        System.out.println(sb);
    }

    private static int find(int x) {
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    private static int union(int a, int b) {
        int pa = find(a);
        int pb = find(b);

        if (pa != pb) {
            parent[pb] = pa;
            size[pa] += size[pb];
        }

        return size[pa];
    }
}