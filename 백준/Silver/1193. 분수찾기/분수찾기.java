import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int numerator = 0, denominator = 0;
        int range = 2;
        int now_range = 1;
        int sector = 1;

        if (N == 1) {
            numerator = 1;
            denominator = 1;
        }else{
            while (range <= N) {
                range += now_range + 1;
                now_range++;
                sector++;
            }

            if (sector % 2 == 0) {
                numerator = 1;
                denominator = sector;

                for (int i = 0; i < N - (range - now_range); i++) {
                    numerator++;
                    denominator--;
                }
            }else {
                numerator = sector;
                denominator = 1;

                for (int i = 0; i < N - (range - now_range); i++) {
                    numerator--;
                    denominator++;
                }
            }
        }

        System.out.println(numerator + "/" + denominator);
    }
}