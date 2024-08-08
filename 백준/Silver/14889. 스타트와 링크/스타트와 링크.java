import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static int N;
    public static int MIN = Integer.MAX_VALUE;
    public static int[][] team;
    public static boolean[] visit;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        team = new int[N][N];
        visit = new boolean[N];
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                team[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        br.close();

        match(0, 0);
        System.out.println(MIN);
    }

    public static void match(int idx, int count) {

        if (count == N / 2) {
            diff();
            return;
        }

        for (int i = idx; i < N; i++) {

            if (!visit[i]) {
                visit[i] = true;
                match(i + 1, count + 1);
                visit[i] = false;
            }
        }
    }

    public static void diff() {
        int start = 0;
        int link = 0;

        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {

                if (visit[i] && visit[j]) {
                    start += team[i][j];
                    start += team[j][i];
                } else if (!visit[i] && !visit[j]) {
                    link += team[i][j];
                    link += team[j][i];
                }
            }
        }

        int val = Math.abs(start - link);

        if (val == 0) {
            System.out.println(0);
            System.exit(0);
        }

        MIN = Math.min(val, MIN);
    }
}
