import java.io.*;
import java.util.*;

public class Main {
    static int N;
    static Node[] tree = new Node[26];
    static StringBuilder sb = new StringBuilder();

    static class Node {
        char left, right;

        Node(char left, char right) {
            this.left = left;
            this.right = right;
        }
    }

    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            char root = st.nextToken().charAt(0);
            char left = st.nextToken().charAt(0);
            char right = st.nextToken().charAt(0);

            int idx = root - 'A';
            tree[idx] = new Node(left, right);
        }

        preorder('A');
        sb.append("\n");
        inorder('A');
        sb.append("\n");
        postorder('A');
        sb.append("\n");

        System.out.println(sb);
    }

    static void preorder(char root) {
        if (root == '.') return;
        int idx = root - 'A';

        sb.append(root);
        preorder(tree[idx].left);
        preorder(tree[idx].right);
    }

    static void inorder(char root) {
        if (root == '.') return;
        int idx = root - 'A';

        inorder(tree[idx].left);
        sb.append(root);
        inorder(tree[idx].right);
    }

    static void postorder(char root) {
        if (root == '.') return;
        int idx = root - 'A';
        
        postorder(tree[idx].left);
        postorder(tree[idx].right);
        sb.append(root);

    }
}
