package lld;

import java.util.regex.Pattern;

public class Chess {
    public static void main(String[] args) {
        Chess game = new Chess();
    }

    private void nextMove(String next) {
        Pattern p = Pattern.compile("[a-hA-H][1-8]");
        switch(next) {
            case "ll" : // Surrender
                break;
            case "oo" :
            case "OO" : // Small Castling
                break;
            case "ooo" :
            case "OOO" : // Large Castling
                break;
            default:
                if (p.matcher(next).matches()) {
                    // Check for move Validity
                } else {
                    // Take input again
                }
        }
    }

    private void getBox (String pos) {
        Pattern p = Pattern.compile("[a-hA-H][1-8]");
        if (p.matcher(pos).matches()) {
            // Check for move Validity
        } else {
            // Take input again
        }
    }
}
