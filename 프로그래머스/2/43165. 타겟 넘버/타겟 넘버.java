import java.util.*;

class Solution {
    
    public int solution(int[] numbers, int target) {
        int answer = 0;
        answer = dfs(numbers, 0, 0, target);
        return answer;
    }
    
    private int dfs(int[] arr, int n, int sum, int target) {
        if (n == arr.length) {
            if (sum == target) return 1;
            return 0;
        }
        
        return dfs(arr, n + 1, sum + arr[n], target) + dfs(arr, n + 1, sum - arr[n], target);
    }
}