import java.io.*;
import java.util.*;

public class Main {

    static int[] parent;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        List<double[]> starList = new ArrayList<>();

        parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }

        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());

            starList.add(new double[]{x, y, i});
        }

        List<double[]> edgeList = new ArrayList<>();

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                double[] star1 = starList.get(i);
                double[] star2 = starList.get(j);

                double xDistance = star2[0] - star1[0];
                double yDistance = star2[1] - star1[1];

                double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));

                edgeList.add(new double[]{star1[2], star2[2], distance});
            }
        }

        edgeList.sort((a, b) -> (int) (a[2] - b[2]));

        double cost = 0.0;

        for (double[] edge : edgeList) {
            int a = (int) edge[0];
            int b = (int) edge[1];

            if (find(a) != find(b)) {
                cost += edge[2];
                union(a, b);
            }
        }

        System.out.printf("%.2f", cost);
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