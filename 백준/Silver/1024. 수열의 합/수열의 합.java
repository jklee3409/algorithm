import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int L = Integer.parseInt(st.nextToken());

        for (int len = L; len <= 100; len++) {
            int sum = len * (len - 1) / 2;
            int remain = N - sum;

            if (remain < 0) break;
            if (remain % len != 0) continue;

            int start = remain / len;
            if (start < 0) continue;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < len; i++) {
                sb.append(start + i).append(' ');
            }
            System.out.println(sb);
            return;
        }

        System.out.println(-1);
    }
}