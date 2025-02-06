import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int H = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        if ((M - 45) < 0) {
            H = (H - 1 < 0) ? 23 : H - 1;
            int temp = 45 - M;
            M = 60 - temp;
        } else {
            M = M - 45;
        }

        System.out.println(H + " " + M);
    }
}
