import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int M = sc.nextInt();
        sc.nextLine();

        int[] arr = new int[N];
        long diff = 0, min_sum = 0, min_diff = 300000;

        for (int i = 0; i < N; i++) {
            arr[i] = sc.nextInt();
        }

        for (int i = 0; i < arr.length - 2; i++) {
            for (int j = i + 1; j < arr.length - 1; j++) {
                for (int k = j + 1; k < arr.length; k++) {
                    long sum = arr[i] + arr[j] + arr[k];
                    diff = M - sum;
                    if (diff < min_diff && diff >= 0) {
                        min_diff = diff;
                        min_sum = sum;
                    }
                }
            }
        }

        System.out.println(min_sum);

    }
}