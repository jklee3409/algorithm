import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int X = sc.nextInt();
        int n = sc.nextInt();
        int price, count;
        int sum = 0;

        for (int i = 0; i < n; i++) {
            price = sc.nextInt();
            count = sc.nextInt();

            sum += price * count;
        }

        if (sum == X) {
            System.out.println("Yes");
        }else{
            System.out.println("No");
        }

    }
}