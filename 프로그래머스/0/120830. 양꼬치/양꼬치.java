class Solution {
    public int solution(int n, int k) {
        int answer = 0;
        
        int amount = (n * 12000) + (k * 2000);
        int service = 0;
        
        if(n >= 10) service = (n / 10) * 2000;
        
        answer = amount - service;
        
        return answer;
    }
}