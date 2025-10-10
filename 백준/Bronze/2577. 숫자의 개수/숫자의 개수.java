import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int A = Integer.parseInt(br.readLine());
        int B = Integer.parseInt(br.readLine());
        int C = Integer.parseInt(br.readLine());
        String result = String.valueOf(A * B * C);

        int[] arr = new int[10];
        for (int i = 0; i < result.length(); i++) {
            int cur = result.charAt(i) - '0';
            arr[cur]++;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(arr[i]).append("\n");
        }
        System.out.println(sb);
    }
}
