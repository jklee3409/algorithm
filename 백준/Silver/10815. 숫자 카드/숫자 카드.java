import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        HashMap<Integer,Integer> map = new HashMap<>();

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            map.put(Integer.parseInt(st.nextToken()), i);
        }

        int M = Integer.parseInt(br.readLine());
        int[] arr = new int[M];

        st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
            if(map.containsKey(arr[i])){
                sb.append(1).append(" ");
            } else {
                sb.append(0).append(" ");
            }
        }

        br.close();
        System.out.println(sb);
    }
}
