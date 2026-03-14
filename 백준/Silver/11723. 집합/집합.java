import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();

        int M = Integer.parseInt(br.readLine());
        boolean[] set = new boolean[21];

        while (M-- > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String cmd = st.nextToken();

            switch (cmd) {
                case "add" -> {
                    int x = Integer.parseInt(st.nextToken());
                    set[x] = true;

                }
                case "remove" -> {
                    int x = Integer.parseInt(st.nextToken());
                    set[x] = false;

                }
                case "check" -> {
                    int x = Integer.parseInt(st.nextToken());
                    sb.append(set[x] ? 1 : 0).append('\n');

                }
                case "toggle" -> {
                    int x = Integer.parseInt(st.nextToken());
                    set[x] = !set[x];

                }
                case "all" -> Arrays.fill(set, true);
                case "empty" -> Arrays.fill(set, false);
            }
        }

        System.out.print(sb);
    }
}