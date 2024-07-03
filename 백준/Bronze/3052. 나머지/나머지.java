import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[10];

        for (int i = 0; i < 10; i++) {
            int num = sc.nextInt();
            arr[i] = num % 42;
        }

        int count = 0;
        boolean check;

        for (int i = 0; i < arr.length; i++) {
            check = false;
            for (int j = i + 1; j < arr.length; j++) {
                if(arr[i] == arr[j]){
                    check = true;
                    break;
                }
            }if (!check) {
                count++;
            }
        }

        System.out.println(count);
    }
}