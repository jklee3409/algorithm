import java.util.*;

class Solution {
    
    List<List<int[]>> blanks = new ArrayList<>();
    List<List<int[]>> puzzles = new ArrayList<>();
    
    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};
    
    public int solution(int[][] game_board, int[][] table) {
        int len = game_board.length;
        
        // 빈 공간 찾기
        boolean[][] visited = new boolean[len][len];
        
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (visited[i][j] || game_board[i][j] == 1) continue;
                
                List<int[]> blank = bfs(game_board, i, j, visited, true);
                normalize(blank);
                sortShape(blank);
                blanks.add(blank);
            }
        }
        
        // 퍼즐 찾기
        visited = new boolean[len][len];
        
        for (int i = 0; i < len; i++) {
            for (int j = 0; j < len; j++) {
                if (visited[i][j] || table[i][j] == 0) continue;
                
                List<int[]> puzzle = bfs(table, i, j, visited, false);
                normalize(puzzle);
                sortShape(puzzle);
                puzzles.add(puzzle);
            }
        }
        
        boolean[] used = new boolean[puzzles.size()];
        
        int answer = 0;
        
        // blank -> puzzle 하나씩 회전하면서 비교
        for (List<int[]> blank : blanks) {
            boolean matched = false;
            
            for (int puzzleId = 0; puzzleId < puzzles.size(); puzzleId++) {
                if (used[puzzleId]) continue;

                List<int[]> puzzle = puzzles.get(puzzleId);

                for (int i = 0; i < 4; i++) {
                    rotate(puzzle);
                    normalize(puzzle);
                    sortShape(puzzle);

                    if (isSame(blank, puzzle)) {
                        used[puzzleId] = true;
                        answer += puzzle.size();
                        matched = true;
                        break;
                    }
                }

                if (matched) break;
            }
        }
        
        return answer;
    }
    
    List<int[]> bfs(int[][] map, int startY, int startX, boolean[][] visited, boolean isBoard) {
        Queue<int[]> q = new ArrayDeque<>();
        List<int[]> shape = new ArrayList<>();
        
        q.offer(new int[]{startY, startX});
        visited[startY][startX] = true;
        
        shape.add(new int[]{startY, startX});
        
        int skip = isBoard ? 1 : 0; 
        
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            
            for (int d = 0; d < 4; d++) {
                int ny = cur[0] + dy[d];
                int nx = cur[1] + dx[d];
                
                if (ny < 0 || nx < 0 || ny >= map.length || nx >= map.length) continue;
                if (visited[ny][nx] || map[ny][nx] == skip) continue; 
                
                shape.add(new int[]{ny, nx});
                
                q.offer(new int[]{ny, nx});
                visited[ny][nx] = true;
            }
        }
        
        return shape;
    }
    
    void normalize(List<int[]> shape) {
        int minY = Integer.MAX_VALUE, minX = Integer.MAX_VALUE;
        
        for(int[] p : shape) {
            minY = Math.min(minY, p[0]);
            minX = Math.min(minX, p[1]);
        }
        
        for(int[] p : shape) {
            p[0] -= minY;
            p[1] -= minX;
        }
    }
    
    void sortShape(List<int[]> shape) {
        
        Collections.sort(shape, (a, b) -> {
            if (a[0] == b[0]) return a[1] - b[1];
            return a[0] - b[0];
        });
    }
    
    void rotate(List<int[]> shape) {
        int size = 0, maxY = 0, maxX = 0;
        
        for (int[] p : shape) {
            maxY = Math.max(maxY, p[0]);
            maxX = Math.max(maxX, p[1]);
        }
        
        int max = Math.max(maxY, maxX);
        size = max + 1;
        
        for(int[] p : shape) {
            int y = p[0];
            int x = p[1];
            
            p[0] = x;
            p[1] = size - 1 - y;
        }
    }
    
    boolean isSame(List<int[]> a, List<int[]> b) {
        if (a.size() != b.size()) return false;
        
        for (int i = 0; i < a.size(); i++) {
            int[] a1 = a.get(i);
            int[] b1 = b.get(i);
            
            if (a1[0] != b1[0] || a1[1] != b1[1]) return false;
        }
        
        return true;
    }
}