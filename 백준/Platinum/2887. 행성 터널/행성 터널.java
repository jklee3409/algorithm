    import java.io.*;
    import java.util.*;

    public class Main {

        static int N;
        static int[] parent;
        static List<int[]> planetList = new ArrayList<>();
        static List<int[]> edgeList = new ArrayList<>();

        public static void main(String[] args) throws Exception{
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            N = Integer.parseInt(br.readLine());

            parent = new int[N + 1];
            for (int i = 1; i <= N; i++) {
                parent[i] = i;
            }

            for (int i = 1; i <= N; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());

                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int z = Integer.parseInt(st.nextToken());

                planetList.add(new int[]{x, y, z, i});
            }

            // 전체 간선 후보를 탐색하면 메모리 초과
            // 각 축을 기준으로 정렬하고 이웃한 행성만 간선 후보

            for (int i = 0; i <= 2; i++) {
                int fi = i;
                planetList.sort((a, b) -> Integer.compare(a[fi], b[fi]));
                findEdgeCandidates(i);
            }

            edgeList.sort((a, b) -> Integer.compare(a[2], b[2]));

            int cost = 0;
            int selected = 0;

            for (int[] edge : edgeList) {
                int a = edge[0];
                int b = edge[1];

                if (find(a) != find(b)) {
                    cost += edge[2];
                    union(a, b);
                    selected++;

                    if (selected == N - 1) break;
                }
            }

            System.out.println(cost);
        }

        private static void findEdgeCandidates(int n) {
            for (int i = 0; i < N - 1; i++) {
                int[] planetA = planetList.get(i);
                int[] planetB = planetList.get(i + 1);

                int a = planetA[n];
                int b = planetB[n];

                int distance = Math.abs(a - b);

                edgeList.add(new int[]{planetA[3], planetB[3], distance});
            }
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