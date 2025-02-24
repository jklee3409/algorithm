class Solution {
    public int solution(int n, int w, int num) {
        int h = (n % w == 0) ? n / w : n / w + 1;
        int[][] storage = new int[h][w];

        int idx = 1, level = 0;
        int row = 0, col = 0;
        while (idx <= n) {

            if (level % 2 == 0) { // 왼쪽부터
                for (int i = 0; i < w && idx <= n; i++) {
                    storage[level][i] = idx;
                    if (idx == num) {
                        row = level;
                        col = i;
                    }
                    idx++;
                }
            } else { // 오른쪽부터
                for (int i = w - 1; i >= 0 && idx <= n; i--) {
                    storage[level][i] = idx;
                    if (idx == num){
                        row = level;
                        col = i;
                    } 
                    idx++;
                }
            }

            level++;
        }

        int result = 0;
        for (int i = row; i < h ; i++) {
            if (storage[i][col] != 0) {
                result++;
            }
        }

        return result;
    }
}