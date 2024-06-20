package tree;

import java.io.StringReader;
import java.util.*;

public class ZigZagTree {
    private static class Node {
        int value;
        Node leftChild, rightChild;
        Node (int val, Node l, Node r) {
            value = val;
            leftChild = l;
            rightChild = r;
        }
        Node (int val) {
            this(val, null, null);
        }
    }

    public static void main(String[] args) {
        ZigZagTree ins = new ZigZagTree();
        Node root = ins.buildTree("1 2 3 4 5 6 7 8 9 10 11 12");
        ins.printZigZagTraversal(root);
    }

    public void printZigZagTraversal(Node root) {
        Stack<Node> stk = new Stack<>();
        stk.push(root);
        boolean reverse = true;
        while (!stk.isEmpty()) {
            Stack<Node> newStk = new Stack<>();
            reverse = !reverse;
            while (!stk.isEmpty()) {
                Node cur = stk.pop();
                System.out.print(cur.value + " ");
                _addNext(newStk, cur, reverse);
            }
            System.out.println("");
            stk = newStk;
        }
    }

    private void _addNext(Stack<Node> newStk, Node cur, boolean reverse) {
        if (reverse) {
            if (cur.rightChild != null) newStk.push(cur.rightChild);
            if (cur.leftChild != null) newStk.push(cur.leftChild);
        } else {
            if (cur.leftChild != null) newStk.push(cur.leftChild);
            if (cur.rightChild != null) newStk.push(cur.rightChild);
        }
    }

    // Build tree from String like "1 2 3 N N 4 6 N 5 N N N 7"
    private Node buildTree(String inputLine) {
        Scanner sc = new Scanner(new StringReader(inputLine));
        Queue<Integer> que = new LinkedList<>();
        Map<Integer, Node> nodeMap = new HashMap<>();
        Node root = null;
        for (int counter = 0; sc.hasNext(); counter++) {
            String nextInp = sc.next();
            Integer parent = que.peek();
            if (counter % 2 == 0)
                que.poll();
            if (nextInp.equalsIgnoreCase("N"))
                continue;
            int nextInt = Integer.parseInt(nextInp);
            que.add(nextInt);

            Node newNode = new Node(nextInt);
            nodeMap.put(nextInt, newNode);
            if (parent == null) {
                root = nodeMap.get(nextInt);
            } else {
                if (counter % 2 == 1)
                    nodeMap.get(parent).leftChild = newNode;
                else
                    nodeMap.get(parent).rightChild = newNode;
            }
        }
        sc.close();
        return root;
    }
}
