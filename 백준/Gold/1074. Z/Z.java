import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int answer = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int r = Integer.parseInt(st.nextToken());
        int c = Integer.parseInt(st.nextToken());

        int size = (int) Math.pow(2, N);

        solution(size, r, c);

        System.out.println(answer);
    }

    private static void solution(int size, int r, int c) {
        // 기저 조건
        if (size == 1) {
            return;
        }

        int newSize = size / 2;

        // 1사분면
        if (r < newSize && c < newSize) {
            solution(newSize, r, c);
        }

        // 2사분면
        else if (r < newSize && c >= newSize) {
            answer += (size * size) / 4;
            solution(newSize, r, c - newSize);
        }

        // 3사분면
        else if (r >= newSize && c < newSize) {
            answer += (size * size / 4) * 2;
            solution(newSize, r - newSize, c);
        }

        // 4사분면
        else {
            answer += (size * size / 4) * 3;
            solution(newSize, r - newSize, c - newSize);
        }
    }
}
