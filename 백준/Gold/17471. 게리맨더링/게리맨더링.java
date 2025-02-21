import java.io.*;
import java.util.*;

public class Main {
    static int N; // 구역 수
    static int[] people; // 각 구역의 인구 수
    static List<List<Integer>> graph = new ArrayList<>();
    static int minDifference = Integer.MAX_VALUE;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        N = Integer.parseInt(br.readLine());
        people = new int[N];

        // 인구 수 입력 및 그래프 초기화
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            people[i] = Integer.parseInt(st.nextToken());
            graph.add(new ArrayList<>());
        }

        // 그래프 입력
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            int count = Integer.parseInt(st.nextToken());

            for (int j = 0; j < count; j++) {
                int neighbor = Integer.parseInt(st.nextToken()) - 1; 
                graph.get(i).add(neighbor);
            }
        }

        // 두 선거구로 나누는 모든 경우 탐색 (부분 집합 생성)
        divideDistricts(0, new ArrayList<>(), new ArrayList<>());
        
        System.out.println(minDifference == Integer.MAX_VALUE ? -1 : minDifference);
    }

    // 구역을 두 그룹으로 나눈다
    private static void divideDistricts(int index, List<Integer> groupA, List<Integer> groupB) {
        if (index == N) { // 모든 구역을 분배한 경우
            if (!groupA.isEmpty() && !groupB.isEmpty()) { // 두 그룹이 비어 있지 않고 (적어도 하나의 구역을 가지고 있는 경우)
                if (isConnected(groupA) && isConnected(groupB)) { // 각 그룹의 구역들이 서로 연결되어 있다면
                    int diff = Math.abs(getPopulation(groupA) - getPopulation(groupB));
                    minDifference = Math.min(minDifference, diff);
                }
            }
            return;
        }

        // 현재 구역을 그룹 A에 넣기
        groupA.add(index);
        divideDistricts(index + 1, groupA, groupB);
        groupA.remove(groupA.size() - 1);

        // 현재 구역을 그룹 B에 넣기
        groupB.add(index);
        divideDistricts(index + 1, groupA, groupB);
        groupB.remove(groupB.size() - 1);
    }

    // 연결된 그룹인지 확인
    private static boolean isConnected(List<Integer> group) {
        
        boolean[] visited = new boolean[N];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(group.get(0)); // 첫 번째 구역에서 시작
        visited[group.get(0)] = true;

        int visitedCount = 1;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int neighbor : graph.get(current)) {
                if (!visited[neighbor] && group.contains(neighbor)) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                    visitedCount++;
                }
            }
        }

        // 방문한 구역 수와 그룹 크기가 같으면 연결된 그룹
        return visitedCount == group.size();
    }

    // 그룹의 인구 수 계산
    private static int getPopulation(List<Integer> group) {
        int result = 0;
        for (int idx : group) {
            result += people[idx];
        }
        return result;
    }
}
