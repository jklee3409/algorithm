import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine()), count = 0;
        HashMap<String, Boolean> firstChat = new HashMap<>();

        while (N-- > 0) {
            String name = br.readLine();

            if (Objects.equals(name, "ENTER")) {
                firstChat.clear();
            } else {
                if (!firstChat.containsKey(name)) {
                    firstChat.put(name, true);
                    count++;
                }
            }
        }

        br.close();;
        System.out.println(count);
    }
}
