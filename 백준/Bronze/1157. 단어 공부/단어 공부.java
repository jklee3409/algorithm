import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int[] arr = new int[26];
        int maxCount = 0, max_index = -1;
        char ch;

        s = s.toUpperCase();

        for (int i = 0; i < s.length(); i++) {
            arr[s.charAt(i) - 'A'] += 1;
        }

        for (int i = 0; i < arr.length; i++) {
            if(maxCount < arr[i]){
                maxCount = arr[i];
                max_index = i;
            } else if (maxCount == arr[i]) {
                maxCount = arr[i];
                max_index = -1;
            }
        }

        if (max_index == -1) {
            ch = '?';
        }else{
            ch = (char) (max_index + 'A');
        }

        System.out.println(ch);
    }
}