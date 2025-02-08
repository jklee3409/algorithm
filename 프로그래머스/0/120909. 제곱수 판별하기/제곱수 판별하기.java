class Solution {
    public int solution(int n) {
        return (Math.sqrt(n) % (int) Math.sqrt(n)) == 0 ? 1 : 2;
    }
}