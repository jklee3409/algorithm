import java.io.*;
import java.util.*;

public class Main {

    static int N;
    static int[][] classroom; // 교실 좌석 배치도
    static Map<Integer, Set<Integer>> favoriteStudentsMap; // 학생별 좋아하는 학생 목록
    static int[] studentOrder; // 자리를 정하는 학생 순서

    static int[] dr = {-1, 1, 0, 0};
    static int[] dc = {0, 0, -1, 1};

    static class Seat implements Comparable<Seat> {
        int r, c, likeCount, emptyCount;

        public Seat(int r, int c, int likeCount, int emptyCount) {
            this.r = r;
            this.c = c;
            this.likeCount = likeCount;
            this.emptyCount = emptyCount;
        }

        @Override
        public int compareTo(Seat other) {
            // 1. 인접한 좋아하는 학생 수 (내림차순)
            if (this.likeCount != other.likeCount) {
                return other.likeCount - this.likeCount;
            }
            // 2. 인접한 빈칸 수 (내림차순)
            if (this.emptyCount != other.emptyCount) {
                return other.emptyCount - this.emptyCount;
            }
            // 3. 행 번호 (오름차순)
            if (this.r != other.r) {
                return this.r - other.r;
            }
            // 4. 열 번호 (오름차순)
            return this.c - other.c;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        classroom = new int[N][N];
        favoriteStudentsMap = new HashMap<>();
        studentOrder = new int[N * N];

        for (int i = 0; i < N * N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int studentId = Integer.parseInt(st.nextToken());
            studentOrder[i] = studentId;

            Set<Integer> favs = new HashSet<>();
            for (int j = 0; j < 4; j++) {
                favs.add(Integer.parseInt(st.nextToken()));
            }
            favoriteStudentsMap.put(studentId, favs);
        }

        placeStudents();

        int totalSatisfaction = calculateSatisfaction();

        System.out.println(totalSatisfaction);
    }

    private static void placeStudents() {
        for (int studentId : studentOrder) {
            Seat bestSeat = findBestSeat(studentId);
            classroom[bestSeat.r][bestSeat.c] = studentId;
        }
    }

    /**
     * 특정 학생을 위한 최적의 자리를 찾는 메소드
     * @param studentId 현재 자리를 찾고 있는 학생
     * @return 가장 좋은 자리 정보
     */
    private static Seat findBestSeat(int studentId) {
        List<Seat> candidateSeats = new ArrayList<>();
        Set<Integer> favs = favoriteStudentsMap.get(studentId);

        // 1. 교실의 모든 칸을 순회하며 후보 자리를 찾는다.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                // 이미 다른 학생이 앉아있으면 건너뛴다.
                if (classroom[i][j] > 0) {
                    continue;
                }

                int likeCount = 0;
                int emptyCount = 0;

                // 2. 현재 칸의 상하좌우 탐색.
                for (int d = 0; d < 4; d++) {
                    int nr = i + dr[d];
                    int nc = j + dc[d];

                    // 교실 범위를 벗어나는지 확인
                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                        continue;
                    }

                    // 인접 칸에 좋아하는 학생이 있는지 확인
                    if (favs.contains(classroom[nr][nc])) {
                        likeCount++;
                    }
                    // 인접 칸이 비어있는지 확인
                    if (classroom[nr][nc] == 0) {
                        emptyCount++;
                    }
                }
                // 3. 후보 자리 정보를 리스트에 추가한다.
                candidateSeats.add(new Seat(i, j, likeCount, emptyCount));
            }
        }

        // 4. 정의된 정렬 기준(compareTo)에 따라 최적의 자리를 찾는다.
        Collections.sort(candidateSeats);

        // 5. 정렬된 리스트의 첫 번째 자리가 최적의 자리이다.
        return candidateSeats.get(0);
    }

    /**
     * 모든 학생의 만족도 총합을 계산하는 메소드
     * @return 만족도 총합
     */
    private static int calculateSatisfaction() {
        int totalSatisfaction = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int studentId = classroom[i][j];
                Set<Integer> favs = favoriteStudentsMap.get(studentId);
                int count = 0;

                // 인접 칸의 좋아하는 학생 수를 센다.
                for (int d = 0; d < 4; d++) {
                    int nr = i + dr[d];
                    int nc = j + dc[d];

                    if (nr < 0 || nr >= N || nc < 0 || nc >= N) {
                        continue;
                    }

                    if (favs.contains(classroom[nr][nc])) {
                        count++;
                    }
                }

                // 만족도를 계산하여 더한다.
                if (count == 1) {
                    totalSatisfaction += 1;
                } else if (count == 2) {
                    totalSatisfaction += 10;
                } else if (count == 3) {
                    totalSatisfaction += 100;
                } else if (count == 4) {
                    totalSatisfaction += 1000;
                }
            }
        }
        return totalSatisfaction;
    }
}
