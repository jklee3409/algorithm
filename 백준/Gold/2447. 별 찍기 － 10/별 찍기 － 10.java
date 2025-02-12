import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int N = scanner.nextInt();
        scanner.close();

        String[] result = star(N);

        StringBuilder sb = new StringBuilder();
        for (String line : result) {
            sb.append(line).append("\n");
        }

        System.out.print(sb);
    }

    private static String[] star(int k) {
        if (k == 3) {
            return new String[]{"***", "* *", "***"};
        }

        String[] arr = star(k / 3);
        String[] stars = new String[k];
        int index = 0;

        for (String s : arr) {
            stars[index++] = s + s + s;
        }

        for (String s : arr) {
            stars[index++] = s + " ".repeat(k / 3) + s;
        }

        for (String s : arr) {
            stars[index++] = s + s + s;
        }

        return stars;
    }
}