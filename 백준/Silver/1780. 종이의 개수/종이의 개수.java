import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int[][] arr;
    static int[] result = new int[3];

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        arr = new int[N][N];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        solution(0,0, N);

        System.out.println(result[0]);
        System.out.println(result[1]);
        System.out.println(result[2]);
    }

    private static void solution(int x, int y, int size) {
        if (check(x,y,size)) {
            result[arr[x][y] + 1]++;
            return;
        }

        int newSize = size / 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                solution(x + i * newSize, y + j * newSize, newSize);
            }
        }
    }

    static boolean check(int x, int y, int size) {
        int first = arr[x][y];

        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                if (first != arr[i][j]) return false;
            }
        }

        return true;
    }
}