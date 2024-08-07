import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static int N;
    public static int[] arr;
    public static int[] operator = new int[4];
    public static int MAX = Integer.MIN_VALUE;
    public static int MIN = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        arr = new int[N];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < 4; i++) {
            operator[i] = Integer.parseInt(st.nextToken());
        }

        calc(arr[0], 1);

        br.close();
        System.out.println(MAX);
        System.out.println(MIN);
    }

    public static void calc(int num, int idx) {

        if (idx == N) {
            MAX = Math.max(num, MAX);
            MIN = Math.min(num, MIN);

            return;
        }

        for (int i = 0; i < 4; i++) {

            if (operator[i] > 0) {

                operator[i]--;

                switch (i) {

                    case 0: calc(num + arr[idx], idx + 1); break;
                    case 1: calc(num - arr[idx], idx + 1); break;
                    case 2: calc(num * arr[idx], idx + 1); break;
                    case 3: calc(num / arr[idx], idx + 1); break;

                }

                operator[i]++;
            }
        }
    }

}
