import java.io.*;

class Solution {
    public int solution(int[][] sizes) {
        
        int wMax = 0, hMax = 0;
        for (int[] size : sizes) {
            wMax = Math.max(wMax, Math.max(size[0], size[1]));
            hMax = Math.max(hMax, Math.min(size[0], size[1]));
        }

        return wMax * hMax;
    }
}