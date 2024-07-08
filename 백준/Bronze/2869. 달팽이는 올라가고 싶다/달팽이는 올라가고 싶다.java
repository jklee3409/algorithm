import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        long A = sc.nextInt();
        long B = sc.nextInt();
        long V = sc.nextInt();

        long dayDist = A - B;
        long goal = V - B;

        if (goal % dayDist != 0) {
            System.out.println(goal / dayDist + 1);
        }else {
            System.out.println(goal / dayDist);
        }

    }
}