import java.util.*;

class Solution {
    
    static class Edge {
        int to, cost;
        boolean isOriginal;
        int trapState;
        
        public Edge(int to, int cost, boolean isOriginal) {
            this.to = to;
            this.cost = cost;
            this.isOriginal = isOriginal;
        }
        
        // PQ에 넣을 때 
        public Edge(int to, int cost, int trapState) {
            this.to = to;
            this.cost = cost;
            this.trapState = trapState;
        }
    }
    
    static int N;
    static List<Edge>[] graph;
    static Set<Integer> trapSet = new HashSet<>();
    static int[] trapIndex;
    
    public int solution(int n, int start, int end, int[][] roads, int[] traps) {
        Solution.N = n;
        Solution.trapSet.clear();
        
        graph = new List[n + 1];
        
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int i = 0; i < roads.length; i++) {
            int a = roads[i][0];
            int b = roads[i][1];
            int c = roads[i][2];
            
            graph[a].add(new Edge(b, c, true));
            graph[b].add(new Edge(a, c, false));
        }
        
        trapIndex = new int[n + 1];
        Arrays.fill(trapIndex, -1);
        
        for (int i = 0; i < traps.length; i++) {
            trapSet.add(traps[i]);
            trapIndex[traps[i]] = i;
        }
        
        return dijkstra(start, end, traps.length);
    }
    
    static int dijkstra(int start, int end, int trapCount) {
        int[][] dist = new int[N + 1][1 << trapCount];
        for (int i = 1; i <= N; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
        
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> {
            return Integer.compare(a.cost, b.cost);
        });
        
        dist[start][0] = 0;
        pq.offer(new Edge(start, 0, 0));
        
        while (!pq.isEmpty()) {
            
            Edge cur = pq.poll();
            
            if (dist[cur.to][cur.trapState] < cur.cost) continue;
            
            if (cur.to == end) return cur.cost;
            
            for (Edge next : graph[cur.to]) {
                
                // 현재 상태에서 해당 간선을 탈 수 있는지 
                if (!isPossibleRoad(cur.to, next, cur.trapState)) continue;
                
                int nextState = cur.trapState;
                
                // 다음 노드가 trap이면 상태 반전
                if (trapSet.contains(next.to)) {
                    int index = trapIndex[next.to];
                    nextState ^= (1 << index);
                }
                
                int nextCost = cur.cost + next.cost;
                
                if (nextCost < dist[next.to][nextState]) {
                    dist[next.to][nextState] = nextCost;
                    pq.offer(new Edge(next.to, nextCost, nextState));
                }
            }
        }
        
        return -1;
    }
    
    static boolean isPossibleRoad(int curNode, Edge next, int trapState) {
        
        boolean curTrapOn = false;
        boolean nextTrapOn = false;
        
        if (trapSet.contains(curNode)) {
            int index = trapIndex[curNode];
            curTrapOn = (trapState & (1 << index)) != 0;
        }
        
        if (trapSet.contains(next.to)) {
            int index = trapIndex[next.to];
            nextTrapOn = (trapState & (1 << index)) != 0;
        }
        
        // 둘 중 하나만 활성화되어 있으면 방향이 뒤집힘
        boolean reversed = curTrapOn ^ nextTrapOn;
        
        if (!reversed) return next.isOriginal;
        else return !next.isOriginal;
    }
}