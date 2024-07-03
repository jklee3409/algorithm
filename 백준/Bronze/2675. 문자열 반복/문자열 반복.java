import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder[] sb = new StringBuilder[n];

        for (int i = 0; i < n; i++) {
            sb[i] = new StringBuilder();
            int m = sc.nextInt();
            String s = sc.next();

            for (int j = 0; j < s.length(); j++) {
                for (int k = 0; k < m; k++) {
                    sb[i].append(s.charAt(j));
                }
            }
        }

        for (int i = 0; i < n; i++) {
            System.out.println(sb[i]);
        }
    }
}