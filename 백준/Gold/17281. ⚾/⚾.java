import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int N;
    static int[][] players;
    static int maxScore = 0;
    static int[] order = new int[9]; // 타자 순서
    static boolean[] selected = new boolean[9]; // 선택된 타자 여부

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        N = Integer.parseInt(br.readLine());
        players = new int[N][9];

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int j = 0; j < 9; j++) {
                players[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 0번 선수는 항상 4번 타자(3번 인덱스)로 고정
        order[3] = 0;
        selected[0] = true;

        // 나머지 선수들의 순서를 모두 조합
        permutation(0);

        System.out.println(maxScore);
    }

    // 가능한 모든 타자 순서 조합 생성
    public static void permutation(int depth) {
        if (depth == 9) {
            // 모든 타자 순서가 결정되면 점수 계산
            int score = getScore(order);
            maxScore = Math.max(maxScore, score);
            return;
        }

        // 4번 타자는 이미 고정되어 있으므로 건너뜀
        if (depth == 3) {
            permutation(depth + 1);
            return;
        }

        for (int i = 1; i < 9; i++) {
            if (!selected[i]) {
                selected[i] = true;
                order[depth] = i;
                permutation(depth + 1);
                selected[i] = false;
            }
        }
    }

    public static int getScore(int[] battingOrder) {
        boolean[] runners = new boolean[4]; // 1, 2, 3 루에 주자가 있는지
        int outCount = 0; // 아웃 카운트
        int inning = 0; // 현재 진행 중인 이닝
        int score = 0; // 점수
        int orderIndex = 0; // 타자 순서 인덱스

        while (inning < N) { // 모든 이닝이 끝날 때까지 반복
            int playerNum = battingOrder[orderIndex]; // 현재 타자 번호
            int cur = players[inning][playerNum]; // 현재 타자의 결과

            if (cur == 0) { // 아웃인 경우
                outCount++;

                if (outCount == 3) { // 3아웃이면 이닝 종료
                    // 이닝 종료 시 초기화
                    outCount = 0;
                    runners = new boolean[4]; // 주자 모두 클리어
                    inning++; // 다음 이닝으로

                    if (inning == N) { // 모든 이닝이 끝나면 종료
                        break;
                    }
                }
            } else if (cur == 1) { // 안타(1루타)인 경우

                if (runners[3]) { // 3루에 주자가 있으면 홈으로
                    score++;
                    runners[3] = false;
                }

                if (runners[2]) { // 2루에 주자가 있으면 3루로
                    runners[3] = true;
                    runners[2] = false;
                }

                if (runners[1]) { // 1루에 주자가 있으면 2루로
                    runners[2] = true;
                    runners[1] = false;
                }

                runners[1] = true; // 타자는 1루로

            } else if (cur == 2) { // 2루타인 경우

                if (runners[3]) { // 3루에 주자가 있으면 홈으로
                    score++;
                    runners[3] = false;
                }

                if (runners[2]) { // 2루에 주자가 있으면 홈으로
                    score++;
                    runners[2] = false;
                }

                if (runners[1]) { // 1루에 주자가 있으면 3루로
                    runners[3] = true;
                    runners[1] = false;
                }

                runners[2] = true; // 타자는 2루로

            } else if (cur == 3) { // 3루타인 경우

                if (runners[3]) { // 3루에 주자가 있으면 홈으로
                    score++;
                    runners[3] = false;
                }

                if (runners[2]) { // 2루에 주자가 있으면 홈으로
                    score++;
                    runners[2] = false;
                }

                if (runners[1]) { // 1루에 주자가 있으면 홈으로
                    score++;
                    runners[1] = false;
                }

                runners[3] = true; // 타자는 3루로

            } else if (cur == 4) { // 홈런인 경우

                // 모든 주자와 타자가 홈으로 들어옴
                for (int j = 1; j <= 3; j++) {
                    if (runners[j]) {
                        score++;
                        runners[j] = false;
                    }
                }

                score++; // 타자도 홈으로
            }

            // 다음 타자로
            orderIndex = (orderIndex + 1) % 9;
        }

        return score;
    }
}
