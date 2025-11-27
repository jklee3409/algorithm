import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static int[] stack;
    static int size = 0;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        
        int N = Integer.parseInt(br.readLine());
        stack = new int[N];
        
        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String command = st.nextToken();
            
            switch (command) {
                case "push":
                    int x = Integer.parseInt(st.nextToken());
                    push(x);
                    break;
                case "pop":
                    sb.append(pop()).append('\n');
                    break;
                case "size":
                    sb.append(size()).append('\n');
                    break;
                case "empty":
                    sb.append(empty()).append('\n');
                    break;
                case "top":
                    sb.append(top()).append('\n');
                    break;
            }
        }
        
        System.out.print(sb);
    }
    
    static void push(int x) {
        stack[size++] = x;
    }
    
    static int pop() {
        if (size == 0) return -1;
        return stack[--size];
    }
    
    static int size() {
        return size;
    }
    
    static int empty() {
        return size == 0 ? 1 : 0;
    }
    
    static int top() {
        if (size == 0) return -1;
        return stack[size - 1];
    }
}
