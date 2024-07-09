import javax.swing.text.StyledEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int count = 0;
        sc.nextLine();

        for (int i = 0; i < N; i++) {
            boolean check = true;
            int num = sc.nextInt();

            if (num == 1) {
                continue;
            } else if (num == 2) {
                count++;
            } else{
                for (int j = 2; j < num; j++) {
                    if(num % j == 0){
                        check = false;
                        break;
                    }
                }
                if (check) {
                    count++;
                }
            }
        }

        System.out.println(count);
    }
}