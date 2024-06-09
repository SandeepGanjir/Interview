package arrays;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Given a sudoku board, find right solution.
 * Input > int[][] board = {
 * {8, 0, 0, 0, 0, 0, 0, 0, 0},
 * {0, 0, 3, 6, 0, 0, 0, 0, 0},
 * {0, 7, 0, 0, 9, 0, 2, 0, 0},
 * {0, 5, 0, 0, 0, 7, 0, 0, 0},
 * {0, 0, 0, 0, 4, 5, 7, 0, 0},
 * {0, 0, 0, 1, 0, 0, 0, 3, 0},
 * {0, 0, 1, 0, 0, 0, 0, 6, 8},
 * {0, 0, 8, 5, 0, 0, 0, 1, 0},
 * {0, 9, 0, 0, 0, 0, 4, 0, 0}
 * };
 * Output >
 * [8, 1, 2, 7, 5, 3, 6, 4, 9]
 * [9, 4, 3, 6, 8, 2, 1, 7, 5]
 * [6, 7, 5, 4, 9, 1, 2, 8, 3]
 * [1, 5, 4, 2, 3, 7, 8, 9, 6]
 * [3, 6, 9, 8, 4, 5, 7, 2, 1]
 * [2, 8, 7, 1, 6, 9, 5, 3, 4]
 * [5, 2, 1, 9, 7, 4, 3, 6, 8]
 * [4, 3, 8, 5, 2, 6, 9, 1, 7]
 * [7, 9, 6, 3, 1, 8, 4, 5, 2]
 */
public class Sudoku {
    public static void main(String[] args) {
        int[][] board = {
                {8, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 3, 6, 0, 0, 0, 0, 0},
                {0, 7, 0, 0, 9, 0, 2, 0, 0},
                {0, 5, 0, 0, 0, 7, 0, 0, 0},
                {0, 0, 0, 0, 4, 5, 7, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 3, 0},
                {0, 0, 1, 0, 0, 0, 0, 6, 8},
                {0, 0, 8, 5, 0, 0, 0, 1, 0},
                {0, 9, 0, 0, 0, 0, 4, 0, 0}
        };
        Sudoku ins = new Sudoku();
        ins.solveRecursive(board);
        for (int i = 0; i < board.length; i++) {
            System.out.println(Arrays.toString(board[i]));
        }
        //System.out.println(ins.count);
    }

    private boolean solveRecursive(int[][] sudoku) {
        for (int i = 0; i < sudoku.length; i++) {
            for (int j = 0; j < sudoku[i].length; j++) {
                if (sudoku[i][j] == 0) {
                    Set<Integer> vals = generatePossibleVals(sudoku, i, j);
                    for (Integer val : vals) {
                        sudoku[i][j] = val;
                        if (solveRecursive(sudoku)) {
                            return true;
                        }
                    }
                    sudoku[i][j] = 0;
                    return false;
                }
            }
        }
        return true;
    }

    
    private Set<Integer> generatePossibleVals(int[][] sudoku, int i, int j) {
        //count++;
        Set<Integer> vals = new HashSet<>();
        for (int k = 1; k <= 9; k++) {
            vals.add(k);
        }
        for (int k = 0; k < sudoku.length; k++) {
            vals.remove(sudoku[k][j]);
        }
        for (int k = 0; k < sudoku[i].length; k++) {
            vals.remove(sudoku[i][k]);
        }
        int a = i / 3;
        int b = j / 3;
        for (int m = 0; m < 3; m++) {
            for (int n = 0; n < 3; n++) {
                vals.remove(sudoku[a * 3 + m][b * 3 + n]);
            }
        }
        return vals;
    }
}
