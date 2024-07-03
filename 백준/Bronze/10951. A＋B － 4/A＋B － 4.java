import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        int a,b;

        while(sc.hasNextInt()){
            a = sc.nextInt();
            b = sc.nextInt();
            sb.append(a + b).append("\n");
        }

        sc.close();
        System.out.println(sb);
    }
}