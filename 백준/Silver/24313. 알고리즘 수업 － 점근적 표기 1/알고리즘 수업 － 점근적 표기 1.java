import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int a1 = sc.nextInt();
        int a0 = sc.nextInt();
        sc.nextLine();

        int c = sc.nextInt();
        int n0 = sc.nextInt();

        sc.close();

        if (a1 * n0 + a0 <= c * n0 && a1 <= c) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
    }
}