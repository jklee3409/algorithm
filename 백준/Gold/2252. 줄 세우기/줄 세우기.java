import java.io.*;
import java.util.*;

public class Main {

   static int N, M;
   static int[] indegree;
   static List<Integer>[] graph;

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      N = Integer.parseInt(st.nextToken());
      M = Integer.parseInt(st.nextToken());

      indegree = new int[N + 1];
      graph = new ArrayList[N + 1];

      for (int i = 1; i <= N; i++) {
         graph[i] = new ArrayList<>();
      }

      for (int i = 0; i < M; i++) {
         st = new StringTokenizer(br.readLine());

         int A = Integer.parseInt(st.nextToken());
         int B = Integer.parseInt(st.nextToken());

         graph[A].add(B);
         indegree[B]++;
      }

      Queue<Integer> q = new ArrayDeque<>();

      for (int i = 1; i <= N; i++) {
         if (indegree[i] == 0) {
            q.offer(i);
         }
      }

      StringBuilder sb = new StringBuilder();

      while (!q.isEmpty()) {
         int cur = q.poll();

         sb.append(cur).append(" ");

         for (int next : graph[cur]) {

            if (--indegree[next] == 0) {
               q.offer(next);
            }
         }
      }

      System.out.println(sb);
   }
}