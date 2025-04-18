import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    static int N, M;
    static int[][] city;
    static List<Chicken> chickenList = new ArrayList<>();
    static List<Chicken[]> chickenComb = new ArrayList<>();

    static class Chicken {
        int y, x;

        public Chicken(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken()); // 치킨집 최댓값
        city = new int[N][N];

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                city[i][j] = Integer.parseInt(st.nextToken());

                if (city[i][j] == 2) {
                    Chicken chicken = new Chicken(i, j);
                    chickenList.add(chicken);
                }
            }
        }

        simulate();
    }

    public static void simulate() {
        int min = Integer.MAX_VALUE; // 치킨 거리 최솟값

        Chicken[] chickens = new Chicken[M];
        comb(chickens, 0, 0);

        // 치킨집 조합별 치킨 거리 계산
        for (int i = 0; i < chickenComb.size(); i++) {
            int[][] tempCity = deepCopy();

            marking(tempCity, chickenComb.get(i)); // 임시 도시 -> 치킨집 조합 적용
            min = Math.min(min, getDiff(tempCity));
        }

        System.out.println(min);
    }

    public static void comb(Chicken[] chickens, int start, int depth) {
        // base camp
        if (depth == M) {
            chickenComb.add(chickens.clone()); // copy 해서 저장 (참조 문제)
            return;
        }

        for (int i = start; i < chickenList.size(); i++) {
            chickens[depth] = chickenList.get(i);

            comb(chickens, i + 1, depth + 1);
        }
    }

    public static void marking(int[][] temptCity, Chicken[] chickens) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                if (temptCity[i][j] == 2) temptCity[i][j] = 0; // 일단 모든 치킨집 초기화
            }
        }

        // 조합에 있는 치킨집만 다시 오픈
        for (int i = 0; i < M; i++) {
            int y = chickens[i].y;
            int x = chickens[i].x;

            temptCity[y][x] = 2;
        }
    }

    // 도시의 치킨 거리를 구하는 메서드
    public static int getDiff(int[][] tempCity) {
        int diffSum = 0; // 도시의 치킨거리

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (tempCity[i][j] == 0 || tempCity[i][j] == 2) continue;

                int min = Integer.MAX_VALUE; // 각각의 집에서 가장 가까운 치킨집과의 거리

                for (int k = 0; k < N; k++) {
                    for (int l = 0; l < N; l++) {

                        if (tempCity[k][l] == 2) {
                            int diff = Math.abs(i - k) + Math.abs(j - l);
                            min = Math.min(min, diff);
                        }
                    }
                }

                diffSum += min;
            }
        }

        return diffSum;
    }

    public static int[][] deepCopy() {
        int[][] temp = new int[N][];

        for (int i = 0; i < N; i++) {
            temp[i] = city[i].clone();
        }

        return temp;
    }
}
