package dp;
import java.util.Arrays;

/**
 * Given a Array of integers, we have to find the maximum value of
 * Max(A[i] - A[j] + A[k]), such that i < j < k.
 * Input : [3, 10, 4, 18, 7, 10, 14, 14, 1, 11]
 * Output : 28
 *  @author Sandeep Ganjir
 */
public class ArrayOptimize {
    public static void main(String[] args) {
        ArrayOptimize ins = new ArrayOptimize();

        int[] Ar = {14, 7, 19, 8, 12, 12, 3, 8, 9, 14};//{3, 10, 4, 18, 7, 10, 14, 14, 1, 11};
        System.out.println("Given : \n" + Arrays.toString(Ar));
        System.out.println("Maximum value is : " + ins.getOptimum(Ar));
    }

    public int getOptimum(int[] Ar) {
        Integer[] opt = optimize(Ar, null, false);
        System.out.println("Solution : \n" + Arrays.toString(opt));

        opt = optimize(Ar, opt, true);
        System.out.println(Arrays.toString(opt));

        opt = optimize(Ar, opt, false);
        System.out.println(Arrays.toString(opt));

        int result = Integer.MIN_VALUE;
        for (int i = 0; i < opt.length; i++) {
            if (opt[i] != null && result < opt[i])
                result = opt[i];
        }
        return result;
    }

    private Integer[] optimize(int[] Ar, Integer[] prev, boolean doSubtract) {
        Integer[] result = new Integer[Ar.length];
        if (prev == null) {
            prev = new Integer[Ar.length];
            Arrays.fill(prev, 0);
            result[0] = doSubtract ? -Ar[0] : Ar[0];
        }
        for (int i = 1; i < Ar.length; i++) {
            if (prev[i - 1] == null)
                result[i] = null;
            else if (result[i - 1] == null)
                result[i] = prev[i - 1] + (doSubtract ? -Ar[i] : Ar[i]);
            else
                result[i] = Math.max(result[i - 1], prev[i - 1] + (doSubtract ? -Ar[i] : Ar[i]));
        }
        return result;
    }
}
