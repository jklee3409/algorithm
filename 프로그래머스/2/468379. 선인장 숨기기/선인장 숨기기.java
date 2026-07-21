import java.util.*;

class Solution {

    public int[] solution(int m, int n, int h, int w, int[][] drops) {
        int neverWet = drops.length + 1;
        int[][] rainTime = new int[m][n];

        for (int r = 0; r < m; r++) {
            Arrays.fill(rainTime[r], neverWet);
        }

        for (int i = 0; i < drops.length; i++) {
            int r = drops[i][0];
            int c = drops[i][1];
            rainTime[r][c] = i + 1;
        }

        int columnCount = n - w + 1;
        int[][] horizontalMin = new int[m][columnCount];

        for (int r = 0; r < m; r++) {
            horizontalMin[r] = slidingWindowMin(rainTime[r], w);
        }

        int bestTime = -1;
        int bestRow = 0;
        int bestColumn = 0;

        for (int c = 0; c < columnCount; c++) {
            int[] column = new int[m];

            for (int r = 0; r < m; r++) {
                column[r] = horizontalMin[r][c];
            }

            int[] verticalMin = slidingWindowMin(column, h);

            for (int r = 0; r < verticalMin.length; r++) {
                if (verticalMin[r] > bestTime) {
                    bestTime = verticalMin[r];
                    bestRow = r;
                    bestColumn = c;
                }
            }
        }

        return new int[]{bestRow, bestColumn};
    }

    private int[] slidingWindowMin(int[] values, int window) {
        int[] result = new int[values.length - window + 1];
        Deque<Integer> deque = new ArrayDeque<>();

        for (int i = 0; i < values.length; i++) {
            while (!deque.isEmpty() && deque.peekFirst() <= i - window) {
                deque.pollFirst();
            }

            while (!deque.isEmpty() && values[deque.peekLast()] >= values[i]) {
                deque.pollLast();
            }

            deque.addLast(i);

            if (i >= window - 1) {
                result[i - window + 1] = values[deque.peekFirst()];
            }
        }

        return result;
    }
}