import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[30];
        List<Integer> list = new ArrayList<>();

        for (int i = 0; i < 28; i++) {
            int index = sc.nextInt();
            arr[index - 1] = index;
        }

        for (int i = 0; i < 30; i++) {
            if(arr[i] == 0){
                list.add(i + 1);
            }
        }

        Collections.sort(list);

        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
}