import java.io.*;
import java.util.*;

public class Main {

   public static void main(String[] args) throws Exception{
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

      int N = Integer.parseInt(br.readLine());

      int[] arr = new int[N];
      int[] lis = new int[N];

      int len = 0;

      for (int i = 0; i < N; i++) {
         arr[i] = Integer.parseInt(br.readLine());
      }

      for (int i = 0; i < N; i++) {
         int pos = lowerBound(lis, arr[i], 0, len);
         lis[pos] = arr[i];

         if (pos == len) len++;
      }

      System.out.println(N - len);
   }

   private static int lowerBound(int[] arr, int target, int left, int right) {

      while (left < right) {
         int mid = (left + right) / 2;

         if (arr[mid] < target) left = mid + 1;
         else right = mid;
      }

      return left;
   }
}