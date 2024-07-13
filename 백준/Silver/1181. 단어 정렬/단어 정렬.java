import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        String[] words = new String[N];

        for (int i = 0; i < N; i++) {
            words[i] = br.readLine();
        }

        br.close();

        Arrays.sort(words, (s1, s2) -> {

            if (s1.length() == s2.length()) {
                
                return s1.compareTo(s2);
            } else {
                
                return s1.length() - s2.length();
            }
        });

        StringBuilder sb = new StringBuilder();
        String prevWord = "";

        for (String word : words) {
            if (!word.equals(prevWord)) {
                sb.append(word).append("\n");
                prevWord = word;
            }
        }

        System.out.print(sb);
    }
}
