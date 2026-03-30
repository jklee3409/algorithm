import java.io.*;
import java.util.*;

public class Main {

   static int N, M;
   static int[][] dp;
   static int[] indegree;
   static boolean[] base;
   static List<Edge>[] edges;

   static class Edge {
      int to, cnt;

      public Edge(int to, int cnt) {
         this.to = to;
         this.cnt = cnt;
      }
   }

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringBuilder sb = new StringBuilder();
      StringTokenizer st;

      N = Integer.parseInt(br.readLine());
      M = Integer.parseInt(br.readLine());

      indegree = new int[N + 1];
      dp = new int[N + 1][N + 1];
      base = new boolean[N + 1];

      edges = new ArrayList[N + 1];

      for (int i = 1; i <= N; i++) {
         edges[i] = new ArrayList<>();
      }

      for (int i = 0; i < M; i++) {
         st = new StringTokenizer(br.readLine());

         int to = Integer.parseInt(st.nextToken());
         int from = Integer.parseInt(st.nextToken());
         int cnt = Integer.parseInt(st.nextToken());

         edges[from].add(new Edge(to, cnt));
         indegree[to]++;
      }

      Queue<Integer> q = new ArrayDeque<>();

      for (int i = 1; i <= N; i++) {
         if (indegree[i] == 0) {
            base[i] = true;
            q.offer(i);
         }
      }

      while (!q.isEmpty()) {
         int currentPart = q.poll();

         for (Edge edge : edges[currentPart]) {

            for (int i = 1; i <= N; i++) {
               int neededCount;

               if (base[currentPart]) neededCount = (i == currentPart) ? 1 : 0;
               else neededCount = dp[currentPart][i];

               dp[edge.to][i] += neededCount * edge.cnt;
            }

            if (--indegree[edge.to] == 0) {
               q.offer(edge.to);
            }
         }
      }

      for (int i = 1; i <= N; i++) {
         if (base[i]) {
            sb.append(i).append(" ").append(dp[N][i]).append("\n");
         }
      }

      System.out.println(sb);
   }
}