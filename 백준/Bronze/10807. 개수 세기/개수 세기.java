import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] s = new int[n];

        for (int i = 0; i < n; i++) {
            int num = sc.nextInt();
            s[i] = num;
        }

        int find = sc.nextInt();
        int count = 0;

        for (int i = 0; i < n; i++) {
            if(find == s[i]){
                count++;
            }
        }

        System.out.println(count);
    }
}