package tree;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.vertx.core.Vertx;

import java.io.StringReader;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

/**
 * A simple implementation of AVL Tree.
 *
 * @author Sandeep Ganjir
 */
public class AVLTree<V> {
    private final Comparator<? super V> comparator;
    private AvlNode<V> root = null;

    public AVLTree() {
        this.comparator = Comparator.comparing(a -> Double.valueOf(a.toString()));
    }

    public AVLTree(Comparator<? super V> comparator) {
        this.comparator = comparator;
    }

    public static void processRequest(AVLTree avlTree) {
        char inp = 'c';
        Scanner scanner = new Scanner(System.in);
        String msg = "##########################################################\n" +
                "Enter (f, i, I, d, D, q) to denote your choice :\n" +
                "1. f to search for element in the AVL tree\n" +
                "2. i to insert an element\n" +
                "3. I to insert multiple elements e.g. 15 11 12 7 9 5 5 3 10\n" +
                "4. d to delete an element\n" +
                "5. D to delete all the elements\n" +
                "6. q to quit the program\n" +
                "##########################################################";

        System.out.println(msg);
        while (inp != 'q') {
            System.out.print("\nEnter f, i, I, d, D or q : ");
            inp = scanner.next().charAt(0);
            switch (inp) {
                case 'q':
                    System.exit(0);
                case 'f':
                    System.out.print("Enter input to search: ");
                    System.out.println(avlTree.find(scanner.nextInt()));
                    break;
                case 'i':
                    System.out.print("Enter input to insert: ");
                    avlTree.insert(scanner.nextInt());
                    break;
                case 'I':
                    System.out.print("Enter space separated integers to insert: ");
                    scanner.nextLine();
                    String inputString = scanner.nextLine();
                    for (Scanner sc = new Scanner(new StringReader(inputString)); sc.hasNextInt(); )
                        avlTree.insert(sc.nextInt());
                    break;
                case 'd':
                    System.out.print("Enter input to delete: ");
                    avlTree.delete(scanner.nextInt());
                    break;
                case 'D':
                    avlTree.root = null;
                    break;
            }
        }
        scanner.close();
    }

    private static void loadSampleData(AVLTree avlTree) {
        int[] Ar = {15, 11, 12, 7, 9, 5, 5, 3, 10}; //{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int j : Ar) avlTree.insert(j);
    }

    public static void main(String[] args) {
        final AVLTree<Integer> avlTree = new AVLTree<>(Comparator.comparingInt(a -> a));

        final int port = new Random().nextInt(65500);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Vertx vertx = Vertx.vertx();
        vertx.createHttpServer().requestHandler(req -> req.response().end(gson.toJson(avlTree.root))).listen(port);
        System.out.println("Check out the tree at: http://localhost:" + port);

        loadSampleData(avlTree);
        processRequest(avlTree);
    }

    public boolean find(V data) {
        return root == null ? false : root.find(data);
    }

    public void insert(V data) {
        root = root == null ? new AvlNode<>(data, comparator) : root.insert(data);
    }

    public void delete(V data) {
        root = root == null ? null : root.delete(data, null);
    }

    private class AvlNode<E> {
        private transient final Comparator<? super E> comparator;
        private E data;
        private int height = 0;
        private AvlNode<E> leftChild = null;
        private AvlNode<E> rightChild = null;

        AvlNode(E data, Comparator<? super E> comparator) {
            this.comparator = comparator;
            this.data = data;
        }

        public E value() {
            return data;
        }

        public boolean find(E data) {
            if (comparator.compare(data, this.data) == 0) return true;
            if (comparator.compare(data, this.data) < 0)
                return this.leftChild == null ? false : this.leftChild.find(data);
            else
                return this.rightChild == null ? false : this.rightChild.find(data);
        }

        public AvlNode<E> insert(E data) {
            if (comparator.compare(data, this.data) == 0) return this;
            if (comparator.compare(data, this.data) < 0)
                leftChild = leftChild == null ? new AvlNode<>(data, comparator) : leftChild.insert(data);
            else
                rightChild = rightChild == null ? new AvlNode<>(data, comparator) : rightChild.insert(data);
            return rebalance();
        }

        public AvlNode<E> delete(E data, AvlNode<E> nodeToDel) {
            if (comparator.compare(data, this.data) == 0) {
                nodeToDel = this;
            }
            if (leftChild == null && rightChild == null && nodeToDel != null) {
                nodeToDel.data = this.data;
                return null;
            }
            if (comparator.compare(data, this.data) < 0)
                leftChild = leftChild == null ? null : leftChild.delete(data, nodeToDel);
            else
                rightChild = rightChild == null ? null : rightChild.delete(data, nodeToDel);
            return rebalance();
        }

        private AvlNode<E> rebalance() {
            AvlNode<E> rotNewCur;
            if (leftHeight() - rightHeight() > 1) {
                if (leftChild.leftHeight() > leftChild.rightHeight()) {
                    rotNewCur = leftChild;
                    leftChild = rotNewCur.rightChild;
                    rotNewCur.rightChild = this;
                } else {
                    rotNewCur = leftChild.rightChild;
                    leftChild.rightChild = rotNewCur.leftChild;
                    rotNewCur.leftChild = leftChild;
                    leftChild = rotNewCur.rightChild;
                    rotNewCur.rightChild = this;
                    rotNewCur.setHeight();
                }
            } else if (rightHeight() - leftHeight() > 1) {
                if (rightChild.rightHeight() > rightChild.leftHeight()) {
                    rotNewCur = rightChild;
                    rightChild = rotNewCur.leftChild;
                    rotNewCur.leftChild = this;
                } else {
                    rotNewCur = rightChild.leftChild;
                    rightChild.leftChild = rotNewCur.rightChild;
                    rotNewCur.rightChild = rightChild;
                    rightChild = rotNewCur.leftChild;
                    rotNewCur.leftChild = this;
                    rotNewCur.setHeight();
                }
            } else {
                rotNewCur = this;
            }
            setHeight();
            return rotNewCur;
        }

        private int leftHeight() {
            return leftChild == null ? -1 : leftChild.height;
        }

        private int rightHeight() {
            return rightChild == null ? -1 : rightChild.height;
        }

        private void setHeight() {
            height = Math.max(leftHeight(), rightHeight()) + 1;
        }
    }
}
