import java.util.*;

class Solution {
    
    static int[][] memo;
    static int answer;
    static int N, M;
    
    public int solution(int[][] info, int n, int m) {
        answer = Integer.MAX_VALUE;
        N = n;
        M = m;
        
        memo = new int[info.length + 1][m];
        for (int[] row : memo) Arrays.fill(row, Integer.MAX_VALUE);    
        
        dfs(0, 0, 0, info);
        
        return answer == Integer.MAX_VALUE ? -1 : answer;
    }
    
    void dfs(int depth, int aSum, int bSum, int[][] info) {
        if (answer <= aSum) return;

        if (memo[depth][bSum] <= aSum) return;
        
        memo[depth][bSum] = aSum;
        
        if (depth == info.length) {
            answer = Math.min(answer, aSum);
            return;
        }
        
        int nextASum = aSum + info[depth][0];
        if (nextASum < N) dfs(depth + 1, nextASum, bSum, info);
        
        int nextBSum = bSum + info[depth][1];
        if (nextBSum < M) dfs(depth + 1, aSum, nextBSum, info);
    }
}