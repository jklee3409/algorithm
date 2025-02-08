class Solution {
    public int[] solution(int[] num_list) {
        int len = num_list.length;
        
        for (int i = 0; i < len / 2; i++) {
            int temp = num_list[i];
            num_list[i] = num_list[len - 1 - i];
            num_list[len - 1 -i] = temp;
        }
        
        return num_list;
    }
}