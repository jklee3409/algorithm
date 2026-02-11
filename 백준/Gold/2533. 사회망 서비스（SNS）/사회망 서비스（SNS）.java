import java.io.*;
import java.util.*;

public class Main {
    static int n;
    static List<List<Integer>> graph = new ArrayList<>();
    static boolean[] visited;
    static int[][] dp; // dp[i][0]: i가 얼리 아답터가 아닐 때, dp[i][1]: i가 얼리 아답터일 때

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        n = Integer.parseInt(br.readLine());
        
        for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
        visited = new boolean[n + 1];
        dp = new int[n + 1][2];
        
        for (int i = 0; i < n - 1; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        dfs(1);
        
        System.out.println(Math.min(dp[1][0], dp[1][1]));
    }

    static void dfs(int curr) {
        visited[curr] = true;
        
        dp[curr][0] = 0; 
        dp[curr][1] = 1; 

        for (int next : graph.get(curr)) {
            if (!visited[next]) { 
                dfs(next); 
                
                dp[curr][0] += dp[next][1];
                
                dp[curr][1] += Math.min(dp[next][0], dp[next][1]);
            }
        }
    }
}