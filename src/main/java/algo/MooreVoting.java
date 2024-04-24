package algo;
import java.util.*;

/**
 * Given an array, write an algorithm that gives majority element.
 * Input : {3, 3, 4, 2, 4, 4, 2, 4, 4}
 * Output : Majority element (4) if exist, null otherwise
 * Expected Time Complexity: O(N).
 * Expected Auxiliary Space: O(1).
 *  @author Sandeep Ganjir
 */
public class MooreVoting {
    public static void main(String[] Args) {
        int[] Ar = {3, 3, 4, 2, 4, 4, 2, 4, 4};
        MooreVoting ins = new MooreVoting();

        System.out.println("The Majority element for : " + Arrays.toString(Ar));
        System.out.println(ins.mooreMajority(Ar));
    }

    private Integer mooreMajority(int[] Ar) {
        int current = Ar[0];
        int count = 1;
        for (int i = 1; i < Ar.length; i++) {
            count += current == Ar[i] ? 1 : -1;
            if (count < 0) {
                current = Ar[i];
                count = 0;
            }
        }
        count = 0;
        for (int i = 0; i < Ar.length; i++) {
            count += current == Ar[i] ? 1 : 0;
        }
        return (2 * count > Ar.length ? current : null);
    }
}
