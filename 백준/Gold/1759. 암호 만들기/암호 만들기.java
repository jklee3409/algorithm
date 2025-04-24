import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    static int L, C;
    static char[] input;
    static char[] target;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        L = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        input = new char[C];
        target = new char[L];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < C; i++) {
            input[i] = st.nextToken().charAt(0);
        }

        Arrays.sort(input);
        solution(0, 0);
        System.out.println(sb);

        br.close();
    }

    public static void solution(int depth, int start) {
        if (depth == L) {
            if (!isValid()) return;

            for (char i : target) {
                sb.append(i);
            }
            sb.append("\n");
            return;
        }

        for (int i = start; i < C; i++) {
            target[depth] = input[i];
            solution(depth + 1, i + 1);
        }
    }

    public static boolean isValid() {
        boolean containVowel = false;
        int consonantCnt = 0;

        for (int i = 0; i < L; i++) {
            if (target[i] == 'a' || target[i] == 'e' || target[i] == 'i' || target[i] == 'o' || target[i] == 'u') {
                containVowel = true;
                continue;
            }
            consonantCnt++;
        }

        return (2 <= consonantCnt) && containVowel;
    }
}
