import java.util.*;

class Solution {
    static int count = 0;
    
    public int solution(int[] numbers, int target) {
        int size = numbers.length;
        
        dfs(numbers, 0, 0, size, target);
        
        return count;
    }
    
    private void dfs(int[] arr, int start, int sum, int size, int target) {
        if (start == size) {
            if (sum == target) count++;
            
            return;
        }
        
        int temp = sum;
        
        sum += arr[start];
        dfs(arr, start + 1, sum, size, target);
        
        sum = temp;
            
        sum -= arr[start];
        dfs(arr, start + 1, sum, size, target);
    }
}