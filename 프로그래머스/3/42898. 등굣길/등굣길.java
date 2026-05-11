import java.util.*;

class Solution {
    
    static final int MOD = 1000000007;
    
    int[][] dp; // 왼쪽이나 위에서만 올 수 있음.
    
    public int solution(int m, int n, int[][] puddles) {
        dp = new int[n + 1][m + 1];
        
        dp[1][1] = 1;
        
        boolean[][] puddle = new boolean[n + 1][m + 1];
        
        for (int[] p : puddles) {
            int x = p[0];
            int y = p[1];
            
            puddle[y][x] = true;
        }
        
        for (int y = 1; y <= n; y++) {
            for (int x = 1; x <= m; x++) {
                if (y == 1 && x == 1) continue;
            
                dp[y][x] = (dp[y - 1][x] + dp[y][x - 1]) % MOD;
                
                if (puddle[y][x]) dp[y][x] = 0;
            }
        }
        
        return dp[n][m];
    }
}