import java.util.*;

class Solution {
    
    public int[] solution(String[] id_list, String[] report, int k) {
        Map<String, Set<String>> reportMap = new HashMap<>();
        Map<String, Integer> countMap = new HashMap();
        
        // 1. 누가 누구 신고했는지 map으로 정리 (중복 제거) 
        // 2. 신고당한 횟수 map으로 정리
        // 3. k 이상이면 메일 횟수 + 1
        
        for (int i = 0; i < report.length; i++) {
            String input = report[i];
            StringTokenizer st = new StringTokenizer(input);
            
            String reporter = st.nextToken();
            String reportedUser = st.nextToken();
            
            reportMap.computeIfAbsent(reporter, x -> new HashSet<>()).add(reportedUser);
        }
        
        for (int i = 0; i < id_list.length; i++) {
            String reporter = id_list[i];
            Set<String> reportedUserSet = reportMap.get(reporter);
            
            if (reportedUserSet == null || reportedUserSet.isEmpty()) continue;
            
            for (String reportedUser : reportedUserSet) {
                countMap.put(reportedUser, countMap.getOrDefault(reportedUser, 0) + 1);
            }
        }
        
        int[] answer = new int[id_list.length];
        
        for (int i = 0; i < id_list.length; i++) {
            String reportedUser = id_list[i];
            
            int count = countMap.getOrDefault(reportedUser, 0);
            
            if (k <= count) {
                for (int j = 0; j < id_list.length; j++) {
                    String reporter = id_list[j];
                    Set<String> reportedUserSet = reportMap.get(reporter);
            
                    if (reportedUserSet == null || reportedUserSet.isEmpty()) continue;
                    
                    if (reportedUserSet.contains(reportedUser)) {
                        answer[j]++;
                    }
                }
            }
        }
        
        return answer;
    }
}