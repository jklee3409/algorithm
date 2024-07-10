import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();

        while (true) {
            List<Integer> list = new ArrayList<>();

            int A = sc.nextInt();
            int B = sc.nextInt();
            int C = sc.nextInt();
            sc.nextLine();

            if (A == 0 && B == 0 && C == 0) {
                break;
            }

            list.add(A);
            list.add(B);
            list.add(C);

            Collections.sort(list);

            if (A == B && A == C) {
                sb.append("Equilateral").append("\n");
            } else if (A == B || A == C || B == C) {
                if (list.get(0) + list.get(1) > list.get(2)) {
                    sb.append("Isosceles").append("\n");
                } else {
                    sb.append("Invalid").append("\n");
                }
            } else {
                if (list.get(0) + list.get(1) > list.get(2)) {
                    sb.append("Scalene").append("\n");
                } else {
                    sb.append("Invalid").append("\n");
                }
            }
        }
        System.out.println(sb);
    }
}