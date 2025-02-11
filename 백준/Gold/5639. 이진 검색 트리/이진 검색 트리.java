import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;

        BinarySearchTree bst = new BinarySearchTree();
        while ((line = br.readLine()) != null) {
            if (line.isEmpty()) break;

            int value = Integer.parseInt(line);

            bst.insert(value);
        }

        bst.printTree();
    }

   static class Node {
        int value;
        Node left, right;

        public Node(int value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }
    }

    static class BinarySearchTree {
        Node root;

        public void insert(int value) {
            root = insertRec(root, value);
        }

        public void printTree() {
            postOrder(root);
        }

        private void postOrder(Node root) {
            if (root != null) {
                postOrder(root.left);
                postOrder(root.right);
                System.out.println(root.value);
            }
        }

        private Node insertRec(Node root, int value) {
            if (root == null) {
                return new Node(value);
            }

            if (value < root.value) {
                root.left = insertRec(root.left, value);
            } else {
                root.right = insertRec(root.right, value);
            }

            return root;
        }
    }
}
