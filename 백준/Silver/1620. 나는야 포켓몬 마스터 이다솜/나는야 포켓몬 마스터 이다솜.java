import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        HashMap<String, Integer> Pokemon1 = new HashMap<>();
        HashMap<Integer, String> Pokemon2 = new HashMap<>();

        for (int i = 1; i <= N; i++) {
            String name = br.readLine();
            Pokemon1.put(name, i);
            Pokemon2.put(i, name);
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            String s = br.readLine();

            if ('A' <= s.charAt(0) && s.charAt(0) <= 'Z') {
                sb.append(Pokemon1.get(s)).append("\n");
            } else {
                sb.append(Pokemon2.get(Integer.parseInt(s))).append("\n");
            }
        }

        br.close();

        System.out.println(sb);
    }
}
