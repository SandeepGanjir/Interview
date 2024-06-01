package dp;

import java.util.Arrays;

/**
 * You are given an array prices where prices[i] is the price of a given stock on the ith day.
 * Find the maximum profit you can achieve. You may complete at most two transactions.
 * Note: You may not engage in multiple transactions simultaneously (i.e., you must sell the stock before you buy again).
 *
 * Input: prices = [3,3,5,0,0,3,1,4]
 * Output: 6
 * Explanation: Buy on day 4 (price = 0) and sell on day 6 (price = 3), profit = 3-0 = 3.
 * Then buy on day 7 (price = 1) and sell on day 8 (price = 4), profit = 4-1 = 3.
 */
public class StockProfit {
    public static void main(String[] args) {
        StockProfit ins = new StockProfit();
        int[] prices = {0,2,4,3,5,6,1,7}; //{3,3,5,0,0,3,1,4};
        int maxProfit = ins.maxProfit(prices);
        //maxProfit = ins.maxProfitOn2(prices);

        System.out.println("Maximum profit from max 2 buy&sell txns from prices:");
        System.out.println(Arrays.toString(prices) + " is : " + maxProfit);
    }

    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int[] changes = new int[prices.length];
        // O(n) & S(n)
        for (int i=1; i<prices.length; i++) changes[i] = prices[i]-prices[i-1];

        // O(n) Solution
        int[] kLeft = new int[changes.length];
        for (int i=1, sumSoFar=0, max=0; i<changes.length; i++) {
            sumSoFar += changes[i-1];
            if (sumSoFar > max) max = sumSoFar;
            if (sumSoFar < 0) sumSoFar = 0;
            kLeft[i] = max;
        }
        int[] kRight = new int[changes.length];
        for (int i=changes.length-1, sumSoFar=0, max=0; i>=0; i--) {
            sumSoFar += changes[i];
            if (sumSoFar > max) max = sumSoFar;
            if (sumSoFar < 0) sumSoFar = 0;
            kRight[i] = max;
        }
        //System.out.println(Arrays.toString(changes));
        //System.out.println(Arrays.toString(kLeft));
        //System.out.println(Arrays.toString(kRight));
        for (int i=0; i<changes.length; i++) {
            maxProfit = Math.max(maxProfit, kLeft[i]+kRight[i]);
        }
        return maxProfit;
    }

    private int maxProfitOn2(int[] prices) {
        int maxProfit = 0;
        int[] changes = new int[prices.length];
        // O(n) & S(n)
        for (int i=1; i<prices.length; i++) changes[i] = prices[i]-prices[i-1];
        //System.out.println(Arrays.toString(changes));

        // O(n * O(findMaxSubArraySum)) & S(1) i.e. O(n^2)
        for (int i=0; i<changes.length; i++) {
            // Split at i -> i.e. 0 to i & i+1 to n as 2 segments and find best profit for each
            int profit = findMaxSubArraySum(changes, 0, i);
            profit += findMaxSubArraySum(changes, i, changes.length);
            if (profit > maxProfit) maxProfit = profit;
        }
        return maxProfit;
    }

    private int findMaxSubArraySum(int[] changes, int start, int end) {
        // O(n) & S(1)
        int max = 0, sumSoFar = 0;
        for (int i=start; i<end; i++) {
            sumSoFar += changes[i];
            if (sumSoFar > max) max = sumSoFar;
            if (sumSoFar < 0) sumSoFar = 0;
        }
        return max;
    }
}
