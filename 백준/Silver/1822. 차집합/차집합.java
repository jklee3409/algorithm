import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class Main {
    static int NA, NB;
    static int[] A;
    static Set<Integer> B = new HashSet<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        NA = Integer.parseInt(st.nextToken());
        NB = Integer.parseInt(st.nextToken());
        A = new int[NA];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < NA; i++) {
            A[i] = Integer.parseInt(st.nextToken());
        }

        Arrays.sort(A);

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < NB; i++) {
            B.add(Integer.parseInt(st.nextToken()));
        }

        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NA; i++) {
            if (B.contains(A[i])) continue;

            sb.append(A[i]).append(" ");
            count++;
        }

        System.out.println(count);
        System.out.println(sb);
    }
}
