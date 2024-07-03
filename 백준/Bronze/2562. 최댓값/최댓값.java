import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num;
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            num = sc.nextInt();
            list.add(num);
        }

        int max = 0;
        int location = 0;

        for (int i = 0; i < 9; i++) {
            if(list.get(i) > max){
                max = list.get(i);
                location = i + 1;
            }
        }

        System.out.println(max);
        System.out.println(location);
    }
}