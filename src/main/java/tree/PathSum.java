package tree;

import com.google.gson.GsonBuilder;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;

/**
 * Given a binary tree we need to return maximum path sum.
 * A path between 2 node is defined as set of all edges and nodes connecting them such that no
 * edges or nodes are repeated. Path sum is sum of all node values (inclusive) between any 2 node.
 */
public class PathSum {
    private class Node {
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
        PathSum ins = new PathSum();
        Node root = ins.buildTree("1 2 3 4 5 N 6 7 N N N N 8 9 10 N 11 12 13 14 N 15 N N 16 17 18 19 N 20", -40);
        //Node root = ins.buildTree("12 -37 -20 -35 -31 N -10 15 N N N N -9 28 32 N -1 -19 4 9 N 0 N N 31 -25 14 19 N -38", null);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(root));
        int[] res = ins.maxPathSum(root);
        System.out.println("Max Path Sum is : " + res[1]);
    }

    // Recursively build path sum (DFS using recursion)
    //  Time Complexity = O(number of nodes)
    private int[] maxPathSum(Node node) {
        int[] res = {0, 0};
        if (node == null) return res;
        if (node.leftChild == null && node.rightChild == null) {
            res[1] = res[0] = node.value;
        } else if (node.leftChild != null && node.rightChild != null) {
            int[] val1 = maxPathSum(node.leftChild);
            int[] val2 = maxPathSum(node.rightChild);
            res[0] = Math.max(node.value, node.value + Math.max(val1[0], val2[0]));
            res[1] = Math.max(res[0], Math.max(Math.max(val1[1], val2[1]), node.value + val1[0] + val2[0]));
        } else {
            int[] val = maxPathSum(node.leftChild != null ? node.leftChild : node.rightChild);
            res[0] = Math.max(node.value, node.value + val[0]);
            res[1] = Math.max(res[0], val[1]);
        }
        System.out.println(node.value + " " + Arrays.toString(res));
        return res;
    }

    // Build tree from String like "1 2 3 4 5 N 6 7 N N N N 8 9 10 N 11 12 13 14 N 15 N N 16 17 18 19 N 20"
    private Node buildTree(String inputLine, Integer range) {
        Scanner sc = new Scanner(new StringReader(inputLine));
        Queue<Integer> que = new LinkedList<>();
        Map<Integer, Node> nodeMap = new HashMap<>();
        Random r = new Random();
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

            int val = range == null ? nextInt : r.nextInt(Math.abs(range)) * (r.nextFloat() < 0.5 ? -1 : 1);
            Node newNode = new Node(val);
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
