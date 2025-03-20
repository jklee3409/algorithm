import java.io.*;
import java.util.*;

public class Main {
    static int N, C;
    static int[] houses;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        houses = new int[N];

        for (int i = 0; i < N; i++) {
            houses[i] = Integer.parseInt(br.readLine());
        }

        Arrays.sort(houses);

        int low = 1, high = houses[N - 1];
        int result = 0;

        while (low <= high) {
            int mid = (low + high) / 2;

            if (canInstall(mid)) {
                result = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        System.out.println(result);
    }

    public static boolean canInstall(int distance) {
        int count = 1;
        int lastInstalled = houses[0];

        for (int i = 1; i < N; i++) {

            // distance 보다 먼 경우에만 설치 가능
            // mid 가 최대값임을 보장하기 위해
            if (houses[i] - lastInstalled >= distance) {
                count++;
                lastInstalled = houses[i];
            }
            if (count == C) return true;
        }

        return false;
    }
}
