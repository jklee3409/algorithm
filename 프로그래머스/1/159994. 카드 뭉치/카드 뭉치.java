import java.util.*;

class Solution {
    public String solution(String[] cards1, String[] cards2, String[] goal) {
        Queue<String> cardsDeque1 = new ArrayDeque<>(Arrays.asList(cards1));
        Queue<String> cardsDeque2 = new ArrayDeque<>(Arrays.asList(cards2));
        Queue<String> goalDeque = new ArrayDeque<>(Arrays.asList(goal));
        
        while (!goalDeque.isEmpty()) {
            if (!cardsDeque1.isEmpty() && cardsDeque1.peek().equals(goalDeque.peek())) { // cards1 에서 가능한지
                cardsDeque1.poll();
                goalDeque.poll();
            } else if (!cardsDeque2.isEmpty() && cardsDeque2.peek().equals(goalDeque.peek())) { // cards2 에서 가능한지
                cardsDeque2.poll();
                goalDeque.poll();
            } else {
                break;
            }
        }
        
        return goalDeque.isEmpty() ? "Yes" : "No";
    }
}