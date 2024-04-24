package dp;
import java.util.*;

/**
 *  0-1 Knapsack problem:
 *   Given weights and values of n items, put these items in a knapsack
 *    of capacity W to get the maximum total value in the knapsack.
 *   Input :
 *    Values : {20, 5, 10, 40, 15, 25}
 *    Weights : {1, 2, 3, 8, 7, 4}
 *    Capacity : 10
 *   Output :    60
 *
 *
 *  Merry wants to carry some fruits in the knapsack to get maximum profit.
 *   Here are the weights and profits of the fruits:
 *  Items: { Apple, Orange, Banana, Melon }
 *  Weights: { 2, 3, 1, 4 }
 *  Profits: { 4, 5, 3, 7 }
 *  Knapsack capacity: 5
 */
public class Knapsack {
    public static void main(String[] args) {
        int[] weights = {1, 2, 3, 8, 7, 4};
        int[] profits = {20, 5, 10, 40, 15, 25};
        int capacity = 10;
        Knapsack ins = new Knapsack();
        System.out.println("0-1 Knapsack solution for the problem is : " +
                ins.knapsack(weights, profits, capacity));
    }

    /**
     * Dynamic Programming approach will be:
     * dp[i][c] = Max( dp[i-1][c], profits[i] + dp[i-1][c-weights[i]] )
     */
    private int knapsack(int[] weights, int[] profits, int capacity) {
        // invalid input case
        if (weights.length != profits.length)
            return -1;

        // trivial case, no need to proceed
        if (capacity == 0)
            return 0;

        // O(capacity) storage solution, since we only need row i and i-1
        int[][] dp = new int[2][capacity + 1];
        // we don't need to initialize the dp array 0 as java takes care of this

        // process for all items
        for (int j = 0; j < weights.length; j++) {
            // process for all capacities
            for (int i = 1; i <= capacity; i++) {
                int profitIfIncluded = (i < weights[j]) ? 0 : profits[j] + dp[j % 2][i - weights[j]];
                dp[(j + 1) % 2][i] = Math.max(dp[j % 2][i], profitIfIncluded);
            }
            // System.out.println(Arrays.toString(dp[(j+1)%2]));
        }

        return dp[weights.length % 2][capacity];
    }
}
