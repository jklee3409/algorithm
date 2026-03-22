import java.io.*;
import java.util.*;

public class Main {

   static int N, K;
   static String[] words;
   static boolean[] visited = new boolean[26];
   static int answer = 0;

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      StringTokenizer st = new StringTokenizer(br.readLine());

      N = Integer.parseInt(st.nextToken());
      K = Integer.parseInt(st.nextToken());

      words = new String[N];
      for (int i = 0; i < N; i++) {
         words[i] = br.readLine();
      }

      if (K < 5) {
         System.out.println(0);
         return;
      }

      if (K == 26) {
         System.out.println(N);
         return;
      }

      visited['a' - 'a'] = true;
      visited['n' - 'a'] = true;
      visited['t' - 'a'] = true;
      visited['i' - 'a'] = true;
      visited['c' - 'a'] = true;

      dfs(0, 0);

      System.out.println(answer);
   }

   static void dfs(int start, int depth) {
      if (depth == K - 5) {
         answer = Math.max(answer, countReadableWords());
         return;
      }

      for (int i = start; i < 26; i++) {
         if (!visited[i]) {
            visited[i] = true;
            dfs(i + 1, depth + 1);
            visited[i] = false;
         }
      }
   }

   static int countReadableWords() {
      int count = 0;

      for (String word : words) {
         boolean canRead = true;

         for (int i = 0; i < word.length(); i++) {
            if (!visited[word.charAt(i) - 'a']) {
               canRead = false;
               break;
            }
         }

         if (canRead) {
            count++;
         }
      }

      return count;
   }
}