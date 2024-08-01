import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Main{
    public static int stored_num = 0, store_count = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());

        int[] arr = new int[N];
        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        Merge_sort.merge_sort(arr, K);

        br.close();
        System.out.println(store_count < K ? -1 : stored_num);
    }

    public static class Merge_sort{
        private static int[] sorted;

        public static void merge_sort(int[] arr, int K) {
            sorted = new int[arr.length];
            merge_sort(arr, 0, arr.length - 1, K);
            sorted = null;
        }

        private static void merge_sort(int[] arr, int low, int high, int K) {
            if (low == high) return;

            int mid = (low + high) / 2;

            merge_sort(arr, low, mid, K);
            merge_sort(arr, mid + 1, high, K);

            merge(arr, low, mid, high, K);
        }

        private static void merge(int[] arr, int low, int mid, int high, int K) {
            int i = low, j = mid + 1, k = low;

            while (i <= mid && j <= high) {
                if (arr[i] <= arr[j]) {
                    sorted[k++] = arr[i++];
                } else {
                    sorted[k++] = arr[j++];
                }
            }

            while (i <= mid) {
                sorted[k++] = arr[i++];
            }

            while (j <= high) {
                sorted[k++] = arr[j++];
            }

            for (int l = low; l <= high; l++) {
                arr[l] = sorted[l];
                store_count++;
                if (store_count == K) {
                    stored_num = arr[l];
                }
            }
        }
    }
}
