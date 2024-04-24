package dp;
import java.util.*;

/**
 * Given 2 arrays find longest discontiguous overlapping subsequence.
 * e.g. A = {1, 11, 2, 10, 4, 5, 2, 1} and B = {21, 2, 19, 11, 1, 4, 5, 10, 2, 2, 1}
 * Result > 5       (11, 4, 5, 2, 1)
 */
public class LongestOverlappingSubsequence {
    private long counter = 0;

    public static void main (String[] args) throws Exception {
        LongestOverlappingSubsequence instance = new LongestOverlappingSubsequence();
        int[] A = {1, 11, 2, 10, 4, 5, 2, 1};
        int[] B = {21, 2, 19, 11, 1, 4, 5, 10, 2, 2, 1};
        instance.optimumSoln(A, B);
    }

    private int optimumSoln (int[] A, int[] B) {
        int [][] S = new int[A.length+1][B.length+1];
        for (int i=0; i<A.length; i++) {
            for (int j=0; j<B.length; j++) {
                if (i==0 && j==0) continue;
                if (A[i] == B[j]) {
                    S[i+1][j+1] = S[i][j]+1;
                } else {
                    S[i+1][j+1] = Math.max(S[i+1][j], S[i][j+1]);
                }
            }
            System.out.println(Arrays.toString(S[i+1]));
        }
        return S[A.length][B.length];
    }
}
