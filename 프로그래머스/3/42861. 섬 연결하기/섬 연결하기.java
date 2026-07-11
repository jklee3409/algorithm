import java.util.*;

class Solution {
    
    class Edge implements Comparable<Edge> {
        int a, b, cost;
        
        public Edge (int a, int b, int cost) {
            this.a = a;
            this.b = b;
            this.cost = cost;
        }
        
        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
    
    static int[] parent;
    
    public int solution(int n, int[][] costs) {
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        parent = new int[n + 1];
        
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
        }
        
        for (int i = 0; i < costs.length; i++) {
            int a = costs[i][0];
            int b = costs[i][1];
            int cost = costs[i][2];
            
            Edge edge = new Edge(a, b, cost);
            
            pq.add(edge);
        }
        
        int answer = 0, count = 0;
        
        while (!pq.isEmpty()) {
            Edge edge = pq.poll();
            
            if (find(edge.a) == find(edge.b)) continue;
            
            union(edge.a, edge.b);
            answer += edge.cost;
            count++;
            
            if (count == n - 1) break;
        }
        
        return answer;
    }
    
    int find(int x) {
        if (parent[x] == x) return x;
        return find(parent[x]);
    }
    
    void union (int a, int b) {
        int pa = find(a);
        int pb = find(b);
        
        if (pa != pb) parent[pb] = pa;
    }
}