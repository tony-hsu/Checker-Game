/* Author: Paul N. Hilfinger.  (C) 2008. */

package qirkat;


import org.junit.Test;
import static org.junit.Assert.*;

import static qirkat.Move.*;

/** Test Move creation.
 *  @author Tony Hsu
 */
public class MoveTest {

    @Test
    public void testMove1() {
        Move m = move('a', '3', 'b', '2');
        assertNotNull(m);
        assertFalse("move should not be jump", m.isJump());
    }

    @Test
    public void testJump1() {
        Move m = move('a', '3', 'a', '5');
        assertNotNull(m);
        assertTrue("move should be jump", m.isJump());
    }

    @Test
    public void testString() {
        assertEquals("a3-b2", move('a', '3', 'b', '2').toString());
        assertEquals("a3-a5", move('a', '3', 'a', '5').toString());
        assertEquals("a3-a5-c3", move('a', '3', 'a', '5',
                                      move('a', '5', 'c', '3')).toString());
    }

    @Test
    public void testParseString() {
        assertEquals("a3-b2", parseMove("a3-b2").toString());
        assertEquals("a3-a5", parseMove("a3-a5").toString());
        assertEquals("a3-a5-c3", parseMove("a3-a5-c3").toString());
        assertEquals("a3-a5-c3-e1", parseMove("a3-a5-c3-e1").toString());
    }

    @Test
    public void testConcatenate() {
        Move m = move('a', '1', 'a', '3');
        Move n = move('a', '3', 'a', '5');
        Move result = move(m, n);
        Move p = move('a', '5', 'c', '5');
        Move result2 = move(result, p);
        assertEquals("a1-a3-a5", result.toString());
        assertEquals("a1-a3-a5-c5", result2.toString());


        Move k = move('a', '1', 'a', '1');
        Move result3 = move(k, n);
        assertEquals("a1-a3-a5", result3.toString());
    }

    @Test
    public void testIfLeftMove() {
        Move m = move('a', '3', 'b', '3');
        assertFalse(m.isLeftMove());
        assertTrue(m.isRightMove());

        Move n = move('a', '3', 'c', '3');
        assertFalse(n.isRightMove());


        Move k = move('c', '3', 'b', '4');
        assertFalse(k.isLeftMove());
    }

    @Test
    public void testJumpedPieces() {
        Move m = move('a', '1', 'c', '3');
        assertEquals(m.jumpedCol(), 'b');
        assertEquals(m.jumpedRow(), '2');

        Move n = move('a', '3', 'c', '1');
        assertEquals(n.jumpedCol(), 'b');
        assertEquals(n.jumpedRow(), '2');

    }

    @Test
    public void testIdentity() {
        Move m = move('a', '1', 'c', '3');
        Move n;
        n = IDENTITY.apply(m);
        m = move(m, move('c', '3', 'c', '5'));
        n = IDENTITY.apply(m);
        m = move('a', '1', 'c', '3');

        assertEquals(n.toString(), "a1-c3-c5");
        assertEquals(m.toString(), "a1-c3");
    }




}
