import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());

        List<Integer> list = new LinkedList<>();
        for (int i = 1; i <= N; i++) {
            list.add(i);
        }

        Iterator<Integer> it = list.iterator();
        List<Integer> result = new ArrayList<>();

        while (!list.isEmpty()) {
            for(int i = 0; i < M - 1; i++) {
                if(!it.hasNext()) {
                    it = list.iterator();
                }
                it.next();
            }

            if (!it.hasNext()) {
                it = list.iterator();
            }

            int removed = it.next();
            it.remove();
            result.add(removed);
        }

        System.out.println(result.toString().replace("[", "<").replace("]", ">"));
    }
}
