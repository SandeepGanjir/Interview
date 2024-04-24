package arrays;

import java.util.TreeSet;

/**
 * Given an n-element array of integers, A, and an integer, m, determine the
 * maximum value of the sum of any of its subarrays modulo m. This means that
 * you must find the sum of each subarray %m, then print the maximum result of
 * this modulo operation for any of the n(n+1)/2 possible subarrays.
 * <p>
 * Sample Input: Ar = {3, 3, 9, 9, 5}   m = 7
 * Output: 6
 */
public class SubarrayModSum {
    public static void main(String[] args) {
        long[] a = {3, 3, 9, 9, 5};
        long result = maximumSum(a, 7);
        System.out.println(result);
    }

    static long maximumSum(long[] a, long m) {
        long result = 0;
        int n = a.length;
        long[] sumArr = new long[n];
        result = sumArr[0] = a[0] % m;
        TreeSet<Long> ts = new TreeSet<>();
        ts.add(sumArr[0]);
        for (int i = 1; i < n; i++) {
            sumArr[i] = (sumArr[i - 1] + a[i]) % m;
            ts.add(sumArr[i]);
            result = Math.max(result, sumArr[i]);
            Long rightNeighbour = ts.ceiling(sumArr[i] + 1l);
            if (rightNeighbour != null)
                result = Math.max(result, (sumArr[i] - rightNeighbour + m) % m);
        }
        return result;
    }
}
