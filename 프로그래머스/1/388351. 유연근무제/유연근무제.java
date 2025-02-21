class Solution {
    public int solution(int[] schedules, int[][] timelogs, int startday) {
        
        // schedules : 희망 출근 시간 ( + 10분까지 유예)
        // timeLogs : 실제 출근 시간
        // startDay : 시작 요일 (1 : 월, 2 : 화, 3 : 수, 4 : 목, 5 : 금, 6 : 토, 7 : 일)
        // 이벤트는 일주일 간 진행, 주말은 제외, 평일만 출근 시간을 모두 지키면 상품 -> 몇 명이 상품을 받을 수 있는지?

        // if (today > 7) today = 1;

        int employees = timelogs.length;
        int result = employees;

        for (int i = 0; i < employees; i++) {
            int today = startday;
            int hopeTime = convertToMinutes(schedules[i]);

            for (int j = 0; j < 7; j++) {
                if (today == 6 || today == 7) { // 주말은 예외
                    today++;
                    continue;
                }

                if (today > 7) today %= 7;

                int rushTime = convertToMinutes(timelogs[i][j]);

                // 희망 출근 시간보다 10분 이상 늦게 출근한 경우 || 다음 날에 출근한 경우
                if (rushTime > hopeTime + 10 || rushTime >= 1440) {
                    result--;
                    break;
                }
                today++;
            }
        }

        return result;
    }
    
     private static int convertToMinutes(int time) {
        int hour = time / 100;
        int minutes = time % 100;
        return hour * 60 + minutes;
    }
}