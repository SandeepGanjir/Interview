package arrays;
import java.util.*;

/**
 * Given a Array of integers, we have to find the 1st element
 * such that all elements on its left are smaller than the element
 * and all on its right are larger that the element.
 * i.e. smallest r such that A[i] < A[r] <= A[j]), for 0 <= i < r < j <= (n-1).
 * Input : {6, 1, 4, 3, 5, 7, 10, 8, 9}
 * Output : 7
 *  @author Sandeep Ganjir
 */
public class RightPosition {
    public static void main(String[] args) {
        RightPosition ins = new RightPosition();
        int[] Ar = {6, 1, 4, 3, 5, 7, 10, 8, 9};
        ins.printSolution(Ar);
        ins.printSolution(ins._generateRandom(6));
    }

    private void printSolution(int[] Ar) {
        int idx = getRightPositionElement(Ar);

        if (idx < 0)
            System.out.println("\nGiven Array : " + Arrays.toString(Ar) + "\n does not have a valid solution");
        else
            System.out.println("\nFor given Array : " + Arrays.toString(Ar) + "\n1st element at right position is : " + Ar[idx]);
    }

    public int getRightPositionElement(int[] Ar) {
        int candidateIndex = 0;
        int maxSoFar = Integer.MIN_VALUE;
        for (int i = 0; i < Ar.length; i++) {
            if (Ar[i] < Ar[candidateIndex]) {
                // We found an element lower than current candidate, so we need to look for a new candidate
                // which will have to be an element that is higher than max so far
                while (i < Ar.length && Ar[i] < maxSoFar)
                    i++;
                if (i >= Ar.length)
                    return -1;
                candidateIndex = i;
            }
            if (Ar[i] > maxSoFar)
                maxSoFar = Ar[i];
        }
        return candidateIndex;
    }

    private int[] _generateRandom(int size) {
        int[] Ar = new int[size];
        Random r = new Random();
        for (int i = 0; i < Ar.length; i++) {
            Ar[i] = r.nextInt(3 * size);
        }
        return Ar;
    }
}
