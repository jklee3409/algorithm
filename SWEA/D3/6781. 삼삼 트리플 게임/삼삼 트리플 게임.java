import java.io.*;
import java.util.*;

public class Solution {

    static class Card {
        int number;
        char color;

        public Card(int number, char color) {
            this.number = number;
            this.color = color;
        }
    }

    static Card[] cards;
    static boolean[] visited;
    static boolean winPossible;

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuffer sb = new StringBuffer();

        int T = Integer.parseInt(br.readLine());

        int num = 1;

        while (T-- > 0) {
            String numInput = br.readLine();
            String cardInput = br.readLine();

            cards = new Card[9];
            visited = new boolean[9];
            winPossible = false;

            for (int i = 0; i < 9; i++) {
                int number = numInput.charAt(i) - '0';
                char color = cardInput.charAt(i);

                cards[i] = new Card(number, color);
            }

            dfs(0);

            sb.append("#").append(num++).append(" ");

            if (winPossible) {
                sb.append("Win");
            } else {
                sb.append("Continue");
            }

            sb.append("\n");
        }

        System.out.println(sb);
    }

    static void dfs(int count) {
        if (winPossible) return;

        if (count == 3) {
            winPossible = true;
            return;
        }

        int first = -1;

        for (int i = 0; i < 9; i++) {
            if (!visited[i]) {
                first = i;
                break;
            }
        }

        visited[first] = true;

        for (int second = first + 1; second < 9; second++) {
            if (visited[second]) continue;

            visited[second] = true;

            for (int third = second + 1; third < 9; third++) {
                if (visited[third]) continue;

                if (!isPossible(cards[first], cards[second], cards[third])) {
                    continue;
                }

                visited[third] = true;

                dfs(count + 1);

                visited[third] = false;
            }

            visited[second] = false;
        }

        visited[first] = false;
    }

    static boolean isPossible(Card first, Card second, Card third) {
        if (first.color != second.color || second.color != third.color) {
            return false;
        }

        int[] numbers = {
                first.number,
                second.number,
                third.number
        };

        Arrays.sort(numbers);

        if (numbers[0] == numbers[1] && numbers[1] == numbers[2]) {
            return true;
        }

        return numbers[0] + 1 == numbers[1]
                && numbers[1] + 1 == numbers[2];
    }
}