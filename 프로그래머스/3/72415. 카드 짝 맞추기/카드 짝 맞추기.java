import java.util.*;

class Solution {
    
    private int[] dy = {-1, 1, 0, 0};
    private int[] dx = {0, 0, -1, 1};
    
    private int answer = Integer.MAX_VALUE;
    
    private int n;
    
    public int solution(int[][] board, int r, int c) {
        Set<Integer> cardSet = new HashSet<>();
        
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == 0) continue;
                cardSet.add(board[i][j]);
            }
        }
        
        this.n = cardSet.size();
        
        dfs(0, 0, r, c, cardSet, board);
        
        return answer;
    }
    
    void dfs(int depth, int totalDist, int startY, int startX, Set<Integer> cardSet, int[][] board) {
        if (depth == n) {
            answer = Math.min(answer, totalDist);
            return;
        }
        
        if (totalDist >= answer) return;
        
        for (int card = 1; card <= 6; card++) {
            if (!cardSet.contains(card)) continue;
            
            int[][] positions = findCardPositions(card, board);
            
            int firstY = positions[0][0];
            int firstX = positions[0][1];
            int secondY = positions[1][0];
            int secondX = positions[1][1];
            
            int firstOrderDist =
                bfs(startY, startX, firstY, firstX, board) 
                + bfs(firstY, firstX, secondY, secondX, board)
                + 2;
            
            int secondOrderDist = 
                bfs(startY, startX, secondY, secondX, board)
                + bfs(secondY, secondX, firstY, firstX, board)
                + 2;
            
            Set<Integer> tempCardSet = new HashSet<>(cardSet);
            tempCardSet.remove(card);
            
            board[firstY][firstX] = 0;
            board[secondY][secondX] = 0;
            
            dfs(depth + 1, totalDist + firstOrderDist, secondY, secondX, tempCardSet, board);
            
            dfs(depth + 1, totalDist + secondOrderDist, firstY, firstX, tempCardSet, board);
            
            board[firstY][firstX] = card;
            board[secondY][secondX] = card;
        }
    }
    
    int[][] findCardPositions(int targetCard, int[][] board) {
        int[][] positions = new int[2][2];
  
        int index = 0;
        
        for(int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (board[y][x] == targetCard) {
                    positions[index][0] = y;
                    positions[index][1] = x;
                    index++;
                    
                    if (index > 1) break;
                }
            }
        }
        
        return positions;
    }
    
    int bfs(int startY, int startX, int targetY, int targetX, int[][] board) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[4][4];
        
        q.offer(new int[]{startY, startX, 0});
        visited[startY][startX] = true;
        
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            
            int y = cur[0];
            int x = cur[1];
            int dist = cur[2];
            
            if (y == targetY && x == targetX) return dist;
            
            for (int d = 0; d < 4; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];
                
                if (isInRange(ny, nx) && !visited[ny][nx]) {
                    q.offer(new int[]{ny, nx, dist + 1});
                    visited[ny][nx] = true;
                }
                
                int[] ctrlPositions = ctrlMove(y, x, d, board);
                int ctrlY = ctrlPositions[0];
                int ctrlX = ctrlPositions[1];
                
                if (!visited[ctrlY][ctrlX]) {
                    q.offer(new int[]{ctrlY, ctrlX, dist + 1});
                    visited[ctrlY][ctrlX] = true;
                }
            }
        }
        
        return -1;
    }
    
    // ctrlMove
    int[] ctrlMove(int y, int x, int direction, int[][] board) {
        int currentY = y;
        int currentX = x;
        
        while(true) {
            int nextY = currentY + dy[direction];
            int nextX = currentX + dx[direction];
            
            if(!isInRange(nextY, nextX)) return new int[]{currentY, currentX};
            
            currentY = nextY;
            currentX = nextX;
            
            if(board[currentY][currentX] != 0) return new int[]{currentY, currentX};
        }
    }
    
    boolean isInRange(int y, int x) {
        return y >= 0 && x >= 0 && y < 4 && x < 4;
    }
}