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

        HashMap<String, Boolean> nListen = new HashMap<>();

        for (int i = 0; i < N; i++) {
            String name = br.readLine();
            nListen.put(name, true);
        }

        List<String> list = new ArrayList<>();
        int count  = 0;

        for (int i = 0; i < M; i++) {
            String key = br.readLine();
            if (nListen.containsKey(key)) {
                list.add(key);
                count++;
            }
        }

        Collections.sort(list);
        StringBuilder sb = new StringBuilder();

        for (String var : list) {
            sb.append(var).append("\n");
        }

        br.close();
        System.out.println(count);
        System.out.println(sb);
    }
}
