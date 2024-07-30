import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine()), count = 1;
        HashSet<String> dancing = new HashSet<>();
        dancing.add("ChongChong");

        while (N-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String A = st.nextToken();
            String B = st.nextToken();

            if (dancing.contains(A) && !dancing.contains(B)) {
                dancing.add(B);
                count++;
            } else if (!dancing.contains(A) && dancing.contains(B)) {
                dancing.add(A);
                count++;
            }
        }

        br.close();
        System.out.println(count);
    }
}
