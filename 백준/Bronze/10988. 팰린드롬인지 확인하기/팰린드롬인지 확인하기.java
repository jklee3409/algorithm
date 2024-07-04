import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        StringBuilder reverse = new StringBuilder(s);
        reverse.reverse();
        int compare = 1;

        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) != reverse.charAt(i)){
                compare = 0;
                break;
            }
        }

        System.out.println(compare);

    }
}