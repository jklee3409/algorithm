import java.io.*;
import java.util.*;

public class Main {

   static int N, M, R;
   static int[] items;
   static int[] dist;
   static List<Edge>[] graph;

   static class Edge{
      int to, cost;

      public Edge(int to, int cost) {
         this.to = to;
         this.cost = cost;
      }
   }

   static class State implements Comparable<State> {
      int node, dist;

      public State(int node, int dist) {
         this.node = node;
         this.dist = dist;
      }

      @Override
      public int compareTo(State o) {
         return Integer.compare(this.dist, o.dist);
      }
   }

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      N = Integer.parseInt(st.nextToken());
      M = Integer.parseInt(st.nextToken());
      R = Integer.parseInt(st.nextToken());

      items = new int[N + 1];
      graph = new ArrayList[N + 1];

      for (int i = 1; i <= N; i++) {
         graph[i] = new ArrayList<>();
      }

      st = new StringTokenizer(br.readLine());

      for (int i = 1; i <= N; i++) {
         items[i] = Integer.parseInt(st.nextToken());
      }

      for (int i = 0; i < R; i++) {
         st = new StringTokenizer(br.readLine());

         int a = Integer.parseInt(st.nextToken());
         int b = Integer.parseInt(st.nextToken());
         int c = Integer.parseInt(st.nextToken());

         graph[a].add(new Edge(b, c));
         graph[b].add(new Edge(a, c));
      }

      int ans = 0;

      for (int start = 1; start <= N; start++) {
         dijkstra(start);

         int sum = 0;

         for (int i = 1; i <= N; i++) {
            if (dist[i] <= M) sum += items[i];
         }

         ans = Math.max(ans, sum);
      }

      System.out.println(ans);
   }

   private static void dijkstra(int start) {
      dist = new int[N + 1];
      Arrays.fill(dist, Integer.MAX_VALUE);

      PriorityQueue<State> pq = new PriorityQueue<>();

      pq.offer(new State(start, 0));
      dist[start] = 0;

      while (!pq.isEmpty()) {
         State cur = pq.poll();

         if (dist[cur.node] < cur.dist) continue;

         for (Edge next : graph[cur.node]) {

            if (dist[next.to] > dist[cur.node] + next.cost) {
               dist[next.to] = dist[cur.node] + next.cost;
               pq.offer(new State(next.to, dist[next.to]));
            }
         }
      }
   }
}