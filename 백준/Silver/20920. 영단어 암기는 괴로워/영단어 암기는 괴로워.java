import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        HashMap<String, Integer> map = new HashMap<>();

        while (N-- > 0) {
            String word = br.readLine();

            if (word.length() >= M) {
                if (map.containsKey(word)) {
                    map.put(word, map.get(word) + 1);
                } else {
                    map.put(word, 1);
                }
            }
        }

        List<String> words = new ArrayList<>(map.keySet());

        words.sort((o1, o2) -> {

            // 2개의 단어의 빈도 수가 다른 경우
            if (Integer.compare(map.get(o1), map.get(o2)) != 0) {
                return Integer.compare(map.get(o2), map.get(o1));
            }

            // 빈도 수가 같은 경우 단어 길이로 비교
            if (o1.length() != o2.length()) {
                return o2.length() - o1.length();
            }

            // 빈도 수와 단어 길이가 모두 같으면 사전 순으로 정렬
            return o1.compareTo(o2);
        });

        StringBuilder sb = new StringBuilder();
        for (String var : words) {
            sb.append(var).append("\n");
        }

        br.close();
        System.out.println(sb);
    }
}
