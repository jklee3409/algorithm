class Solution {
    public int solution(int num1, int num2) {
        int answer = 0;
        
        double temp = num1 / (double) num2;
        
        answer = (int) (temp * 1000);
        
        return answer;
    }
}