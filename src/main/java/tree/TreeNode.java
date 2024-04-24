package tree;
import java.util.*;

/**
 * Given an undirected acyclic graph, find the node which if
 * made root will result in a tree of minimum height.
 * Input : {{5, 2}, {3, 1}, {4, 2}, {1, 5}, {6, 2}};
 * Output : 5
 * Expected Time Complexity: O(N).
 * Expected Auxiliary Space: O(N).
 *  @author Sandeep Ganjir
 */
public class TreeNode {
    public static void main(String[] Args) {
        TreeNode ins = new TreeNode();
        int[] vertices = {1, 2, 3, 4, 5, 6};
        int[][] edges = {{5, 2}, {3, 1}, {4, 2}, {1, 5}, {6, 2}}; //{{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 6}};;
        System.out.println("The root node for min height tree should be : " + ins.getRoots(vertices, edges));
    }

    private List<Integer> getRoots(int[] Vtx, int[][] Edges) {
        Set<Integer> remain = new HashSet<>();
        Map<Integer, Set<Integer>> adjList = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < Edges.length; i++) {
            Set<Integer> adj1 = adjList.computeIfAbsent(Edges[i][0], a -> new HashSet<>());
            Set<Integer> adj2 = adjList.computeIfAbsent(Edges[i][1], a -> new HashSet<>());
            adj1.add(Edges[i][1]);
            adj2.add(Edges[i][0]);
        }
        for (int i = 0; i < Vtx.length; i++) {
            if (adjList.get(Vtx[i]).size() == 1)
                queue.add(Vtx[i]);
            else
                remain.add(Vtx[i]);
        }
        while (!remain.isEmpty()) {
            Queue<Integer> nextQueue = new LinkedList<>();
            while (!queue.isEmpty()) {
                Set<Integer> adjacent = adjList.get(queue.poll());
                for (Integer vtx : adjacent) {
                    if (remain.contains(vtx)) {
                        nextQueue.add(vtx);
                        remain.remove(vtx);
                    }
                }
            }
            queue = nextQueue;
        }
        return (List<Integer>) queue;
    }
}
