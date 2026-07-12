import java.util.*;

class Solution {

    public int solution(String[] arr) {
        int numberCount = (arr.length + 1) / 2;

        int[] numbers = new int[numberCount];
        char[] operators = new char[numberCount - 1];

        int numberIdx = 0;
        int operatorIdx = 0;

        for (String value : arr) {
            if (value.equals("+") || value.equals("-")) operators[operatorIdx++] = value.charAt(0);
            else numbers[numberIdx++] = Integer.parseInt(value);
        }

        int[][] maxDp = new int[numberCount][numberCount];
        int[][] minDp = new int[numberCount][numberCount];

        for (int i = 0; i < numberCount; i++) {
            maxDp[i][i] = numbers[i];
            minDp[i][i] = numbers[i];
        }

        for (int length = 2; length <= numberCount; length++) {

            for (int start = 0; start + length - 1 < numberCount; start++) {
                int end = start + length - 1;

                maxDp[start][end] = Integer.MIN_VALUE;
                minDp[start][end] = Integer.MAX_VALUE;

                for (int mid = start; mid < end; mid++) {
                    char operator = operators[mid];

                    int maxCandidate;
                    int minCandidate;

                    if (operator == '+') {
                        maxCandidate =
                                maxDp[start][mid] + maxDp[mid + 1][end];

                        minCandidate =
                                minDp[start][mid] + minDp[mid + 1][end];
                    } else {
                        maxCandidate =
                                maxDp[start][mid] - minDp[mid + 1][end];

                        minCandidate =
                                minDp[start][mid] - maxDp[mid + 1][end];
                    }

                    maxDp[start][end] =
                            Math.max(maxDp[start][end], maxCandidate);

                    minDp[start][end] =
                            Math.min(minDp[start][end], minCandidate);
                }
            }
        }

        return maxDp[0][numberCount - 1];
    }
}