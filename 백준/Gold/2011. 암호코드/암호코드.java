import java.io.*;
import java.util.*;

public class Main {

    static int MILLION = 1000000;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String input = br.readLine();
        int len = input.length();

        int[] arr = new int[len + 1];
        int[] dp = new int[len + 1];

        for (int i = 1; i <= len; i++) {
            arr[i] = input.charAt(i - 1) - '0';
        }

        if (arr[1] == 0) {
            System.out.println(0);
            return;
        }

        dp[0] = 1;
        dp[1] = 1;

        for (int i = 2; i <= len; i++) {
            int one = arr[i];
            int two = arr[i - 1] * 10 + arr[i];

            if (1 <= one && one <= 9) {
                dp[i] = (dp[i] + dp[i - 1]) % MILLION;
            }

            if (10 <= two && two <= 26) {
                dp[i] = (dp[i] + dp[i - 2]) % MILLION;
            }
        }

        System.out.println(dp[len]);
    }
}