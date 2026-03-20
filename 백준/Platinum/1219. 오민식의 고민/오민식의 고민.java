import java.io.*;
import java.util.*;

public class Main {
   
   static final long NEG_INF = Long.MIN_VALUE / 4;

   static class Edge {
      int from, to, cost;

      Edge(int from, int to, int cost) {
         this.from = from;
         this.to = to;
         this.cost = cost;
      }
   }

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      int N = Integer.parseInt(st.nextToken());
      int S = Integer.parseInt(st.nextToken());
      int E = Integer.parseInt(st.nextToken());
      int M = Integer.parseInt(st.nextToken());

      List<Edge> edges = new ArrayList<>();
      
      List<Integer>[] graph = new ArrayList[N];
      for (int i = 0; i < N; i++) graph[i] = new ArrayList<>();

      for (int i = 0; i < M; i++) {
         st = new StringTokenizer(br.readLine());

         int from = Integer.parseInt(st.nextToken());
         int to = Integer.parseInt(st.nextToken());
         int cost = Integer.parseInt(st.nextToken());

         edges.add(new Edge(from, to, cost));
         graph[from].add(to);
      }

      long[] earn = new long[N];
      
      st = new StringTokenizer(br.readLine());
      
      for (int i = 0; i < N; i++) {
         earn[i] = Long.parseLong(st.nextToken());
      }

      long[] dist = new long[N];
      Arrays.fill(dist, NEG_INF);
      dist[S] = earn[S];

      boolean[] cycle = new boolean[N];

      for (int i = 0; i < N; i++) {
         for (Edge edge : edges) {
            if (dist[edge.from] == NEG_INF) continue;

            long nextMoney = dist[edge.from] - edge.cost + earn[edge.to];

            if (dist[edge.to] < nextMoney) {
               dist[edge.to] = nextMoney;
               if (i == N - 1) {
                  cycle[edge.to] = true;
               }
            }
         }
      }

      if (dist[E] == NEG_INF) {
         System.out.println("gg");
         return;
      }

      Queue<Integer> q = new LinkedList<>();
      boolean[] visited = new boolean[N];

      for (int i = 0; i < N; i++) {
         if (cycle[i]) {
            q.offer(i);
            visited[i] = true;
         }
      }

      while (!q.isEmpty()) {
         int cur = q.poll();

         if (cur == E) {
            System.out.println("Gee");
            return;
         }

         for (int next : graph[cur]) {
            if (visited[next]) continue;

            visited[next] = true;
            q.offer(next);
         }
      }

      System.out.println(dist[E]);
   }
}