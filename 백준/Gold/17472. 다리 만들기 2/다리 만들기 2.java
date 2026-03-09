import java.io.*;
import java.util.*;

public class Main {
    static int N, M;
    static int[][] map;
    static List<int[]> edgeList = new ArrayList<>();

    static int[] parent;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new int[N][M];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        simulate();
    }

    // 1. 섬 라벨링
    // 2. 다리 놓기 (다리 후보 리스트 생성)
    // 3. MST 찾기

    private static void simulate() {
        boolean[][] visited = new boolean[N][M];
        int label = 1;

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                if (visited[y][x] || map[y][x] == 0) continue;

                labeling(y, x, label, visited);
                label++;
            }
        }

        findBridges();
        kruskal(label - 1);
    }

    static int[] dy = {-1, 1, 0, 0};
    static int[] dx = {0, 0, -1, 1};

    private static void labeling(int startY, int startX, int label, boolean[][] visited) {
        Queue<int[]> queue = new ArrayDeque<>();

        queue.offer(new int[] {startY, startX});
        visited[startY][startX] = true;
        map[startY][startX] = label;

        while (!queue.isEmpty()) {
            int[] n = queue.poll();
            int y = n[0];
            int x = n[1];

            for (int i = 0; i < 4; i++) {
                int ny = y + dy[i];
                int nx = x + dx[i];

                if (ny < 0 || nx < 0 || ny >= N || nx >= M) continue;
                if (visited[ny][nx] || map[ny][nx] == 0) continue;

                queue.offer(new int[]{ny, nx});
                visited[ny][nx] = true;
                map[ny][nx] = label;
            }
        }
    }

    private static void findBridges() {
        // 다리의 조건
        // 1. 방향이 중간에 바뀌면 안됨
        // 2. 길이는 2 이상
        
        // 간선 후보를 찾는 방법
        // 모든 지점에서 4방향 직진 탐색
        // 다른 섬을 만나고, 길이가 2 이상이라면 통과
        // 범위를 벗어나거나, 같은 섬을 만나면 안됨

        for (int y = 0; y < N; y++) {
            for (int x = 0; x < M; x++) {
                int from = map[y][x];
                if (from == 0) continue;

                for (int d = 0; d < 4; d++) {
                    int distant = 0;

                    int ny = y + dy[d];
                    int nx = x + dx[d];

                    while (true) {
                        if (ny < 0 || nx < 0 || ny >= N || nx >= M) break;

                        int next = map[ny][nx];

                        if (next == 0) { // 바다면 계속 탐색
                            ny = ny + dy[d];
                            nx = nx + dx[d];
                            distant++;

                        } else if (next == from) { // 같은 섬이면 탐색 중지
                            break;

                        } else { // 다른 섬이고

                            if (distant >= 2) { // 다리 길이가 2 이상이면, 간선 후보 추가하고 탐색 중지
                                edgeList.add(new int[]{from, next, distant});
                                break;

                            } else { // 다리 길이가 2 이하면 그대로 탐색 중지
                                break;
                            }
                        }

                    }
                }
            }
        }
    }

    private static void kruskal(int label) {
        // 간선 길이 순으로 정렬
        // Union - Find
        parent = new int[label + 1];
        for (int i = 1; i <= label; i++) {
            parent[i] = i;
        }

        edgeList.sort((a, b) -> a[2] - b[2]);

        int cost = 0;
        int selectedEdge = 0;
        for (int[] edge : edgeList) {
            int a = edge[0];
            int b = edge[1];

            if (find(a) != find(b)) {
                union(a, b);
                cost += edge[2];
                selectedEdge++;
            }
        }

        System.out.println(selectedEdge == label - 1 ? cost : -1);
    }

    private static int find(int x) {
        if (parent[x] == x) return x;
        return parent[x] = find(parent[x]);
    }

    private static void union(int a, int b) {
        int pa = find(a);
        int pb = find(b);

        if (pa != pb) parent[pb] = pa;
    }
}