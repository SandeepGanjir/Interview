package arrays;

import java.util.Arrays;
import java.util.Random;

/**
 * Minimum halls required for class scheduling
 * Given N lecture timings, with their start time and end time (both inclusive),
 * the task is to find the minimum number of halls required to hold all the classes such that a single hall
 * can be used for only one lecture at a given time. Note that the maximum end time can be 10^5.
 * Examples:
 * All lectures must be held in different halls because
 * At time instance 1 all lectures are ongoing.
 * Input: lectures[][] = {{8, 12}, {4, 8}, {4, 10}, {12, 18}, {0, 1}, {8, 11}, {12, 18}, {13, 16}}
 * Output: 3
 */
public class MinLectureHalls {
    public static void main(String[] args) {
        MinLectureHalls ins = new MinLectureHalls();
        int[][] lectureTimes = new int[8][2];
        System.out.println("With Lecture timings as :- ");
        ins.populateRandom(lectureTimes);
        System.out.println("Minimum number of Lecture Halls required to hold all the classes is : "
                + ins.minHallsRequired(lectureTimes));
    }

    private void populateRandom(int[][] lectureTimes) {
        Random r = new Random();
        for (int i = 0; i < lectureTimes.length; i++) {
            lectureTimes[i][0] = r.nextInt(lectureTimes.length * 2);
            lectureTimes[i][1] = lectureTimes[i][0] + r.nextInt(lectureTimes.length);
            System.out.println(Arrays.toString(lectureTimes[i]));
        }
    }

    private int minHallsRequired(int[][] lectures) {
        // get Max time Value
        int max = 0;
        for (int i = 0; i < lectures.length; i++) {
            if (max < lectures[i][1])
                max = lectures[i][1];
        }

        // arrange all slots on timeline
        int[] timeline = new int[max + 1];
        for (int i = 0; i < lectures.length; i++) {
            if (lectures[i][0] >= 0) timeline[lectures[i][0]]++;
            if (lectures[i][1] < timeline.length) timeline[lectures[i][1]]--;
        }

        // calculate max halls used concurrently
        int minHalls = 0;
        int currentHalls = 0;
        for (int i = 0; i < timeline.length; i++) {
            currentHalls += timeline[i];
            if (minHalls < currentHalls)
                minHalls = currentHalls;
        }
        return minHalls;
    }
}
