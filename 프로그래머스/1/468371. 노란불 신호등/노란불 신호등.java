class Solution {
    
    public int solution(int[][] signals) {
        int limit = 1;
        
        for (int[] signal : signals) {
            int period = signal[0] + signal[1] + signal[2];
            limit = lcm(limit, period);
        }
        
        for (int time = 1; time <= limit; time++) {
            boolean allYellow = true;
            
            for (int[] signal : signals) {
                int green = signal[0];
                int yellow = signal[1];
                int red = signal[2];
                
                int period = green + yellow + red;
                
                int current = (time - 1) % period;
                
                if (current < green || current >= green + yellow) {
                    allYellow = false;
                    break;
                }
            }
            
            if (allYellow) {
                return time;
            }
        }
        
        return -1;
    }
    
    int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
    
    int lcm (int a, int b) {
        return a / gcd(a, b) * b;
    }
}