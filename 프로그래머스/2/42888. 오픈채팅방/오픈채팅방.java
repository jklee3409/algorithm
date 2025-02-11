import java.util.*;

class Solution {
    public String[] solution(String[] record) {
        HashMap<String, String> nicknameMap = new HashMap<>();
        List<String> result = new ArrayList<>();

        for (String str : record) {
            StringTokenizer st = new StringTokenizer(str);
            String state = st.nextToken();
            String uid = st.nextToken();

            if (!state.equals("Leave")) {
                String nickname = st.nextToken();
                nicknameMap.put(uid, nickname);
            }
        }

        for (String str : record) {
            StringTokenizer st = new StringTokenizer(str);
            String state = st.nextToken();
            String uid = st.nextToken();

            if (state.equals("Enter")) {
                result.add(nicknameMap.get(uid) + "님이 들어왔습니다.");
            } else if (state.equals("Leave")) {
                result.add(nicknameMap.get(uid) + "님이 나갔습니다.");
            }
        }

        return result.toArray(new String[0]);
    }
}
