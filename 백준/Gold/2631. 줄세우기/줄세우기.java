import java.io.*;
import java.util.*;

public class Main {

   public static void main(String[] args) throws Exception{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      int N = Integer.parseInt(br.readLine());

      int[] arr = new int[N];
      int[] dp = new int[N];

      for (int i = 0; i < N; i++) {
         arr[i] = Integer.parseInt(br.readLine());
      }

      for (int i = 0; i < N; i++) {
         dp[i] = 1;

         for (int j = 0; j < i; j++) {

            if (arr[j] < arr[i] && dp[i] < dp[j] + 1) {
               dp[i] = dp[j] + 1;
            }
         }
      }

      int lis = 0;
      for (int i = 0; i < N; i++) {
         lis = Math.max(lis, dp[i]);
      }

      System.out.println(N - lis);
   }
}