import java.io.*;
import java.util.*;

public class Main {
    static int N, K;
    static int[] deviceArr;
    static Set<Integer> plugs = new HashSet<>();

    public static void main(String[] args) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());
        deviceArr = new int[K];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < K; i++) deviceArr[i] = Integer.parseInt(st.nextToken());

        int result = 0;
        for (int i = 0; i < K; i++) {
            int device = deviceArr[i];

            // 이미 플러그에 꽂혀있는 경우
            if (plugs.contains(device)) continue;

            // 플러그에 자리가 있는 경우
            if (plugs.size() < N) {
                plugs.add(device);
                continue;
            }

            int maxDist = 0;
            int targetPlug = 0;

            // 플러그에서 하나를 빼야 하는 경우 (가장 늦게 사용되는 것을 제거하자)
            for (Integer plug : plugs) {
                int next = Integer.MAX_VALUE; // 또 다시 사용되는 경우의 인덱스

                // 현재 탐색 중인 디바이스 이후부터 탐색
                for (int j = i + 1; j < K; j++) {

                    if (plug == deviceArr[j]) {
                        next = j;
                        break;
                    }
                }

                if (maxDist < next) {
                    maxDist = next;
                    targetPlug = plug;
                }
            }

            plugs.remove(targetPlug);
            plugs.add(device);
            result++;
        }

        System.out.println(result);
    }
}