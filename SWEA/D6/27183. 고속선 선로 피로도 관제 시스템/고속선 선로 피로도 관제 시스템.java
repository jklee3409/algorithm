import java.io.*;

public class Solution {

    static int[] tree;
    static int[] lazyAdd;
    static int[] lazyReset;
    static boolean[] hasReset;

    static int len;

    /*=========================================================
      여기서부터 5개 메서드만 구현하시오.
      main 및 입출력 부분은 수정하지 않는 것을 권장한다.

      init      : 각 테스트 케이스 시작 시 1회 호출. 전역 자료 구조 초기화.
      addStress : 구간 [l,r] 의 모든 구간 피로도를 w 만큼 증가(range add).
      repair    : 구간 [l,r] 을 v 로 재설정(range assign, v=0 가능).
      getPeak   : 구간 [l,r] 의 최대 피로도 반환(range max).
      findRisk  : 구간 [l,r] 에서 값 >= x 인 가장 왼쪽 인덱스 반환, 없으면 -1.
    =========================================================*/

    static void init(int N, int[] fatigue) {
        // TODO: fatigue[0..N-1] = 초기 피로도. 전역 자료 구조를 반드시 초기화할 것.
        tree = new int[N * 4];
        lazyAdd = new int[N * 4];
        lazyReset = new int[N * 4];
        hasReset = new boolean[N * 4];

        len = N;

        build(1, 0, len - 1, fatigue);
    }

    static int build(int node, int start, int end, int[] fatigue) {
        if (start == end) {
            return tree[node] = fatigue[start];
        }

        int mid = start + (end - start) / 2;

        int leftMax = build(node * 2, start, mid, fatigue);
        int rightMax = build(node * 2 + 1, mid + 1, end, fatigue);

        return tree[node] = Math.max(leftMax, rightMax);
    }

    static void addStress(int l, int r, int w) {
        // TODO: 구간 [l,r] 에 피로도 w 누적
        increase(1, 0, len - 1, l, r, w);
    }

    static void increase(int node, int start, int end, int left, int right, int w) {
        if (right < start || end < left) return;

        if (left <= start && end <= right) {
            tree[node] += w;
            lazyAdd[node] += w;
            return;
        }

        push(node, start, end);

        int mid = start + (end - start) / 2;

        increase(node * 2, start, mid, left, right, w);
        increase(node * 2 + 1, mid + 1, end, left, right, w);

        tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
    }

    static void repair(int l, int r, int v) {
        // TODO: 구간 [l,r] 을 v 로 재설정
        reset(1, 0, len - 1, l, r, v);
    }

    static void reset(int node, int start, int end, int left, int right, int v) {
        if (right < start || end < left) return;

        if (left <= start && end <= right) {
            tree[node] = v;

            lazyReset[node] = v;
            hasReset[node] = true;

            lazyAdd[node] = 0;

            return;
        }

        push(node, start, end);

        int mid = start + (end - start) / 2;

        reset(node * 2, start, mid, left, right, v);
        reset(node * 2 + 1, mid + 1, end, left, right, v);

        tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);
    }

    static int getPeak(int l, int r) {
        // TODO: 구간 [l,r] 의 최대 피로도 반환
        return query(1, 0, len - 1, l, r);
    }

    static int query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) return Integer.MIN_VALUE;

        if (left <= start && end <= right) return tree[node];

        push(node, start, end);

        int mid = start + (end - start) / 2;

        int leftMax = query(node * 2, start, mid, left, right);
        int rightMax = query(node * 2 + 1, mid + 1, end, left, right);

        return Math.max(leftMax, rightMax);
    }

    static int findRisk(int l, int r, int x) {
        // TODO: 구간 [l,r] 에서 값 >= x 인 최소 인덱스 반환, 없으면 -1
        int answer = findLeast(1, 0, len - 1, l, r, x);
        return answer == Integer.MAX_VALUE ? -1 : answer;
    }

    static int findLeast(int node, int start, int end, int left, int right, int x) {
        if (right < start || end < left) return Integer.MAX_VALUE;

        if (tree[node] < x) return Integer.MAX_VALUE;

        if (start == end) return start;

        push(node, start, end);

        int mid = start + (end - start) / 2;

        int leftIdx = findLeast(node * 2, start, mid, left, right, x);

        if (leftIdx != Integer.MAX_VALUE) return leftIdx;

        return findLeast(node * 2 + 1, mid + 1, end, left, right, x);
    }

    static void push(int node, int start, int end) {

        if (hasReset[node]) {
            if (start == end) {
                hasReset[node] = false;
                return;
            }

            int value = lazyReset[node];

            tree[node * 2] = value;
            tree[node * 2 + 1] = value;

            lazyReset[node * 2] = value;
            lazyReset[node * 2 + 1] = value;
            
            lazyAdd[node * 2] = 0;
            lazyAdd[node * 2 + 1] = 0;

            hasReset[node * 2] = true;
            hasReset[node * 2 + 1] = true;

            hasReset[node] = false;
        }

        if (lazyAdd[node] != 0) {
            if (start == end) {
                lazyAdd[node] = 0;
                return;
            }

            int value = lazyAdd[node];

            tree[node * 2] += value;
            tree[node * 2 + 1] += value;

            tree[node] = Math.max(tree[node * 2], tree[node * 2 + 1]);

            lazyAdd[node * 2] += value;
            lazyAdd[node * 2 + 1] += value;

            lazyAdd[node] = 0;
        }
    }

    /*========= 이하 수정 비권장 (출력 형식 유지) =========*/
    public static void main(String[] args) throws IOException {
        StreamTokenizer in = new StreamTokenizer(
                new BufferedReader(new InputStreamReader(System.in)));
        StringBuilder sb = new StringBuilder();
        in.nextToken();

        int T = (int) in.nval;

        for (int tc = 1; tc <= T; tc++) {
            in.nextToken(); int N = (int) in.nval;
            in.nextToken(); int M = (int) in.nval;

            int[] fatigue = new int[N];

            for (int i = 0; i < N; i++) {
                in.nextToken();
                fatigue[i] = (int) in.nval;
            }

            init(N, fatigue);
            sb.append('#').append(tc).append('\n');

            for (int q = 0; q < M; q++) {
                in.nextToken(); int op = (int) in.nval;

                if (op == 1) {
                    in.nextToken(); int l = (int) in.nval;
                    in.nextToken(); int r = (int) in.nval;
                    in.nextToken(); int w = (int) in.nval;
                    addStress(l, r, w);

                } else if (op == 2) {
                    in.nextToken(); int l = (int) in.nval;
                    in.nextToken(); int r = (int) in.nval;
                    in.nextToken(); int v = (int) in.nval;
                    repair(l, r, v);

                } else if (op == 3) {
                    in.nextToken(); int l = (int) in.nval;
                    in.nextToken(); int r = (int) in.nval;
                    sb.append(getPeak(l, r)).append('\n');

                } else {
                    in.nextToken(); int l = (int) in.nval;
                    in.nextToken(); int r = (int) in.nval;
                    in.nextToken(); int x = (int) in.nval;
                    sb.append(findRisk(l, r, x)).append('\n');
                }
            }
        }
        System.out.print(sb);
    }
}