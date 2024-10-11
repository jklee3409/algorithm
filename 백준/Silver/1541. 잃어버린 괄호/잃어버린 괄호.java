import java.beans.beancontext.BeanContextMembershipEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int sum = Integer.MAX_VALUE;

        // - 연산자로 먼저 구분
        StringTokenizer sub = new StringTokenizer(br.readLine(), "-");

        while (sub.hasMoreTokens()) {
            int temp = 0;

            StringTokenizer add = new StringTokenizer(sub.nextToken(), "+");

            // 분리된 문자열의 정수를 모두 더해줌.
            while (add.hasMoreTokens()) {
                temp += Integer.parseInt(add.nextToken());
            }

            // 식의 첫 번째는 항상 양수이기 때문에 빼면 안됨.
            if (sum == Integer.MAX_VALUE) {
                sum = temp;
            } else {
                // 각각의 더해진 값들을 빼준다.
                sum -= temp;
            }
        }

        System.out.println(sum);
    }
}
