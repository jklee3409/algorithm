import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        int a,b,sum;
        List<Integer> s = new ArrayList<>();

        for (int i = 0; i < num; i++) {
           a = sc.nextInt();
           b = sc.nextInt();
           sum = a + b;
           s.add(sum);
        }

        for (int i = 0; i < s.size(); i++) {
            System.out.println(s.get(i));
        }
    }
}