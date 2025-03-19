import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static long[] price, distance;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        distance = new long[N - 1];
        price = new long[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N - 1; i++) {
            distance[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            price[i] = Integer.parseInt(st.nextToken());
        }

        int idx = 0;
        long result = 0;
        long min = Integer.MAX_VALUE;

        while (idx < N - 1) {

            // 현재 가격이 더 저렴하면
            if (price[idx] < min) {
                min = price[idx]; // 최저값 갱신
                result += price[idx] * distance[idx]; // 갱신된 최저값으로 주유
                idx++;
            } else {
                result += min * distance[idx]; // 이전 최저값으로 주유
                idx++;
            }
        }

        System.out.println(result);
    }
}
