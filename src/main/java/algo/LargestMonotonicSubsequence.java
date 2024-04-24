package algo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Given a sequence (array) of n numbers. Erdos Szekeres Theorem says
 * there will be monotonic subsequence of at least Sqrt(n) length.
 * We need to find the length of largest monotonic subsequence.
 * Input : 1, 5, 2, 8, 50, 30, 25, 11, 40
 * Output : 5
 * Expected Time Complexity: O(N log N).
 * Expected Auxiliary Space: O(N).
 *
 * @author Sandeep Ganjir
 */
public class LargestMonotonicSubsequence {
    public static void main(String[] Args) {
        LargestMonotonicSubsequence ins = new LargestMonotonicSubsequence();
        int[] Ar = {1, 5, 2, 8, 50, 30, 25, 11, 40};

        System.out.println("The length of longest monotonic susequenece for : " + Arrays.toString(Ar));
        System.out.println(ins.longestMonotonicSubsequence(Ar));
    }

    private int longestMonotonicSubsequence(int[] Ar) {
        List<Integer> result = new ArrayList<>();
        result.add(Ar[0]);
        for (int i = 1; i < Ar.length; i++) {
            if (Ar[i] > result.get(result.size() - 1))
                result.add(Ar[i]);
            else
                searchAndReplace(result, Ar[i]);
        }
        System.out.println(result);
        return result.size();
    }

    private void searchAndReplace(List<Integer> list, int value) {
        int beg = 0;
        int end = list.size() - 1;
        while (beg <= end) {
            int mid = (beg + end) / 2;
            if (list.get(mid) < value) {
                beg = mid + 1;
            } else {
                if (mid == 0 || list.get(mid - 1) < value)
                    list.set(mid, value);
                end = mid - 1;
            }
        }
    }
}
