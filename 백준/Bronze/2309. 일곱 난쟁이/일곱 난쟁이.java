import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int[] arr = new int[9];
        int sum = 0;

        for (int i = 0; i < 9; i++) {
            arr[i] = Integer.parseInt(br.readLine());
            sum += arr[i];
        }

        Arrays.sort(arr);

        for (int i = 0; i < 8; i++) {
            for (int j = i + 1; j < 9; j++) {
                if (sum - arr[i] - arr[j] == 100) {
                    print(i, j, arr);
                    return;
                }
            }
        }
    }

    private static void print(int i, int j, int[] arr) {
        for (int k = 0; k < 9; k++) {
            if (k != i && k != j) {
                System.out.println(arr[k]);
            }
        }
    }
}
