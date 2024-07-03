import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int find = sc.nextInt();
        List<Integer> list = new ArrayList<>();
        int num;

        for (int i = 0; i < n; i++) {
            num = sc.nextInt();
            if(num < find){
                list.add(num);
            }
        }

        for(int i = 0; i < list.size(); i ++){
            System.out.println(list.get(i));
        }
    }
}