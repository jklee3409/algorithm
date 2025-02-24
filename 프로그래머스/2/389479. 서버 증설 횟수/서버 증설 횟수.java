class Solution {
    public int solution(int[] players, int m, int k) {
        int[] additionServers = new int[24]; // 각 시간대에 증설한 서버 수
        int cnt = 0; // 총 증설 횟수

        for (int i = 0; i < 24; i++) {
            // 현재 시간대에 필요한 서버 수 
            int requiredServers = players[i] / m;

            // 동작 중인 서버 : i - k + 1 시부터 i - 1 시까지의 서버.
            int activeServers = 0;
            int start = Math.max(0, i - k + 1);
            for (int j = start; j < i; j++) {
                activeServers += additionServers[j];
            }

            // 필요한 서버 수와 현재 활성 서버 수의 차이만큼 추가 증설
            int additional = Math.max(0, requiredServers - activeServers);
            additionServers[i] = additional;
            cnt += additional;
        }

        return cnt;
    }
}