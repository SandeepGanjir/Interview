package graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Lets say we have a Metro system in our city where each metro only stays in same line and
 * goes in loop to certain stops. We can switch lines where stop overlaps. (Uber)
 * Given the list of stops for each line, find minimum number of line
 * change is required to reach from source stop to destination stop.
 *
 * e.g. - {{1,2,7}, {3,6,7,4}, {3,4,8}, {4,5,9}}; Src = 1, Dest = 5
 * Answer -> 3
 */
public class MinLineChange {
    public static void main(String[] args) {
        int[][] routes = {{1,2,7}, {3,6,7,4}, {3,4,8}, {4,5,9}};
        int src = 1;
        int dest = 5;
        MinLineChange ins = new MinLineChange();
        int minHops = ins.getMinHops(routes, src, dest);

        // Output
        System.out.println("Given the list of stops for each metro line as :");
        for(int[] line: routes) {
            System.out.println(Arrays.toString(line));
        }
        System.out.println(String.format("With Source as %s and Destination as %s", src, dest));
        System.out.println("Min line hops required is : " + minHops);
    }

    /*
     * Time Complexity = O(size(routes))
     * Space Complexity = O(size(routes))
     */
    private int getMinHops(int[][] routes, int src, int dest) {
        Map<Integer, List<Set<Integer>>> linesByStop = new HashMap<>();
        for (int i = 0; i < routes.length; i++) {
            Set<Integer> line = new HashSet<>();
            for (int j = 0; j < routes[i].length; j++) {
                int stop = routes[i][j];
                line.add(stop);
                linesByStop.computeIfAbsent(stop, a -> new ArrayList<>()).add(line);
            }
        }
        //System.out.println(linesByStop);

        int minHops = 0;
        final Set<Integer> traversed = new HashSet<>();
        Set<Integer> curStops = new HashSet<>();
        curStops.add(src);
        while (!curStops.isEmpty()) {
            // keep track of all stops we can reach in 1 hop that hasn't already traversed
            Set<Integer> nextStops = new HashSet<>();
            for (Integer stop : curStops) {
                for (Set<Integer> line : linesByStop.get(stop)) {
                    if (line.contains(dest)) {
                        return minHops;
                    }
                    nextStops.addAll(line.stream().filter(st -> !traversed.contains(st)).collect(Collectors.toList()));
                    traversed.addAll(line);
                }
            }
            minHops++;
            curStops = nextStops;
        }
        return -1;
    }
}
