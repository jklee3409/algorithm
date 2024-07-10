import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        long N = Integer.parseInt(br.readLine());
        long sum = 0;
        long prev_sum = 0;

        for (int i = 1; i <= N - 2; i++) {
            for(int j = i; j <= i; j++){
                prev_sum += j;
            }
            sum += prev_sum;
        }

        System.out.println(sum);
        System.out.println(3);
    }
}