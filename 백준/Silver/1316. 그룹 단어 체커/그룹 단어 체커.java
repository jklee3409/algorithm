import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        int count = 0;

        for (int i = 0; i < n; i++) {
            boolean check = true;
            String s = sc.nextLine();

            for (int j = 1; j < s.length(); j++) {
                char prev = s.charAt(j - 1);
                char now = s.charAt(j);

                // 연속 x
                if (now != prev) {
                    for (int k = 0; k < j; k++) {
                        // 중복 단어
                        if (now == s.charAt(k)) {
                            check = false;
                            break;
                        }
                    }
                }
                if(!check) break;
            }
            if(check) count++;
        }
        
        System.out.println(count);
    }
}