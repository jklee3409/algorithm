import java.io.*;
import java.util.*;

public class Main {

   static int N, M;

   static List<int[]>[][] switches;
   static boolean[][] visited;
   static boolean[][] light;

   public static void main(String[] args) throws Exception{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      N = Integer.parseInt(st.nextToken());
      M = Integer.parseInt(st.nextToken());

      switches = new ArrayList[N + 1][N + 1];
      visited = new boolean[N + 1][N + 1];
      light = new boolean[N + 1][N + 1];

      for (int i = 1; i <= N; i++) {
         for (int j = 1; j <= N; j++) {
            switches[i][j] = new ArrayList<>();
         }
      }

      for (int i = 0; i < M; i++) {
         st = new StringTokenizer(br.readLine());

         int x = Integer.parseInt(st.nextToken());
         int y = Integer.parseInt(st.nextToken());
         int a = Integer.parseInt(st.nextToken());
         int b = Integer.parseInt(st.nextToken());

         switches[x][y].add(new int[]{a, b});
      }

      System.out.println(bfs());
   }

   static int[] dy = {-1, 1, 0, 0};
   static int[] dx = {0, 0, -1, 1};

   private static int bfs() {
      int count = 1;

      Queue<int[]> q = new ArrayDeque<>();

      q.offer(new int[]{1, 1});
      visited[1][1] = true;
      light[1][1] = true;

      while (!q.isEmpty()) {
         int[] cur = q.poll();
         int y = cur[0];
         int x = cur[1];

         for (int[] next : switches[y][x]) {
            int ny = next[0];
            int nx = next[1];

            if (light[ny][nx]) continue;

            light[ny][nx] = true;
            count++;

            if (hasVisitedNeighbor(ny, nx)) {
               q.offer(new int[]{ny, nx});
               visited[ny][nx] = true;
            }
         }

         for (int d = 0; d < 4; d++) {
            int ny = y + dy[d];
            int nx = x + dx[d];

            if (outOfRange(ny, nx)) continue;
            if (!light[ny][nx] || visited[ny][nx]) continue;

            q.offer(new int[]{ny, nx});
            visited[ny][nx] = true;
         }
      }

      return count;
   }

   private static boolean hasVisitedNeighbor(int y, int x) {
      for (int d = 0; d < 4; d++) {
         int ny = y + dy[d];
         int nx = x + dx[d];

         if (outOfRange(ny, nx)) continue;
         if (visited[ny][nx]) return true;
      }
      return false;
   }

   private static boolean outOfRange(int y, int x) {
      return y < 1 || x < 1 || y > N || x > N;
   }
}