import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] x_arr = new int[N];
        int[] y_arr = new int[N];

        for(int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            x_arr[i] = Integer.parseInt(st.nextToken());
            y_arr[i] = Integer.parseInt(st.nextToken());
        }
        
        Arrays.sort(x_arr);
        Arrays.sort(y_arr);
        
        System.out.println((x_arr[N - 1] - x_arr[0]) * (y_arr[N - 1] - y_arr[0]));
    }

}