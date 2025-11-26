import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int T = sc.nextInt();

        int[][] apt = new int[15][15];
        
        // 0층 i호에는 i명이 산다.
        for (int i = 1; i <= 14; i++) {
            apt[0][i] = i;
        }
        
        // i층 j호에는 (i-1)층 1~j호까지 사람들의 수의 합만큼 산다.
        for (int i = 1; i <= 14; i++) {
            for (int j = 1; j <= 14; j++) {
                apt[i][j] = apt[i][j-1] + apt[i-1][j];
            }
        }
        
        for (int t = 0; t < T; t++) {
            int k = sc.nextInt();
            int n = sc.nextInt();
            System.out.println(apt[k][n]);
        }
        
        sc.close();
    }
}
