import java.io.*;

class Solution {
    public int solution(int[][] sizes) {
        for (int i = 0; i < sizes.length; i++) {
            int w = sizes[i][0];
            int h = sizes[i][1];

            if (w < h) {
                sizes[i][0] = h;    
                sizes[i][1] = w;    
            }
        }
        
        int wMAx = 0, hMAx = 0;
        for (int[] size : sizes) {
            wMAx = Math.max(wMAx, size[0]);
            hMAx = Math.max(hMAx, size[1]);
        }

        return wMAx * hMAx;
    }
}