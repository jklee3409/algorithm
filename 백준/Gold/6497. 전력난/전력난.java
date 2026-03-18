import java.io.*;
import java.util.*;

public class Main {

    static int[] parent;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;

        while (true) {
            st = new StringTokenizer(br.readLine());

            int M = Integer.parseInt(st.nextToken());
            int N = Integer.parseInt(st.nextToken());

            if (N == 0 && M == 0) break;

            parent = new int[M];
            for (int i = 0; i < M; i++) {
                parent[i] = i;
            }

            List<int[]> edgeList = new ArrayList<>();

            int costSum = 0;

            for (int i = 0; i < N; i++) {
                st = new StringTokenizer(br.readLine());

                int A = Integer.parseInt(st.nextToken());
                int B = Integer.parseInt(st.nextToken());
                int cost = Integer.parseInt(st.nextToken());

                edgeList.add(new int[]{A, B, cost});

                costSum += cost;
            }

            edgeList.sort((a, b) -> Integer.compare(a[2], b[2]));

            int count = 0;
            int mstCost = 0;

            for (int[] edge : edgeList) {
                int A = edge[0];
                int B = edge[1];
                int C = edge[2];

                if (find(A) != find(B)) {
                    union(A, B);
                    mstCost += C;

                    if (++count == M - 1) break;
                }
            }

            sb.append(costSum - mstCost).append("\n");
        }

        System.out.println(sb);
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