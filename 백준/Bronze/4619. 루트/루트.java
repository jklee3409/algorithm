import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            int B = sc.nextInt();
            int N = sc.nextInt();
            if (B == 0 && N == 0) break;

            int closest = 0;
            int minDiff = Integer.MAX_VALUE;

            for (int A = 1; ; A++) {
                int power = (int) Math.pow(A, N);
                int diff = Math.abs(power - B);
                if (diff < minDiff) {
                    minDiff = diff;
                    closest = A;
                } else {
                    break;
                }
            }
            System.out.println(closest);
        }
        sc.close();
    }
}
