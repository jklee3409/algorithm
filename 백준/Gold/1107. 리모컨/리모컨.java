import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int N = Integer.parseInt(br.readLine()); // 목표 채널
        int M = Integer.parseInt(br.readLine()); // 고장난 버튼 개수

        boolean[] broken = new boolean[10];
        if (M > 0) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            for (int i = 0; i < M; i++) {
                broken[Integer.parseInt(st.nextToken())] = true;
            }
        }

        int minPress = Math.abs(N - 100); 
        
        for (int channel = 0; channel <= 999999; channel++) {
            int len = canPress(channel, broken);
            if (len > 0) {
                int press = len + Math.abs(channel - N);
                minPress = Math.min(minPress, press);
            }
        }

        System.out.println(minPress);
    }
    
    static int canPress(int channel, boolean[] broken) {
        if (channel == 0) return broken[0] ? 0 : 1;

        int len = 0;
        while (channel > 0) {
            int digit = channel % 10;
            if (broken[digit]) return 0;
            len++;
            channel /= 10;
        }
        return len;
    }
}
