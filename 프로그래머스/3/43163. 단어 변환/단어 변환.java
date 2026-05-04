import java.util.*;

class Solution {
    public int solution(String begin, String target, String[] words) {
        int answer = bfs(begin, target, words);
        return answer;
    }
    
    int bfs(String begin, String target, String[] words) {
        Queue<String> q = new ArrayDeque<>();
        boolean[] visited = new boolean[words.length];
        
        q.offer(begin);
        int level = 0;
        
        while(!q.isEmpty()) {
            int size = q.size();
            
            for (int i = 0; i < size; i++) {
                String cur = q.poll();
                
                if (cur.equals(target)) return level;
                
                for (int j = 0; j < words.length; j++) {
                    if (visited[j] || !canChange(cur, words[j])) continue;
                    
                    q.offer(words[j]);
                    visited[j] = true;
                }
            }
            level++;
        }
        
        return 0;
    }
    
    boolean canChange(String a, String b) {
        int diff = 0;
        
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) diff++;
        }
        
        return diff == 1;
    }
}