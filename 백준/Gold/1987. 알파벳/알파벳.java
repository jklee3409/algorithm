import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.StringTokenizer;
public class Main {
    static int w, h;
    static char[][] map;
    static int ans = 1;
    static int[][] visit;
    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    
    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringBuilder sb = new StringBuilder();
        
        StringTokenizer st;
        st = new StringTokenizer(br.readLine());
        h = Integer.parseInt(st.nextToken());
        w = Integer.parseInt(st.nextToken());
        map = new char[h][w];
        visit = new int[h][w];
        for (int y = 0; y < h; y++) {
            
            map[y] = br.readLine().toCharArray();
        }
        
        // 시작 지점 (0,0)의 알파벳을 방문 처리한 상태에서 DFS 시작
        dfs(1, 1 << (map[0][0] - 'A'), 0, 0);
        sb.append(ans).append('\n');
        bw.write(sb.toString());
        bw.flush();
        bw.close();
        br.close();
    }
    
    public static void dfs(int level, int visited, int x, int y) {
        ans = Math.max(ans, level);
        visit[y][x] = visited; // 현재 좌표와 방문상태 기록
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            // 범위 밖이거나 이미 방문한 알파벳인 경우, 혹은 같은 상태로 방문했던 경우는 패스
            if (nx < 0 || nx >= w || ny < 0 || ny >= h) continue;
            if ((visited & (1 << (map[ny][nx] - 'A'))) != 0) continue;
            if ((visited | (1 << (map[ny][nx] - 'A'))) == visit[ny][nx]) continue;
            // 방문할 수 있는 칸이면 DFS 진행
            dfs(level + 1, visited | (1 << (map[ny][nx] - 'A')), nx, ny);
        }
    }
}