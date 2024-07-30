import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine()), sum = 0, maxCount = 1;
        int[] arr = new int[N];
        HashMap<Integer, Integer> map = new HashMap<>();

        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(br.readLine());
            sum += arr[i];

            if (map.containsKey(arr[i])) {
                map.put(arr[i], map.get(arr[i]) + 1);
                if (map.get(arr[i]) > maxCount) {
                    maxCount = map.get(arr[i]);
                }
            } else {
                map.put(arr[i], 1);
            }

        }

        List<Integer> modeList = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == maxCount) {
                modeList.add(entry.getKey());
            }
        }

        Collections.sort(modeList);

        Arrays.sort(arr);
        int mean = Math.round((float) sum / N);
        int median = arr[N / 2];
        int mode = modeList.size() > 1 ? modeList.get(1) : modeList.get(0);

        int range = arr[arr.length - 1] - arr[0];

        String sb = mean + "\n" + median + "\n" + mode + "\n" + range;

        br.close();
        System.out.println(sb);
    }
}
