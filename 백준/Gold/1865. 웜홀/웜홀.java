import java.io.*;
import java.util.*;

public class Main {

   static class Edge {
      int from, to, cost;

      public Edge(int from, int to, int cost) {
         this.from = from;
         this.to = to;
         this.cost = cost;
      }
   }

   public static void main(String[] args) throws Exception{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringBuilder sb = new StringBuilder();
      StringTokenizer st;

      int T = Integer.parseInt(br.readLine());

      while (T-- > 0) {
         st = new StringTokenizer(br.readLine());

         int N = Integer.parseInt(st.nextToken());
         int M = Integer.parseInt(st.nextToken());
         int W = Integer.parseInt(st.nextToken());

         List<Edge> edges = new ArrayList<>();

         for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());

            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            edges.add(new Edge(A, B, cost));
            edges.add(new Edge(B, A, cost));
         }

         for (int i = 0; i < W; i++) {
            st = new StringTokenizer(br.readLine());

            int A = Integer.parseInt(st.nextToken());
            int B = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            edges.add(new Edge(A, B, -cost));
         }

         long[] dist = new long[N + 1];

         boolean isPossible = false;

         for (int i = 1; i <= N; i++) {
            boolean isUpdated = false;

            for (Edge edge : edges) {

               if (dist[edge.to] > dist[edge.from] + edge.cost) {
                  dist[edge.to] = dist[edge.from] + edge.cost;
                  isUpdated = true;
               }
            }

            if (!isUpdated) break;

            if (i == N) {
               isPossible = true;
               break;
            }
         }

         sb.append(isPossible ? "YES" : "NO").append("\n");
      }

      System.out.println(sb);
   }
}