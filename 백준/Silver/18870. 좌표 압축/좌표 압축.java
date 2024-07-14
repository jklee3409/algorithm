import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        int[] origin = new int[N];
        int[] sorted = new int[N];
        HashMap<Integer, Integer> rankingMap = new HashMap<>();

        st = new StringTokenizer(br.readLine());

        for (int i = 0; i < N; i++) {
            origin[i] = sorted[i] = Integer.parseInt(st.nextToken());
        }

        br.close();

        Arrays.sort(sorted);
        int rank = 0;

        for (int var : sorted) {
            if (!rankingMap.containsKey(var)) {
                rankingMap.put(var, rank);
                rank++;
            }
        }

        StringBuilder sb = new StringBuilder();

        for (int var : origin) {
            int ranking = rankingMap.get(var);
            sb.append(ranking).append(" ");
        }

        System.out.println(sb);
    }
}
