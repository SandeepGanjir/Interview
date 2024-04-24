package arrays;

import java.util.Arrays;

/**
 * Given n sticks of lengths l0, l1, ... ln, use of the sticks to construct a
 * non-degenerate triangle with the maximum possible perimeter. Then print the
 * lengths of its sides as space-separated integers in non-decreasing order.
 *
 * Sample Input: {1, 1, 1, 2, 3, 5}
 * Output: 1 1 1
 */
public class MaxPerimeterTriangle {
    public static void main(String[] args) {
        int[] l = {1, 1, 1, 2, 3, 5};
        int[] result = maximumPerimeterTriangle(l);
        if (result[0] != 0)
            for (int i = 0; i < result.length; i++) {
                System.out.print(result[i] + (i != result.length - 1 ? " " : ""));
            }
        else
            System.out.println("-1");
    }

    static int[] maximumPerimeterTriangle(int[] l) {
        // Complete this function
        int[] sidesArr = new int[3];
        if (l.length < 3) return sidesArr;

        // Note that if we sort the array any possible solution will be contiguous
        // due to triangle property A[i] < A[i-1] + A[i-2] and we need Max(A[i] + A[i-1] + A[i-2])
        Arrays.sort(l);

        for (int i = l.length-1; i > 1; i--) {
            if (l[i] < l[i-1] + l[i-2]) {
                sidesArr[0] = l[i-2];
                sidesArr[1] = l[i-1];
                sidesArr[2] = l[i];
                return sidesArr;
            }
        }
        return sidesArr;
    }
}
