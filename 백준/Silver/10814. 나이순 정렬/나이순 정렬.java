import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;

        int N = Integer.parseInt(br.readLine());
        String[][] memberList = new String[N][3];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            memberList[i][0] = st.nextToken();
            memberList[i][1] = st.nextToken();
            memberList[i][2] = String.valueOf(i);
        }

        Arrays.sort(memberList, (s1, s2) -> {
            if (Objects.equals(s1[0], s2[0])) {
                return Integer.parseInt(s1[2]) - Integer.parseInt(s2[2]);
            } else {
                return Integer.parseInt(s1[0]) - Integer.parseInt(s2[0]);
            }
        });

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < N; i++) {
            sb.append(memberList[i][0]).append(" ").append(memberList[i][1]).append("\n");
        }

        System.out.println(sb);
    }
}
