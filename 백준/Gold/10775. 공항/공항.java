import java.io.*;
import java.util.*;

public class Main {

    static int[] parent;

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int G = Integer.parseInt(br.readLine());
        int P = Integer.parseInt(br.readLine());

        parent = new int[G + 1];
        for (int i = 1; i <= G; i++) {
            parent[i] = i;
        }

        int result = 0;
        for (int i = 0; i < P; i++) {
            int p = Integer.parseInt(br.readLine());

            int gate = find(p);

            if (gate == 0) break;

            union(gate - 1, gate);

            result++;
        }

        System.out.println(result);
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