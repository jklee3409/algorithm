import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();

        while (true) {
            int n = sc.nextInt();

            if (n == -1) {
                break;
            }

            List<Integer> list = new ArrayList<>();
            int sum = 0;

            for (int i = 1; i < n; i++) {
                if(n % i == 0){
                    sum += i;
                    list.add(i);
                }
            }


            if (sum == n) {
                sb.append(n).append(" = ");
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1){
                        sb.append(list.get(i));
                    }else {
                        sb.append(list.get(i)).append(" + ");
                    }
                }
                sb.append("\n");
            } else {
                sb.append(n).append(" is NOT perfect.").append("\n");
            }
        }

        System.out.println(sb);
    }
}