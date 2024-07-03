import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Integer> list = new ArrayList<>();
        int num;

        for (int i = 0; i < n; i++) {
            num = sc.nextInt();
            list.add(num);
        }

        Collections.sort(list);

        System.out.println(list.get(0) +  " " + list.get(list.size() - 1));
    }
}