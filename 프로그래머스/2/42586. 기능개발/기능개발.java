import java.util.*;

class Solution {
    public List<Integer> solution(int[] progresses, int[] speeds) {
        List<Integer> result = new ArrayList<>(); // Stack 대신 List 사용
        
        int len = progresses.length;
        int[] arr = new int[len]; // 각 기능의 개발 완료까지 필요한 일수
        
        // 각 작업이 완료되기까지 걸리는 일수 계산
        for (int i = 0; i < len; i++) {
            arr[i] = (100 - progresses[i]) / speeds[i];
            if ((100 - progresses[i]) % speeds[i] != 0) {
                arr[i]++; // 나머지가 있을 경우 하루 추가
            }
        }

        int i = 0;
        while (i < len) {
            int count = 1; // 현재 배포될 기능 개수
            int curDays = arr[i]; // 기준이 되는 첫 번째 작업의 배포일
            
            int idx = i + 1;
            while (idx < len && arr[idx] <= curDays) {
                count++;
                idx++;
            }
            
            result.add(count);
            i = idx; // 다음 검사 위치 업데이트
        }

        return result;
    }
}
