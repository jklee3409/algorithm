import java.io.*;
import java.util.StringTokenizer;

public class Main {
    static int N, S, COUNT = 0;
    static int[] arr;
    static boolean[] selected;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        S = Integer.parseInt(st.nextToken());
        arr = new int[N];
        selected = new boolean[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        subset(0);

        // 공집합
        if (S == 0) {
            COUNT--;
        }

        System.out.println(COUNT);
    }

    public static void subset(int depth) {
        if (depth == N) {
            if (sum() == S) {
                COUNT++;
            }
            return;
        }

        selected[depth] = true;
        subset(depth + 1);

        selected[depth] = false;
        subset(depth + 1);
    }

    public static int sum() {
        int sum = 0;

        for (int i = 0; i < N; i++) {
            if (selected[i]) {
                sum += arr[i];
            }
        }

        return sum;
    }
}
