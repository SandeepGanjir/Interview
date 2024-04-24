package tree;

import java.util.*;
import java.io.StringReader;

/**
 * Given a binary tree. Find the size of its largest subtree that is a Binary Search Tree.
 * https://practice.geeksforgeeks.org/problems/largest-bst/1
 * Input : 1 2 3 N N 4 6 N 5 N N N 7
 * Output : 3
 * Expected Time Complexity: O(N).
 * Expected Auxiliary Space: O(Height of the BST).
 *  @author Sandeep Ganjir
 */
public class LargestBST {
    class Node {
        int data;
        Node leftChild;
        Node rightChild;

        Node(int data) {
            this.data = data;
            leftChild = rightChild = null;
        }
    }

    private Node root;
    Map<Integer, Node> nodeMap = new HashMap<>();

    public void buildTree(String inputLine) {
        Scanner sc = new Scanner(new StringReader(inputLine));
        Queue<Integer> que = new LinkedList<>();
        for (int counter = 0; sc.hasNext(); counter++) {
            String nextInp = sc.next();
            Integer parent = que.peek();
            if (counter % 2 == 0)
                que.poll();
            if (nextInp.equalsIgnoreCase("N"))
                continue;
            int nextInt = Integer.parseInt(nextInp);
            insertAt(parent, nextInt, counter % 2 == 1);
            que.add(nextInt);
        }
        sc.close();
    }

    public void insertAt(Integer parent, int value, boolean isLeftChild) {
        Node newNode = new Node(value);
        nodeMap.put(value, newNode);
        if (parent == null)
            root = newNode;
        else
        if (isLeftChild)
            nodeMap.get(parent).leftChild = newNode;
        else
            nodeMap.get(parent).rightChild = newNode;
    }

    // This functon will return a size 4 Integer Array with convention
    // [ SizeOfLargestBstInSubtree, IsSubTreeBST(1|0), leftMostValueInBst, rightMostValueInBst ]
    private Integer[] largestBST(Node node){
        if (node==null) {
            Integer[] Ar = {0, -1, null, null};
            return Ar;
        }
        Integer[] Ar = {1, 1, node.data, node.data};
        boolean isBST = true;
        int leftBstSize = 0;
        int rightBstSize = 0;

        if (node.leftChild!=null) {
            Integer[] ArL = largestBST(node.leftChild);
            if (ArL[1]==0 || !isBST || ArL[3]>node.data) {
                isBST = false;
            } else {
                Ar[2] = ArL[2];
            }
            leftBstSize = ArL[0];
        }
        if (node.rightChild!=null) {
            Integer[] ArR = largestBST(node.rightChild);
            if (ArR[1]==0 || !isBST || ArR[2]<node.data) {
                isBST = false;
            } else {
                Ar[3] = ArR[3];
            }
            rightBstSize = ArR[0];
        }

        Ar[0] = (isBST==true) ? (leftBstSize + 1 + rightBstSize) : Math.max(leftBstSize, rightBstSize);
        Ar[1] = (isBST==true) ? 1 : 0;
        return Ar;
    }

    public int largestBST(){
        return largestBST(root)[0];
    }

    public static void main(String[] Args) {
        LargestBST ins = new LargestBST();
        ins.buildTree("1 2 3 N N 4 6 N 5 N N N 7"); //"1 2 3 N N 4 6 N 5 N N N 7"

        System.out.println("The size of largest BST from given binary tree is : " + ins.largestBST());
    }
}
