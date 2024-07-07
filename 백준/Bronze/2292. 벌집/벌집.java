import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int range = 2;
        int path = 1;

        if (N == 1) {
            System.out.println(path);
        }else {
            while (range <= N) {
                range = range + (6 * path);
                path++;
            }
            System.out.println(path);
        }
    }
}