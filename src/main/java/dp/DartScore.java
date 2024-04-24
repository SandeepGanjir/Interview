package dp;
import java.util.*;

/**
 * Given a rotating circular dart board where only one score number is targetable through a slit at time.
 *  We can see all score numbers. But since rotation speed is high we cannot target adjacent scores.
 *  Find the highest obtainable score in a single rotation of board.
 * Ar : [18, 16, 17, 5, 7, 12, 11, 3]
 * Output > 54
 */
public class DartScore {
    public static void main (String[] args) {
        int[] Ar = {18, 16, 17, 5, 7, 12, 11, 7};
        System.out.println("Max achievable score for - " +
                Arrays.toString(Ar) + " is : " + maxDartScore(Ar));
    }

    private static int maxDartScore(int[] Ar) {
        if (Ar == null || Ar.length < 1) {
            return 0;
        }

        int bestSofar = 0;
        int[][] soln = new int[Ar.length][2];
        soln[0][0] = Ar[0];
        soln[0][1] = 0;

        for (int i=1; i<Ar.length; i++) {
            soln[i][0] = Math.max(bestSofar, soln[i-1][1]) + Ar[i];
            soln[i][1] = Math.max(soln[i-1][0], soln[i-1][1]);
            bestSofar = soln[i-1][0];
        }
        bestSofar = Math.max(soln[Ar.length-1][0], soln[Ar.length-1][1]);

        return bestSofar;
    }
}
