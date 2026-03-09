import java.io.*;
import java.util.*;

public class Main {

    static int[] parent;

    static class Star {
        double x, y;
        int idx;

        public Star(double x, double y, int idx) {
            this.x = x;
            this.y = y;
            this.idx = idx;
        }
    }

    static class Edge {
        int a, b;
        double dist;

        public Edge(int a, int b, double dist) {
            this.a = a;
            this.b = b;
            this.dist = dist;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine());
        List<Star> starList = new ArrayList<>();
        List<Edge> edgeList = new ArrayList<>();

        parent = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }

        for (int i = 1; i <= n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            double x = Double.parseDouble(st.nextToken());
            double y = Double.parseDouble(st.nextToken());

            starList.add(new Star(x, y, i));
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                Star s1 = starList.get(i);
                Star s2 = starList.get(j);

                double dx = s1.x - s2.x;
                double dy = s1.y - s2.y;
                double dist = Math.sqrt(dx * dx + dy * dy);

                edgeList.add(new Edge(s1.idx, s2.idx, dist));
            }
        }

        edgeList.sort((e1, e2) -> Double.compare(e1.dist, e2.dist));

        double cost = 0.0;
        int count = 0;

        for (Edge edge : edgeList) {
            if (find(edge.a) != find(edge.b)) {
                union(edge.a, edge.b);
                cost += edge.dist;
                count++;

                if (count == n - 1) break;
            }
        }

        System.out.printf("%.2f%n", cost);
    }

    private static int find(int x) {
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    private static void union(int a, int b) {
        int pa = find(a);
        int pb = find(b);

        if (pa != pb) {
            parent[pb] = pa;
        }
    }
}