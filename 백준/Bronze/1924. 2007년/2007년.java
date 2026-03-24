import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int x = Integer.parseInt(st.nextToken()); // 월
        int y = Integer.parseInt(st.nextToken()); // 일

        int[] daysInMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        String[] week = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        int totalDays = 0;

        for (int i = 0; i < x - 1; i++) {
            totalDays += daysInMonth[i];
        }

        totalDays += y;

        System.out.println(week[totalDays % 7]);
    }
}