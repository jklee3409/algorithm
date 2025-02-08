class Solution {
    public int[] solution(int numer1, int denom1, int numer2, int denom2) {
        int[] answer = new int[2];
        
        int gcp = gcp(denom1, denom2);
        int lcm = (denom1 * denom2) / gcp;
        
        numer1 *= (lcm / denom1);
        
        numer2 *= (lcm / denom2);
        
        int resultN = numer1 + numer2;
        int resultD = lcm;
        
        
        gcp = gcp(resultN, resultD);
        resultN /= gcp;
        resultD /= gcp;
        
        
        answer[0] = resultN;
        answer[1] = resultD;
        
        return answer;
    }
    
    public static int gcp(int n1, int n2) {
        if(n1 % n2 == 0) {
            return n2;
        }
        
        return gcp(n2, n1 % n2);
    }
}