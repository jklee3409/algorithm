import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Main{
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        float final_grade = 0;
        float num = 0;

        for (int i = 0; i < 20; i++) {
            StringTokenizer st = new StringTokenizer(sc.nextLine(), " ");

            String sub = st.nextToken();
            float credit = Float.parseFloat(st.nextToken());
            String grade = st.nextToken();

            switch (grade) {
                case "A+":
                    final_grade += (float) (credit * 4.5);
                    num += credit;
                    break;
                case "A0":
                    final_grade += (float) (credit * 4.0);
                    num += credit;
                    break;
                case "B+":
                    final_grade += (float) (credit * 3.5);
                    num += credit;
                    break;
                case "B0":
                    final_grade += (float) (credit * 3.0);
                    num += credit;
                    break;
                case "C+":
                    final_grade += (float) (credit * 2.5);
                    num += credit;
                    break;
                case "C0":
                    final_grade += (float) (credit * 2.0);
                    num += credit;
                    break;
                case "D+":
                    final_grade += (float) (credit * 1.5);
                    num += credit;
                    break;
                case "D0":
                    final_grade += credit;
                    num += credit;
                    break;
                case "F":
                    final_grade += 0;
                    num += credit;
                    break;
                case "P":
                    break;
            }
        }

        System.out.println(final_grade / num);
    }
}