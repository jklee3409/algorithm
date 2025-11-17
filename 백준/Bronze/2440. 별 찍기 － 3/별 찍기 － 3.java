import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int repeat = n;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append("*".repeat(repeat));
            sb.append("\n");
            repeat--;
        }

        System.out.println(sb);
    }
}