package qirkat;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static qirkat.Move.*;

public class MoreBoardTests {

    private static
    final
    String[] GAME1 = {"b3-c3", "b2-b3", "b4-b2"};

    private
    static
    final
    String[] GAME2 = {"d3-c3", "b3-d3", "c2-c3"};

    private
    static
    final
    String[] GAME23
            = {"c2-c3", "c4-c2", "c1-c3", "a3-c1", "c3-a3", "c5-c4"};
    private final char[][] boardRepr = new char[][]{
            {'b', 'b', 'b', 'b', 'b'},
            {'b', 'b', 'b', 'b', 'b'},
            {'b', 'b', '-', 'w', 'w'},
            {'w', 'w', 'w', 'w', 'w'},
            {'w', 'w', 'w', 'w', 'w'}
    };
    private final PieceColor currMove = PieceColor.WHITE;

    private static void makeMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.makeMove(Move.parseMove(s));
        }
    }

    /**
     * @return the String representation of the initial state. This will
     * be a string in which we concatenate the values from the bottom of
     * board upwards, so we can pass it into setPieces. Read the comments
     * in Board#setPieces for more information.
     * <p>
     * For our current boardRepr, the String returned
     * by getInitialRepresentation is
     * "  w w w w w\n  w w w w w\n  b b - w w\n  b b b b b\n  b b b b b"
     * <p>
     * We use a StringBuilder to avoid recreating Strings (because Strings
     * are immutable).
     */
    private String getInitialRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        for (int i = boardRepr.length - 1; i >= 0; i--) {
            for (int j = 0; j < boardRepr[0].length; j++) {
                sb.append(boardRepr[i][j] + " ");
            }
            sb.deleteCharAt(sb.length() - 1);
            if (i != 0) {
                sb.append("\n  ");
            }
        }
        return sb.toString();
    }

    private Board getBoard() {
        Board b = new Board();
        b.setPieces(getInitialRepresentation(), currMove);
        return b;
    }

    private void resetToInitialState(Board b) {
        b.setPieces(getInitialRepresentation(), currMove);
    }

    /*
    @Test
    public void testSomething() {
        Board b = getBoard();
        makeMoves(b, GAME1);
        System.out.println(b);
        resetToInitialState(b);
        assertEquals(b.toString(), getBoard().toString());
    }

    @Test
    public void test2() {
        Board b = getBoard();
        makeMoves(b, GAME2);
        System.out.println(b);
        resetToInitialState(b);
        assertEquals(b.toString(), getBoard().toString());
    }
    */

    @Test
    public void testGetMoves() {
        Board b1 = new Board();
        ArrayList<Move> result2 = b1.getMoves();
        System.out.println(result2);

        Board b0 = new Board();
        makeMoves(b0, GAME23);
        System.out.println(b0.getMoves());
    }

    @Test
    public void testGetJumps() {
        Board b0 = new Board();
        makeMoves(b0, GAME23);

        System.out.println(b0.getMoves());
    }

}
