package arrays;
import java.util.*;

/**
 * Minimum capacity to ship packages in order in D-days.
 * Given a weight array, write an algorithm that gives required minimum capacity to ship cargo in d days.
 * Input : {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}; D = 5
 * Output : 15
 * Expected Time Complexity: O(N log N).
 * Expected Auxiliary Space: O(N).
 *  @author Sandeep Ganjir
 */
public class CargoShip {
    private int minCapacity(int[] Ar, int days) {
        int maxWeight = Integer.MIN_VALUE;
        int sumOfWeight = 0;
        for (int i = 0; i < Ar.length; i++) {
            sumOfWeight += Ar[i];
            if (maxWeight < Ar[i])
                maxWeight = Ar[i];
        }
        if (days == 1)
            return sumOfWeight;
        if (days >= Ar.length)
            return maxWeight;

        int minLimit = Math.max(maxWeight, sumOfWeight / days);
        int maxLimit = sumOfWeight;
        while (minLimit < maxLimit) {
            int midCap = (minLimit + maxLimit) / 2;
            int expectedDays = getExpectedDays(Ar, midCap);
            if (expectedDays > days) {
                minLimit = midCap + 1;
            } else {
                maxLimit = midCap;
            }
        }
        return minLimit;
    }

    private int getExpectedDays(int[] Ar, int capacity) {
        int days = 1;
        int usedCap = 0;
        for (int i = 0; i < Ar.length; i++) {
            usedCap += Ar[i];
            if (usedCap > capacity) {
                days++;
                usedCap = Ar[i];
            }
        }
        return days;
    }

    public static void main(String[] Args) {
        CargoShip ins = new CargoShip();
        int[] Ar = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int days = 5;

        System.out.print("Minimum capacity to ship packages in order of : " + Arrays.toString(Ar) + " in " + days + "-days : ");
        System.out.println(ins.minCapacity(Ar, days));
    }
}
