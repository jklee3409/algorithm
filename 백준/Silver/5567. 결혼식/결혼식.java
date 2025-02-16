import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;


public class Main {
    static boolean[] visited;
    static List<List<Integer>> friends = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine()); // 동기의 수
        int m = Integer.parseInt(br.readLine()); // 친구 관계의 수

        for (int i = 0; i <= n; i++) {
            friends.add(new ArrayList<>());
        }

        // 친구 관계 입력 받기
        for (int i = 0; i < m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());

            friends.get(a).add(b);
            friends.get(b).add(a);
        }

        System.out.println(getInviteCount(n, friends));
    }

    private static int getInviteCount(int n, List<List<Integer>> friends) {
        Queue<Integer> queue = new LinkedList<>();
        visited = new boolean[n + 1]; // 방문 여부 확인 배열
        int inviteCount = 0; // 초대할 사람 수

        queue.offer(1); // 상근이부터 시작
        visited[1] = true;
        int level = 0; // BFS에서 깊이 (0: 상근이, 1: 친구, 2: 친구의 친구)

        while (!queue.isEmpty() && level < 2) { // 친구의 친구까지만 탐색
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                int current = queue.poll();

                for (int friend : friends.get(current)) {
                    if (!visited[friend]) {
                        visited[friend] = true;
                        queue.offer(friend);
                        inviteCount++;
                    }
                }
            }

            level++; // 탐색 깊이 증가
        }

        return inviteCount; // 초대할 친구의 수 반환
    }
}