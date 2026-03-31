import java.io.*;
import java.util.*;

public class Main {

   public static void main(String[] args) throws Exception {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      char[] str1 = br.readLine().toCharArray();
      char[] str2 = br.readLine().toCharArray();

      int len_1 = str1.length;
      int len_2 = str2.length;

      int[][] dp = new int[len_1 + 1][len_2 + 1];

      for (int i = 1; i <= len_1; i++) {
         for (int j = 1; j <= len_2; j++) {

            if (str1[i - 1] == str2[j - 1]) dp[i][j] = dp[i - 1][j - 1] + 1;
            else dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
         }
      }

      System.out.println(dp[len_1][len_2]);
   }
}