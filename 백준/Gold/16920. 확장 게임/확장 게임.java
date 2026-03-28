import java.io.*;
import java.util.*;

public class Main {

   static int N, M, P;
   static char[][] map;
   static int[] result;
   static int[] cnt;
   static Queue<int[]>[] q;

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      N = Integer.parseInt(st.nextToken());
      M = Integer.parseInt(st.nextToken());
      P = Integer.parseInt(st.nextToken());

      map = new char[N][M];
      result = new int[P + 1];
      cnt = new int[P + 1];

      q = new ArrayDeque[P + 1];
      for (int i = 1; i <= P; i++) {
         q[i] = new ArrayDeque<>();
      }

      st = new StringTokenizer(br.readLine());

      for (int i = 1; i <= P; i++) {
         cnt[i] = Integer.parseInt(st.nextToken());
      }

      for (int i = 0; i < N; i++) {
         String input = br.readLine();

         for (int j = 0; j < M; j++) {
            char c = input.charAt(j);
            map[i][j] = c;

            if ('1' <= c && c <= '9') {
               int idx = c - '0';

               q[idx].offer(new int[]{i, j});
               result[idx]++;
            }
         }
      }

      while (true) {
         boolean moved = false;

         for (int player = 1; player <= P; player++) {
            if (expand(player)) moved = true;
         }

         if (!moved) break;
      }

      StringBuilder sb = new StringBuilder();

      for (int i = 1; i <= P; i++) {
         sb.append(result[i]).append(" ");
      }

      System.out.println(sb);
   }

   static int[] dy = {-1, 1, 0, 0};
   static int[] dx = {0, 0, -1, 1};

   private static boolean expand(int player) {
      boolean moved = false;

      for (int dist = 0; dist < cnt[player]; dist++) {
         int size = q[player].size();
         if (size == 0) break;

         for (int i = 0; i < size; i++) {
            int[] cur = q[player].poll();

            for (int d = 0; d < 4; d++) {
               int ny = cur[0] + dy[d];
               int nx = cur[1] + dx[d];

               if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
               if (map[ny][nx] != '.') continue;

               map[ny][nx] = (char) (player + '0');
               result[player]++;

               q[player].offer(new int[]{ny, nx});
               moved = true;
            }
         }
      }

      return moved;
   }
}