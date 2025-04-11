class Solution {
    public int solution(int[][] triangle) {
       int h = triangle.length;

        for (int i = 1; i < h; i++) {

            triangle[i][0] += triangle[i - 1][0];
            triangle[i][i] += triangle[i - 1][i - 1];

            for (int j = 1; j < i; j++) {
                    triangle[i][j] += Math.max(triangle[i - 1][j - 1], triangle[i - 1][j]);
            }
        }

        int max = 0;
        for (int i = 0; i < h; i++) {
            max = Math.max(max, triangle[h - 1][i]);
        }

        return max;
    }
}