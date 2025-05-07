import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int F = Integer.parseInt(br.readLine());

        // 뒤 두 자리를 00으로 바꾸기
        N = (N / 100) * 100;

        // 뒤에서부터 하나씩 증가시키며 나눠떨어지는 값 찾기
        for (int i = 0; i < 100; i++) {
            if ((N + i) % F == 0) {
                // 두 자리 형식으로 출력
                System.out.printf("%02d\n", i);
                break;
            }
        }
    }
}
