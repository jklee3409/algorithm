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

        System.out.println(solution(size, r, c));
    }

    private static int solution(int size, int r, int c) {
        int index = 0;

        while (size > 1) {
            size /= 2; // 현재 부분 배열 크기

            // 1사분면 
            if (r < size && c < size) {}

            // 2사분면 
            else if (r < size && c >= size) {
                index += size * size;
                c -= size; 
            }

            // 3사분면 
            else if (r >= size && c < size) {
                index += 2 * size * size;
                r -= size; 
            }

            // 4사분면 
            else {
                index += 3 * size * size;
                r -= size;
                c -= size;
            }
        }

        return index;
    }
}
