class Solution {
    
    private int[][] cost;
    private int[][] hint;
    private int n;
    
    private int answer = Integer.MAX_VALUE;
    
    public int solution(int[][] cost, int[][] hint) {
        this.cost = cost;
        this.hint = hint;
        this.n = cost.length;
        
        int[] hintCount = new int[n];
        
        dfs(0, 0, hintCount);
        
        return answer;
    }
    
    void dfs(int stage, int totalCost, int[] hintCount) {
        if (stage == n) {
            answer = Math.min(answer, totalCost);
            return;
        }
        
        if (totalCost >= answer) return;
        
        int usedHintCount = Math.min(hintCount[stage], n - 1);
        int nextCost = totalCost + cost[stage][usedHintCount];
        
        if (stage == n - 1) {
            answer = Math.min(answer, nextCost);
            return;
        }
        
        dfs(stage + 1, nextCost, hintCount);
        
        for (int i = 1; i < hint[stage].length; i++) {
            int hintStage = hint[stage][i] - 1;
            hintCount[hintStage]++;
        }
        
        dfs(stage + 1, nextCost + hint[stage][0], hintCount);
        
        for (int i = 1; i < hint[stage].length; i++) {
            int hintStage = hint[stage][i] - 1;
            hintCount[hintStage]--;
        }
    }
}