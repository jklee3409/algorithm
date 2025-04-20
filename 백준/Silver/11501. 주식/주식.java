import java.io.*;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();

        for (int t = 0; t < T; t++) {
            int N = Integer.parseInt(br.readLine());
            int[] stock = new int[N];

            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 0; i < N; i++) {
                stock[i] = Integer.parseInt(st.nextToken());
            }

            long max = 0;
            long profit = 0;

            for (int i = N - 1; i >= 0; i--) {
                if (stock[i] > max) {
                    max = stock[i]; // 앞으로 가장 비싼 날
                } else {
                    profit += (max - stock[i]); // 이익 = 미래 최고가 - 현재가
                }
            }

            sb.append(profit).append("\n");
        }

        System.out.println(sb);
    }
}
