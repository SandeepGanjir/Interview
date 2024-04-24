package graph;
import java.util.*;

/**
 * Given a graph, find the number of disconnected components.
 * Input : a Graph
 * Output : 5
 * Expected Time Complexity: O(N + E).
 * Expected Auxiliary Space: O(N).
 *  @author Sandeep Ganjir
 */
public class DisjointSet {
    public static void main (String[] Args) {
        DisjointSet ins = new DisjointSet();
        int[] vertices = {1, 2, 3, 4, 5, 6, 7, 8};
        int[][] edges = {{1, 2}, {3, 5}, {4, 2}, {4, 2}, {5, 1}, {7, 6}, {5, 4}};
        System.out.println("Number of disconnected componenets in the graph is : " + ins.disconnectedComponenets(vertices, edges));
    }

    private int disconnectedComponenets(int[] Vtx, int[][] Edges) {
        Map<Integer, Set<Integer>> components = new HashMap<>();
        for (int i = 0; i < Vtx.length; i++) {
            Set<Integer> island = new HashSet<>();
            island.add(Vtx[i]);
            components.put(Vtx[i], island);
        }
        for (int i = 0; i < Edges.length; i++) {
            // Edge between Edges[i][0] & Edges[i][1]
            Set<Integer> island = components.get(Edges[i][0]);
            Set<Integer> island2 = components.get(Edges[i][1]);
            if (island != island2) {
                island.addAll(island2);
                for (Integer vtx : island2) {
                    components.put(vtx, island);
                }
            }
        }
        Set<Set<Integer>> islands = new HashSet<>();
        for (Set<Integer> island : components.values()) {
            islands.add(island);
        }
        return islands.size();
    }
}
