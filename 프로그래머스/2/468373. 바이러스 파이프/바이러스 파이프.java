import java.util.*;

class Solution {
    
    class Node {
        int n, type;
        
        public Node(int n, int type) {
            this.n = n;
            this.type = type;
        }
    }
    
    static List<Node>[] graph;

    static int answer = Integer.MIN_VALUE;
    
    public int solution(int n, int infection, int[][] edges, int k) {
        graph = new ArrayList[n + 1];
        boolean[] infected = new boolean[n + 1];
        
        infected[infection] = true;
        
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int i = 0; i < edges.length; i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            int type = edges[i][2];
            
            graph[a].add(new Node(b, type));
            graph[b].add(new Node(a, type));
        }
        
        dfs(0, k, infected);
        
        return answer;
    }
    
    void dfs(int depth, int k, boolean[] infected) {
        if (depth == k) {
            int count = 0;
            
            for (boolean isInfect : infected) {
                if (isInfect) count++;
            }
            
            answer = Math.max(answer, count);
            
            return;
        }
        
        for (int type = 1; type <= 3; type++) {
            boolean[] next = infected.clone();
            
            spread(type, next);
            
            dfs(depth + 1, k, next);
        }
    }
    
    void spread(int openType, boolean[] infected) {
        Queue<Integer> q = new ArrayDeque<>();

        for (int i = 1; i < infected.length; i++) {
            if (infected[i]) q.offer(i);
        }

        while (!q.isEmpty()) {
            int cur = q.poll();

            for (Node next : graph[cur]) {
                if (next.type != openType) continue;
                if (infected[next.n]) continue;

                infected[next.n] = true;
                q.offer(next.n);
            }
        }
    }
}