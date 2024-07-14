import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        HashMap<String, Boolean> log = new HashMap<>();

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String name = st.nextToken();
            boolean enter = Objects.equals(st.nextToken(), "enter");

            log.put(name, enter);
        }

        br.close();
        List<String> enterList = new ArrayList<>();

        for (Map.Entry<String, Boolean> entry : log.entrySet()) {
            if (entry.getValue()) {
                enterList.add(entry.getKey());
            }
        }

        enterList.sort(Collections.reverseOrder());
        StringBuilder sb = new StringBuilder();

        for (String s : enterList) {
            sb.append(s).append("\n");
        }

        System.out.println(sb);
    }
}
