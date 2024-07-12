import javax.swing.text.StyledEditorKit;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{

    public static int min = 5000;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        find_by_35(N);
        find_by_3(N);

        if(find_by_3(N) || find_by_35(N)){
            System.out.println(min);
        } else {
            System.out.println(-1);
        }
    }

    public static boolean find_by_3(int N) {
        int count = 0;
        boolean check = true;

        if (N % 3 == 0) {
            count = N / 3;
        } else {
            count = -1;
        }

        if (0 < count && count < min) {
            min = count;
        }

        if(count == -1){
            check = false;
        }

        return  check;
    }

    public static boolean find_by_35(int sugar) {
        int count = 0;
        boolean check = true;

        while (true) {

            if (sugar % 5 == 0) {
                count += sugar / 5;
                break;
            }else {
                sugar -= 3;
                count++;

                if (sugar < 0) {
                    count = -1;
                    break;
                }
            }
        }

        if (0 < count && count < min) {
            min = count;
        }

        if(count == -1){
            check = false;
        }


        return  check;
    }
}