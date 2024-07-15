import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        HashMap<Integer, Integer> list = new HashMap<>();

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int key = Integer.parseInt(st.nextToken());
            int val = 0;
            if(list.get(key) != null) {
                val = list.get(key);
            }
            list.put(key, val + 1);
        }

        int M = Integer.parseInt(br.readLine());
        st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            int val = 0, key = Integer.parseInt(st.nextToken());
            if(list.get(key) != null){
                val = list.get(key);
            }
            sb.append(val).append(" ");
        }

        br.close();
        System.out.println(sb);
    }
}
