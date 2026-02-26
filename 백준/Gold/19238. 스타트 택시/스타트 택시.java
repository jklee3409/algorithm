import java.io.*;
import java.util.*;

public class Main {

    static int N, M, Fuel;
    static int[][] map;
    static int taxiR, taxiC;
    static int[][] passengersMap;
    static Passenger[] passengers;

    static int[] dr = {-1, 0, 0, 1};
    static int[] dc = {0, -1, 1, 0};

    static class Passenger {
        int r, c, destR, destC;
        public Passenger(int r, int c, int destR, int destC) {
            this.r = r;
            this.c = c;
            this.destR = destR;
            this.destC = destC;
        }
    }

    static class Node implements Comparable<Node> {
        int r, c, d;
        public Node(int r, int c, int d) {
            this.r = r;
            this.c = c;
            this.d = d;
        }
        @Override
        public int compareTo(Node o) {
            if (this.d != o.d) return this.d - o.d;
            if (this.r != o.r) return this.r - o.r;
            return this.c - o.c;
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        Fuel = Integer.parseInt(st.nextToken());

        map = new int[N][N];
        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < N; j++) {
                map[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        st = new StringTokenizer(br.readLine());
        taxiR = Integer.parseInt(st.nextToken()) - 1;
        taxiC = Integer.parseInt(st.nextToken()) - 1;

        passengersMap = new int[N][N];
        passengers = new Passenger[M + 1];

        for (int i = 1; i <= M; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken()) - 1;
            int c = Integer.parseInt(st.nextToken()) - 1;
            int dr = Integer.parseInt(st.nextToken()) - 1;
            int dc = Integer.parseInt(st.nextToken()) - 1;
            passengers[i] = new Passenger(r, c, dr, dc);
            passengersMap[r][c] = i;
        }

        for (int i = 0; i < M; i++) {
            int[] target = findPassenger();
            if (target == null) {
                System.out.println(-1);
                return;
            }

            int pIdx = target[0];
            int dist = target[1];

            Fuel -= dist;
            if (Fuel < 0) {
                System.out.println(-1);
                return;
            }

            Passenger p = passengers[pIdx];
            taxiR = p.r;
            taxiC = p.c;
            passengersMap[p.r][p.c] = 0;

            int moveDist = move(p.destR, p.destC);
            if (moveDist == -1) {
                System.out.println(-1);
                return;
            }

            Fuel -= moveDist;
            if (Fuel < 0) {
                System.out.println(-1);
                return;
            }

            Fuel += moveDist * 2;
            taxiR = p.destR;
            taxiC = p.destC;
        }

        System.out.println(Fuel);
    }

    static int[] findPassenger() {
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.offer(new Node(taxiR, taxiC, 0));
        boolean[][] visited = new boolean[N][N];
        visited[taxiR][taxiC] = true;

        while (!pq.isEmpty()) {
            Node curr = pq.poll();

            if (passengersMap[curr.r][curr.c] > 0) {
                return new int[]{passengersMap[curr.r][curr.c], curr.d};
            }

            for (int i = 0; i < 4; i++) {
                int nr = curr.r + dr[i];
                int nc = curr.c + dc[i];

                if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                if (map[nr][nc] == 1 || visited[nr][nc]) continue;

                visited[nr][nc] = true;
                pq.offer(new Node(nr, nc, curr.d + 1));
            }
        }
        return null;
    }

    static int move(int destR, int destC) {
        Queue<Node> q = new LinkedList<>();
        q.offer(new Node(taxiR, taxiC, 0));
        boolean[][] visited = new boolean[N][N];
        visited[taxiR][taxiC] = true;

        while (!q.isEmpty()) {
            Node curr = q.poll();

            if (curr.r == destR && curr.c == destC) return curr.d;

            for (int i = 0; i < 4; i++) {
                int nr = curr.r + dr[i];
                int nc = curr.c + dc[i];

                if (nr < 0 || nr >= N || nc < 0 || nc >= N) continue;
                if (map[nr][nc] == 1 || visited[nr][nc]) continue;

                visited[nr][nc] = true;
                q.offer(new Node(nr, nc, curr.d + 1));
            }
        }
        return -1;
    }
}