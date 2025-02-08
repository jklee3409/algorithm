class Solution {
    public int solution(int[] numbers) {
        int max = Integer.MIN_VALUE;
        int n = numbers.length;
        
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                max = Math.max(max, numbers[i] * numbers[j]);
            }
        }
        
        return max == Integer.MIN_VALUE ? 0 : max;
    }
}