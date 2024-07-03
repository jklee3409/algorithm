import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double[] score = new double[n];
        double[] newScore = new double[n];
        double max = 0;
        double sum = 0;

        for (int i = 0; i < n; i++) {
            score[i] = sc.nextInt();
            if(max < score[i]){
                max = score[i];
            }
        }

        for (int i = 0; i < n; i++) {
            newScore[i] = score[i] / max * 100;
            sum += newScore[i];
        }

        System.out.println(sum / n);
    }
}