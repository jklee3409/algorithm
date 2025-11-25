import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int N = sc.nextInt();
        int cnt = 0;
        for (int i = 1; i <= N; i++) {
            if (isHansu(i)) {
                cnt++;
            }
        }
        System.out.println(cnt);
    }
    
    public static boolean isHansu(int n) {
        if (n < 100) {
            return true;
        }
        int a = n / 100;           // 백의 자리
        int b = (n / 10) % 10;     // 십의 자리
        int c = n % 10;            // 일의 자리
        return (a - b) == (b - c);
    }
}
