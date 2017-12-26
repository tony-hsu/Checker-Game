package qirkat;

import java.util.ArrayList;

/** Represents a Qirkat tile. There is a tile for
 * every index on the board. It contains possible moves
 * and directions.
 *  @author Tony Hsu
 */
class Tile {

    /** Represents the color of the tile.
     */
    private PieceColor color;

    /** Represents the direction of the tile.
     */
    private int direction;

    /** Represents all the possible directions at
     * the tile.
     */
    private ArrayList<Integer>  possibleMoves;

    /** Constructor for the tile class.
     * @param dir Int.
     * @param moves ArrayList<Integer>.
     */
    Tile(int dir, ArrayList<Integer> moves) {
        direction = dir;
        possibleMoves = moves;
    }

    /** Sets the color for the tile.
     * @param col PieceColor
     */
    void setColor(PieceColor col) {
        color = col;
    }

    /** Returns the direction of the tile.
     */
    public int direction() {
        return direction;
    }

    /** Returns the possibleMoves at the tile.
     */
    public ArrayList<Integer> possibleMoves() {
        return possibleMoves;
    }

    /** Returns the color at the tile.
     */
    PieceColor getColor() {
        return color;
    }


    /** Returns the possible direction moves.
     * @param n int.
     */
    ArrayList<Integer> possDirMoves(int n) {
        ArrayList<Integer> result = new ArrayList<>();
        if (direction > 0) {
            if ((n % 2) == 0) {
                if (!(n > (10 + 9))) {
                    result.add(n + 5);
                    if (!(((n + 1) % 5) == 0)) {
                        result.add(n + 6);
                    }
                }
                if (!(((n + 1) % 5) == 0)) {
                    result.add(n + 1);
                    if (!(n < 5)) {
                        result.add(n - 4);
                    }
                }
                if (!(n < 5)) {
                    result.add(n - 5);
                }
            } else {
                if (!(n > (10 + 9))) {
                    result.add(n + 5);
                }
                if (!(((n + 1) % 5) == 0)) {
                    result.add(n + 1);
                }
                if (!(n < 5)) {
                    result.add(n - 5);
                }
            }
        } else {
            possibleLeftMoves(result, n);
        }
        return result;
    }

    /** Generates the possible left moves.
     * @param result ArrayList<Integer>
     * @param n int
     */
    void possibleLeftMoves(ArrayList<Integer> result, int n) {
        if ((n % 2) == 0) {
            if (!(n > (10 + 9))) {
                result.add(n + 5);
                if (!(((n % 5) == 0))) {
                    result.add(n + 4);
                }
            }
            if (!(((n % 5) == 0))) {
                result.add(n - 1);
                if (!(n < 5)) {
                    result.add(n - 6);
                }
            }
            if (!(n < 5)) {
                result.add(n - 5);
            }
        } else {
            if (!(n > (10 + 9))) {
                result.add(n + 5);
            }
            if (!(((n % 5) == 0))) {
                result.add(n - 1);
            }
            if (!(n < 5)) {
                result.add(n - 5);
            }
        }
    }

    /** Generates the possible jumps.
     * @param n int
     * @return ArrayList<Integer>
     */
    ArrayList<Integer> possibleJumps(int n) {
        ArrayList<Integer> result = new ArrayList<>();
        if ((n % 2) == 0) {
            if (!(n > (10 + 4))) {
                result.add(n + 5 * 2);
                if ((!(((n + 1) % 5) == 0)) && (!(((n + 2) % 5) == 0))) {
                    result.add(n + 6 * 2);
                }
                if ((!(((n % 5) == 0))) && (!((((n - 1) % 5) == 0)))) {
                    result.add(n + 4 * 2);
                }
            }
            if ((!(((n + 1) % 5) == 0)) && (!(((n + 2) % 5) == 0))) {
                result.add(n + 1 * 2);
                if (!(n < 10)) {
                    result.add(n - 4 * 2);
                }
            }
            if ((!(((n % 5) == 0))) && (!((((n - 1) % 5) == 0)))) {
                result.add(n - 1 * 2);
                if (!(n < 10)) {
                    result.add(n - 6 * 2);
                }
            }
            if (!(n < 10)) {
                result.add(n - 5 * 2);
            }
        } else {
            if (!(n > (10 + 4))) {
                result.add(n + 5 * 2);
            }
            if ((!(((n + 1) % 5) == 0)) && (!(((n + 2) % 5) == 0))) {
                result.add(n + 1 * 2);
            }
            if ((!(((n % 5) == 0))) && (!((((n - 1) % 5) == 0)))) {
                result.add(n - 1 * 2);
            }
            if (!(n < 10)) {
                result.add(n - 5 * 2);
            }
        }
        ArrayList<Integer> toRemove = removeNotIndex(result);
        result.removeAll(toRemove);
        return result;
    }

    /** Generates indexes that are out of bounds.
     * @param indexes ArrayList<Integer>
     * @return ArrayList<Integer>
     */
    ArrayList<Integer> removeNotIndex(ArrayList<Integer> indexes) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        for (int k = 0; k < indexes.size(); k++) {
            int cur = indexes.get(k);
            if (cur > Move.MAX_INDEX || cur < 0) {
                toRemove.add(cur);
            }
        }
        return toRemove;
    }

    /** Sets the direction at the tile.
     * @param dir int
     */
    void setDirection(int dir) {
        direction = dir;
    }

    /** Generates possible upward moves.
     * @param n int
     * @return ArrayList<Integer>
     */
    ArrayList<Integer> upwardMovs(int n) {
        ArrayList<Integer> result = new ArrayList<>();
        if ((n % 2) == 0) {
            if (!(n > (10 + 9))) {
                result.add(n + 5);
                if (!(((n + 1) % 5) == 0)) {
                    result.add(n + 6);
                }
                if (!(((n % 5) == 0))) {
                    result.add(n + 4);
                }
            }
        } else {
            if (!(n > (10 + 9))) {
                result.add(n + 5);
            }
        }
        return result;
    }

    /** Generates the possible downward videos.
     * @param n int
     * @return ArrayList<Integer>
     */
    ArrayList<Integer> downwardMovs(int n) {
        ArrayList<Integer> result = new ArrayList<>();
        if ((n % 2) == 0) {
            if (!(((n + 1) % 5) == 0)) {
                if (!(n < 5)) {
                    result.add(n - 4);
                }
            }
            if (!(((n % 5) == 0))) {
                if (!(n < 5)) {
                    result.add(n - 6);
                }
            }
            if (!(n < 5)) {
                result.add(n - 5);
            }
        } else {
            if (!(n < 5)) {
                result.add(n - 5);
            }
        }
        return result;
    }


}
