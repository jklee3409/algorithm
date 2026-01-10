import java.io.*;
import java.util.*;

public class Main {
    static int N, M, K;
    static Land[][] land;
    static int[][] energies;

    static class Land {
        int energy;
        List<Tree> trees;

        public Land(int energy, List<Tree> trees) {
            this.energy = energy;
            this.trees = trees;
        }
    }

    static class Tree {
        int age;
        boolean isLive;

        public Tree(int age, boolean isLive) {
            this.age = age;
            this.isLive = isLive;
        }
    }

    public static void main(String[] args) throws  IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        land = new Land[N + 1][N + 1];
        energies = new int[N + 1][N + 1];

        for (int i = 1; i < N + 1; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 1; j < N + 1; j++) {
                int addEnergy = Integer.parseInt(st.nextToken());
                land[i][j] = new Land(5, new ArrayList<>());
                energies[i][j] = addEnergy;
            }
        }

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            int age = Integer.parseInt(st.nextToken());

            Tree tree = new Tree(age, true);
            land[y][x].trees.add(tree);
        }

        simulate();
    }

    static void simulate() {

        while (K-- > 0) {
            // 봄
            for (int y = 1; y < N + 1; y++) {
                for (int x = 1; x < N + 1; x++) {
                    grow(y, x);
                }
            }

            // 여름
            for (int y = 1; y < N + 1; y++) {
                for (int x = 1; x < N + 1; x++) {
                    recycleDeadTree(y, x);
                }
            }

            // 가을
            for (int y = 1; y < N + 1; y++) {
                for (int x = 1; x < N + 1; x++) {
                    propagate(y, x);
                }
            }

            // 겨울
            for (int y = 1; y < N + 1; y++) {
                for (int x = 1; x < N + 1; x++) {
                    addEnergy(y, x);
                }
            }
        }

        int result = 0;
        for (Land[] lands : land) {
            for (Land land1 : lands) {
                if (land1 == null || land1.trees.isEmpty()) continue;
                result += land1.trees.size();
            }
        }

        System.out.println(result);
    }

    static void grow(int y, int x) {
        int energy = land[y][x].energy;
        land[y][x].trees.sort(Comparator.comparing(tree -> tree.age));

        for (Tree tree : land[y][x].trees) {
            if (tree.isLive) {
                if (tree.age <= energy) {
                    energy -= tree.age;
                    tree.age++;
                } else {
                    tree.isLive = false;
                }
            }
        }

        land[y][x].energy = energy;
    }

    static void recycleDeadTree(int y, int x) {
        Iterator<Tree> it = land[y][x].trees.iterator();

        while (it.hasNext()) {
            Tree tree = it.next();
            if (!tree.isLive) {
                land[y][x].energy += tree.age / 2;
                it.remove();
            }
        }
    }

    static int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};
    static int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};

    static void propagate(int y, int x) {
        for (Tree tree : land[y][x].trees) {
            if (tree.isLive && tree.age % 5 != 0) continue;

            for (int d = 0; d < 8; d++) {
                int ny = y + dy[d];
                int nx = x + dx[d];

                if (ny < 1 || nx < 1 || ny > N || nx > N) continue;

                land[ny][nx].trees.add(new Tree(1, true));
            }
        }
    }

    static void addEnergy(int y, int x) {
        land[y][x].energy += energies[y][x];
    }
}