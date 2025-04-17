import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.StringTokenizer;

public class Main {
    static int N, W, L;
    static Truck[] trucks;
    static class Truck {
        int weight, position;

        public Truck(int weight, int position) {
            this.weight = weight;
            this.position = position;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken()); // 트럭 수
        W = Integer.parseInt(st.nextToken()); // 다리 길이
        L = Integer.parseInt(st.nextToken()); // 최대 하중
        trucks = new Truck[N];

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < N; i++) {
            int weight = Integer.parseInt(st.nextToken());
            trucks[i] = new Truck(weight, 0);
        }

        simulate();
    }

    public static void simulate() {
        Queue<Truck> bridge = new ArrayDeque<>(); // 다리
        Truck curTruck = trucks[0];

        bridge.offer(curTruck);
        int currentWeight = curTruck.weight; // 다리에 올라가 있는 트럭 무게의 합

        int idx = 1; // 다리를 지나갈 트럭
        int time = 1; // 시간

        while (idx < N || !bridge.isEmpty()) {
            if (idx < N) curTruck = trucks[idx];

            if (bridge.isEmpty()) continue;

            for (Truck truck : bridge) {
                truck.position++; // 다리 위의 트럭 한칸씩 이동

                if (truck.position == W) { // 다리를 지나갔다면
                    bridge.poll();
                    currentWeight -= truck.weight;
                }
            }

            if (currentWeight + curTruck.weight <= L && idx < N) { // 다리에 올라갈 수 있다면 진입
                bridge.offer(curTruck); // 다리 진입
                currentWeight += curTruck.weight; // 무게 증가

                idx++;
            }

            time++; // 한 턴 증가
        }

        System.out.println(time);
    }
}
