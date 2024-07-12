import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main{

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        int[] arr = new int[N];

        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(br.readLine());
        }

        br.close();

        mergeSort(arr,0, arr.length - 1);

        for (int var : arr) {
            System.out.println(var);
        }
    }

    public static void mergeSort(int[] arr, int left, int right) {
        if (left < right) {

            int middle = (left + right) / 2;

            mergeSort(arr, left, middle);

            mergeSort(arr, middle + 1, right);

            merge(arr, left, middle, right);
        }
    }

    public static void merge(int[] arr, int left, int middle, int right) {

        // 구간 설정
        int n1 = middle - left + 1;
        int n2 = right - middle;

        //임시 배열 생성
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];

        System.arraycopy(arr, left, leftArr, 0, n1);
        System.arraycopy(arr, middle + 1, rightArr, 0, n2);

        // i = leftArr 의 인덱스
        // j = rightArr 의 인덱스
        // k = arr 의 인덱스
        int i = 0, j = 0, k = left;

        // leftArr 과 rightArr 을 비교하여
        // 더 작은 원소를 arr에 복사
        while (i < n1 && j < n2) {
            if (leftArr[i] <= rightArr[j]) {
                arr[k] = leftArr[i++];
            } else {
                arr[k] = rightArr[j++];
            }
            k++;
        }

        // leftArr 에 원소가 남아 있는 경우
        while (i < n1) {
            arr[k++] = leftArr[i++];
        }

        // rightArr 에 원소가 남아 있는 경우
        while (j < n2) {
            arr[k++] = rightArr[j++];
        }
    }
}