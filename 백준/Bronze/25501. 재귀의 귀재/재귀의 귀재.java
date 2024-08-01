import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    static int recursion_count = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        StringBuilder sb = new StringBuilder();
        while (N-- > 0) {
            String str = br.readLine();
            sb.append(isPalindrome(str)).append(" ").append(recursion_count).append("\n");
            recursion_count = 0;
        }

        br.close();
        System.out.println(sb);
    }

    public static int recursion(String s, int l, int r){
        recursion_count++;
        if(l >= r) return 1;
        else if(s.charAt(l) != s.charAt(r)) return 0;
        else return recursion(s, l+1, r-1);
    }

    public static int isPalindrome(String s){
        return recursion(s, 0, s.length()-1);
    }
}
