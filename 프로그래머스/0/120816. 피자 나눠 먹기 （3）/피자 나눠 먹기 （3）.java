class Solution {
    public int solution(int slice, int n) {
        int answer = 1;
        
        while (true) {
            if (n <= slice * answer) break;
            
            answer++;
        }
        
        return answer;
    }
}