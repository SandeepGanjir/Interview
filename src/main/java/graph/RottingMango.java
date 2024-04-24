package graph;
import java.util.*;

/**
 * Given a matrix where each cell in the matrix can have values 0, 1 or 2
 *  representing mangoes which has the following meaning:
 *   0: Empty cell (No mango)
 *   1: Cells have fresh mangoes
 *   2: Cells have rotten mangoes
 * Determine what is the minimum time required so that all the mangoes become rotten.
 * A rotten mango at index [i,j] can rot other fresh mango at indexes
 *  [i-1,j], [i+1,j], [i,j-1], [i,j+1] (up, down, left and right).
 * If it is impossible to rot every mango then simply return -1.
 */
public class RottingMango {
    public static void main(String[] args) {
        RottingMango ins = new RottingMango();
        int[][] Ar = {  {2, 1, 0, 2, 1},
                        {1, 0, 1, 2, 1},
                        {1, 0, 0, 2, 1} };
        for (int[] A: Ar) System.out.println(Arrays.toString(A));
        System.out.println("Time to rot every mango is : " + ins.timeToPropagateRot(Ar));
    }

    private int timeToPropagateRot(int[][] ar) {
        if (ar == null || ar.length<1 || ar[0].length<1) return -1;

        // Get list of all initially rotten mangoes
        List<Integer []> rotten = new ArrayList<>();
        for (int i=0; i<ar.length; i++) {
            for (int j=0; j<ar[i].length; j++) {
                if (ar[i][j] == 2){
                    Integer[] location = {i, j};
                    rotten.add(location);
                }
            }
        }

        int steps = 0;
        while (rotten.size() > 0) {
            rotten = propagateRot(rotten, ar);
            steps++;
        }

        // check if any non-rotten mangoes left, it is impossible to rot them
        for (int i=0; i<ar.length; i++) {
            for (int j=0; j<ar[i].length; j++) {
                if (ar[i][j] == 1){
                    return -1;
                }
            }
        }

        return steps;
    }

    private List<Integer[]> propagateRot(List<Integer[]> rotten, int[][] ar) {
        List<Integer []> nextRottent = new ArrayList<>();
        for (Integer[] pos: rotten) {
            addIfPossibleToRot(ar, pos[0]-1, pos[1], nextRottent);
            addIfPossibleToRot(ar, pos[0]+1, pos[1], nextRottent);
            addIfPossibleToRot(ar, pos[0], pos[1]-1, nextRottent);
            addIfPossibleToRot(ar, pos[0], pos[1]+1, nextRottent);
        }
        return nextRottent;
    }

    private void addIfPossibleToRot(int[][] ar, Integer i, Integer j, List<Integer[]> nextRottent) {
        if (i!=null && i>=0 && i<ar.length && j!=null && j>=0 && j<ar[i].length) {
            if (ar[i][j]==1) {
                ar[i][j] = 2;
                Integer[] location = {i, j};
                nextRottent.add(location);
            }
        }
    }
}
