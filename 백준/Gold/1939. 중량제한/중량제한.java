import java.io.*;
import java.util.*;

public class Main {

   static int N, M;
   static int[] parent;
   static List<Edge> edges = new ArrayList<>();

   static class Edge {
      int a, b, cost;

      public Edge(int a, int b, int cost) {
         this.a = a;
         this.b = b;
         this.cost = cost;
      }
   }

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      N = Integer.parseInt(st.nextToken());
      M = Integer.parseInt(st.nextToken());

      parent = new int[N + 1];
      for (int i = 1; i <= N; i++) {
         parent[i] = i;
      }

      for (int i = 0; i < M; i++) {
         st = new StringTokenizer(br.readLine());

         int a = Integer.parseInt(st.nextToken());
         int b = Integer.parseInt(st.nextToken());
         int cost = Integer.parseInt(st.nextToken());

         edges.add(new Edge(a, b, cost));
      }

      st = new StringTokenizer(br.readLine());

      int src = Integer.parseInt(st.nextToken());
      int dst = Integer.parseInt(st.nextToken());

      edges.sort((a, b) -> Integer.compare(b.cost, a.cost));

      int ans = 0;

      for (Edge edge : edges) {

         if (find(edge.a) != find(edge.b)) union(edge.a, edge.b);

         if (find(src) == find(dst)) {
            ans = edge.cost;
            break;
         }
      }

      System.out.println(ans);
   }

   private static int find(int x) {
      if (x == parent[x]) return x;
      return parent[x] = find(parent[x]);
   }

   private static void union(int a, int b) {
      int pa = find(a);
      int pb = find(b);

      if (pa != pb) parent[pb] = pa;
   }
}