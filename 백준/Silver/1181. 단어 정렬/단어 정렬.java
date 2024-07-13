import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine());
        List<String> words = new ArrayList<>();

        for (int i = 0; i < N; i++) {
            words.add(br.readLine());
        }

        br.close();

        words.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));

        StringBuilder sb = new StringBuilder();
        String prevWord = "";

        for (String word : words) {
            if (!word.equals(prevWord)) {
                sb.append(word).append("\n");
                prevWord = word;
            }
        }

        System.out.print(sb.toString());
    }
}
