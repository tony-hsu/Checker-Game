package qirkat;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


/**
 * Tests of the Board class.
 *
 * @author Tony Hsu
 */
public class BoardTest {

    private static final String INIT_BOARD =
            "  b b b b b\n  b b b b b\n  b b - w w\n  w w w w w\n  w w w w w";

    private
    static
    final
    String[] GAME1
            =
            {"c2-c3", "c4-c2", "c1-c3", "a3-c1", "c3-a3", "c5-c4", "a3-c5-c3"};

    private
    static
    final
    String[] GAME2
            = {"c2-c3", "c4-c2", "c1-c3", "a3-c1", "c3-a3", "c5-c4"};


    private static final String GAME1_BOARD
            = "  b b - b b\n  b - - b b\n  "
            + "- - w w w\n  w - - w w\n  w w b w w";

    private static void makeMoves(Board b, String[] moves) {
        for (String s : moves) {
            b.makeMove(Move.parseMove(s));
        }
    }

    @Test
    public void testInit1() {
        Board b0 = new Board();
        assertEquals(INIT_BOARD, b0.toString());

        System.out.println(b0.toString(true));
    }

    @Test
    public void testBuildingBoard() {
        Board b0 = new Board();
        assertEquals(PieceColor.WHITE, b0.get(13));
        assertEquals(PieceColor.BLACK, b0.get(10));

        ArrayList<Integer> result1 = new ArrayList<>();
        result1.add(5);
        result1.add(6);
        result1.add(1);
        assertEquals(result1, b0.tiles().get(0).possibleMoves());

        ArrayList<Integer> result2 = new ArrayList<>();
        result2.add(9);
        result2.add(8);
        result2.add(3);
        assertEquals(result2, b0.tiles().get(4).possibleMoves());

        ArrayList<Integer> result3 = new ArrayList<>();
        result3.add(20);
        result3.add(16);
        result3.add(10);
        assertEquals(result3, b0.tiles().get(15).possibleMoves());

        ArrayList<Integer> result4 = new ArrayList<>();
        result4.add(17);
        result4.add(18);
        result4.add(16);
        result4.add(13);
        result4.add(8);
        result4.add(11);
        result4.add(6);
        result4.add(7);
        assertEquals(result4, b0.tiles().get(12).possibleMoves());

        ArrayList<Integer> result5 = new ArrayList<>();
        result5.add(21);
        result5.add(16);
        result5.add(15);
        assertEquals(result5, b0.tiles().get(20).possibleMoves());
    }


    @Test
    public void testMoves1() {
        Board b0 = new Board();
        makeMoves(b0, GAME1);
        assertEquals(GAME1_BOARD, b0.toString());
    }

    @Test
    public void testLegalMoves() {
        Board b0 = new Board();
        makeMoves(b0, GAME1);

        Move first = Move.move('c', '1', 'c', '2');
        assertFalse(b0.legalMove(first));

        Move second = Move.move('a', '3', 'c', '5');
        Move secondH = Move.move('c', '5', 'c', '2');
        second = Move.move(second, secondH);
        assertFalse(b0.legalMove(second));

        Move third = Move.move('e', '2', 'c', '2');
        assertFalse(b0.legalMove(third));

        Move fourth = Move.move('d', '2', 'c', '2');
        assertFalse(b0.legalMove(fourth));
        Move fourthH = Move.move('c', '2', 'd', '2');
        assertFalse(b0.legalMove(fourthH));

        Move fifth = Move.move('b', '2', 'c', '2');
        assertFalse(b0.legalMove(fifth));
    }

    @Test
    public void testGetMoves() {
        Board b1 = new Board();
        ArrayList<Move> result2 = b1.getMoves();
        System.out.println(result2);

        Board b0 = new Board();
        makeMoves(b0, GAME2);
        System.out.println(b0.getMoves());
    }

    @Test
    public void testGetJumps() {
        Board b0 = new Board();
        makeMoves(b0, GAME2);

        Move first = Move.move('b', '4', 'b', '3');
        b0.setWhoseMove(b0.whoseMove().opposite());
        b0.makeMove(first);
        Move second = Move.move('b', '5', 'b', '4');
        b0.setWhoseMove(b0.whoseMove().opposite());
        b0.makeMove(second);

        System.out.println(b0.toString());
        System.out.println(b0.getMoves());

    }


    @Test
    public void testMoveDiagonally() {
        Board b0 = new Board();


        System.out.println(b0.getMoves());
    }

    @Test
    public void testUndo() {
        Board b0 = new Board();
        Board b1 = new Board(b0);
        makeMoves(b0, GAME1);
        Board b2 = new Board(b0);
        for (int i = 0; i < GAME1.length; i += 1) {
            b0.undo();
        }
        assertEquals("failed to return to start", b1, b0);
        makeMoves(b0, GAME1);
        assertEquals("second pass failed to reach same position", b2, b0);
    }

}
