package qirkat;
import java.util.Stack;
import java.util.Observer;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Formatter;


import static qirkat.PieceColor.*;
import static qirkat.Move.*;

/** A Qirkat board.   The squares are labeled by column (a char value between
 *  'a' and 'e') and row (a char value between '1' and '5'.
 *
 *  For some purposes, it is useful to refer to squares using a single
 *  integer, which we call its "linearized index".  This is simply the
 *  number of the square in row-major order (with row 0 being the bottom row)
 *  counting from 0).
 *
 *  Moves on this board are denoted by Moves.
 *  @author Tony Hsu
 */
class Board extends Observable {

    /** A new, cleared board at the start of the game. */
    Board() {
        tiles = new ArrayList<>();
        clear();
        for (int i = 0; i <= MAX_INDEX; i++) {
            ArrayList<Integer>  posMoves = possibleMovesGen(i);
            tiles.set(i, new Tile(0, posMoves));
        }
        setPieces("  w w w w w\n  w w w w w\n  "
                + "b b - w w\n  b b b b b\n  b b b b b", WHITE);
    }

    /** A copy of B. */
    Board(Board b) {
        internalCopy(b);
    }

    /** Generating all the possible moves at a tile.
     * @param n int
     * @return ArrayList<Integer>
     * */
    ArrayList<Integer> possibleMovesGen(int n) {
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
            if (!(((n + 1) % 5) == 0)) {
                result.add(n + 1);
                if (!(n < 5)) {
                    result.add(n - 4);
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
            if (!(((n + 1) % 5) == 0)) {
                result.add(n + 1);
            }
            if (!(((n % 5) == 0))) {
                result.add(n - 1);
            }
            if (!(n < 5)) {
                result.add(n - 5);
            }
        }
        return result;
    }

    /** Return a constant view of me (allows any access method, but no
     *  method that modifies it). */
    Board constantView() {
        return this.new ConstantBoard();
    }

    /** Clear me to my starting state, with pieces in their initial
     *  positions. */
    void clear() {
        _whoseMove = WHITE;
        _gameOver = false;
        tiles = new ArrayList<>();
        for (int i = 0; i <= MAX_INDEX; i++) {
            ArrayList<Integer>  posMoves = possibleMovesGen(i);
            tiles.add(new Tile(0, posMoves));
        }
        setPieces("  w w w w w\n  w w w w w\n  "
                + "b b - w w\n  b b b b b\n  b b b b b", whoseMove());
        setChanged();
        notifyObservers();
    }

    /** Copy B into me. */
    void copy(Board b) {
        internalCopy(b);
    }

    /** Copy B into me. */
    private void internalCopy(Board b) {
        this.tiles = new ArrayList<>();
        for (int i = 0; i < b.tiles.size(); i++) {
            Tile fromTile = b.tiles.get(i);
            ArrayList<Integer> newPossibleMoves = new ArrayList<>();
            for (int j : fromTile.possibleMoves()) {
                newPossibleMoves.add(j);
            }
            Tile newTile =  new Tile(fromTile.direction(),
                    newPossibleMoves);
            newTile.setColor(fromTile.getColor());
            this.tiles.add(newTile);
        }
        _whoseMove = b._whoseMove;
        _gameOver = b._gameOver;
    }

    /** Set _gameover.
     * @param cur boolean */
    void setGameOver(boolean cur) {
        _gameOver = cur;
    }

    /** Set my contents as defined by STR.  STR consists of 25 characters,
     *  each of which is b, w, or -, optionally interspersed with whitespace.
     *  These give the contents of the Board in row-major order, starting
     *  with the bottom row (row 1) and left column (column a). All squares
     *  are initialized to allow horizontal movement in either direction.
     *  NEXTMOVE indicates whose move it is.
     */
    void setPieces(String str, PieceColor nextMove) {
        if (nextMove == EMPTY || nextMove == null) {
            throw new IllegalArgumentException("bad player color");
        }
        str = str.replaceAll("\\s", "");
        if (!str.matches("[bw-]{25}")) {
            throw new IllegalArgumentException("bad board description");
        }

        for (int k = 0; k < str.length(); k += 1) {
            switch (str.charAt(k)) {
            case '-':
                set(k, EMPTY);
                break;
            case 'b': case 'B':
                set(k, BLACK);
                break;
            case 'w': case 'W':
                set(k, WHITE);
                break;
            default:
                break;
            }
        }

        setWhoseMove(nextMove);



        setChanged();
        notifyObservers();
    }

    /** Return true iff the game is over: i.e., if the current player has
     *  no moves. */
    boolean gameOver() {
        return _gameOver;
    }

    /** Return the current contents of square C R, where 'a' <= C <= 'e',
     *  and '1' <= R <= '5'.  */
    PieceColor get(char c, char r) {
        assert validSquare(c, r);
        return get(index(c, r));
    }

    /** Return the current contents of the square at linearized index K. */
    PieceColor get(int k) {
        assert validSquare(k);
        return tiles.get(k).getColor();
    }

    /** Set get(C, R) to V, where 'a' <= C <= 'e', and
     *  '1' <= R <= '5'. */
    private void set(char c, char r, PieceColor v) {
        assert validSquare(c, r);
        set(index(c, r), v);
    }

    /** Set get(K) to V, where K is the linearized index of a square. */
    private void set(int k, PieceColor v) {
        assert validSquare(k);
        tiles.get(k).setColor(v);

    }

    /** Set get(K) to V, where K is the linearized index of a square.
     *  Also includes direction
     *  @param k int
     *  @param dir int
     *  @param v PieceColor
     *  */
    private void set(int k, PieceColor v, int dir) {
        assert validSquare(k);
        tiles.get(k).setColor(v);
        tiles.get(k).setDirection(dir);

    }

    /** Return true iff cur MOV is legal on the current board.
     * @param mov Move
     * @return boolean
     * */
    boolean legalMoveHelper(Move mov) {
        int from = mov.fromIndex();
        int to = mov.toIndex();
        Tile fromTile = tiles.get(from);
        if (jumpPossible()) {
            return false;
        }
        if (!fromTile.possibleMoves().contains(mov.toIndex())) {
            return false;
        }
        if (fromTile.direction() != 0) {
            if (mov.isLeftMove() || mov.isRightMove()) {
                ArrayList<Integer> posDirMoves =
                        fromTile.possDirMoves(from);
                if (!posDirMoves.contains(to)) {
                    return false;
                }
            }
        }
        if (!get(to).equals(EMPTY)) {
            return false;
        }
        if (mov.isRightMove()) {
            if ((from > (10 + 9)) && get(from).equals(WHITE)) {
                return false;
            }
            if ((from < 5) && get(from).equals(BLACK)) {
                return false;
            }
        }
        if (mov.isLeftMove()) {
            if ((from > (10 + 9)) && get(from).equals(WHITE)) {
                return false;
            }
            if ((from < 5) && get(from).equals(BLACK)) {
                return false;
            }
        }
        if (get(from).equals(BLACK)) {
            ArrayList<Integer> upward = fromTile.upwardMovs(from);
            if (upward.contains(to)) {
                return false;
            }
        }
        if (get(from).equals(WHITE)) {
            ArrayList<Integer> downward = fromTile.downwardMovs(from);
            if (downward.contains(to)) {
                return false;
            }
        }
        return true;
    }



    /** Return true iff MOV is legal on the current board.
     * @param mov Move
     * @return boolean
     * */
    boolean legalMove(Move mov) {
        if (!(validSquare(mov.fromIndex()) && validSquare(mov.toIndex()))) {
            return false;
        }
        if (mov.isVestigial()) {
            return true;
        }
        if (!(get(mov.fromIndex()).equals(whoseMove()))) {
            return false;
        }
        if (mov.isJump()) {
            PieceColor cur =  get(mov.fromIndex());
            Board temp = new Board(this);
            while (mov != null) {
                int from = mov.fromIndex();
                Tile fromTile = temp.tiles.get(from);
                ArrayList<Integer> possJumps = fromTile.possibleJumps(from);
                if (!possJumps.contains(mov.toIndex())) {
                    return false;
                }
                PieceColor jumped = temp.get(mov.jumpedIndex());
                if (!temp.get(mov.toIndex()).equals(EMPTY)) {
                    return false;
                }
                if (!jumped.equals(cur.opposite())) {
                    return false;
                }
                temp.tiles.get(mov.fromIndex()).setColor(EMPTY);
                temp.tiles.get(mov.jumpedIndex()).setColor(EMPTY);
                mov = mov.jumpTail();
            }
            return true;
        } else {
            return legalMoveHelper(mov);
        }

    }

    /** Return a list of all legal moves from the current position. */
    ArrayList<Move> getMoves() {
        ArrayList<Move> result = new ArrayList<>();
        getMoves(result);
        return result;
    }

    /** Add all legal moves from the current position to MOVES. */
    void getMoves(ArrayList<Move> moves) {
        if (gameOver()) {
            return;
        }
        if (jumpPossible()) {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                getJumps(moves, k);
            }
        } else {
            for (int k = 0; k <= MAX_INDEX; k += 1) {
                getMoves(moves, k);
            }
        }
    }

    /** Add all legal non-capturing moves from the position
     *  with linearized index K to MOVES. */
    private void getMoves(ArrayList<Move> moves, int k) {

        ArrayList<Integer> posMovs = possibleMovesGen(k);
        for (int i : posMovs) {
            Move potentialAdd = move(col(k), row(k), col(i), row(i));
            if (legalMove(potentialAdd)) {
                moves.add(potentialAdd);
            }
        }
    }

    /** Add all legal captures from the position with linearized index K
     *  to MOVES. */
    private void getJumps(ArrayList<Move> moves, int k) {
        Board newTemp = new Board();
        newTemp.copy(this);

        ArrayList<Move> result = contiJump(new ArrayList<Move>(), k, newTemp);
        moves.addAll(result);

    }

    /** Concatenating all the possible moves.
     * @param k int
     * @param movs ArrayList<Move>
     * @param temp Board
     * @return ArrayList<Move>
     *  */
    ArrayList<Move> contiJump(ArrayList<Move> movs, int k, Board temp) {
        ArrayList<Move> result = new ArrayList<>();
        ArrayList<Integer> initJumps = tiles.get(k).possibleJumps(k);
        for (int jump : initJumps) {
            Move potentialAdd = move(col(k), row(k), col(jump), row(jump));
            if (temp.legalMove(potentialAdd)) {
                Board newTemp = new Board();
                newTemp.copy(temp);
                ArrayList<Move> newMovs = new ArrayList<>();
                if (!movs.isEmpty()) {
                    for (Move addingMov : movs) {
                        Move newMov = IDENTITY.apply(addingMov);
                        newMov = move(newMov, potentialAdd);
                        newMovs.add(newMov);
                    }
                } else {
                    newMovs.add(potentialAdd);
                }
                newTemp.makeMove(potentialAdd);
                newTemp.setWhoseMove(newTemp.whoseMove().opposite());
                newMovs = contiJump(newMovs, jump, newTemp);
                result.addAll(newMovs);
            }
        }
        if (result.isEmpty()) {
            return movs;
        }
        return result;
    }

    /** Return true iff MOV is a valid jump sequence on the current board.
     *  MOV must be a jump or null.  If ALLOWPARTIAL, allow jumps that
     *  could be continued and are valid as far as they go.  */
    boolean checkJump(Move mov, boolean allowPartial) {
        if (!allowPartial) {
            return legalMove(mov);
        } else {
            PieceColor cur =  get(mov.fromIndex());
            while (mov != null) {
                PieceColor jumped = get(mov.jumpedIndex());
                if (!get(mov.toIndex()).equals(EMPTY)) {
                    return false;
                }
                if (!jumped.equals(cur.opposite())) {
                    return false;
                }
                mov = mov.jumpTail();
            }
            return true;
        }

    }

    /** Return true iff a jump is possible for a piece at position C R. */
    boolean jumpPossible(char c, char r) {
        return jumpPossible(index(c, r));
    }

    /** Return true iff a jump is possible for a piece at position with
     *  linearized index K. */
    boolean jumpPossible(int k) {
        if (!validSquare(k)) {
            return false;
        }
        ArrayList<Move> result = new ArrayList<>();
        getJumps(result, k);
        return !(result.size() == 0);

    }

    /** Return true iff a jump is possible from the current board. */
    boolean jumpPossible() {
        for (int k = 0; k <= MAX_INDEX; k += 1) {
            if (jumpPossible(k)) {
                return true;
            }
        }
        return false;
    }

    /** Return the color of the player who has the next move.  The
     *  value is arbitrary if gameOver(). */
    PieceColor whoseMove() {
        return _whoseMove;
    }

    /** Perform the move C0R0-C1R1. Assumes that legalMove(C0, R0, C1, R1). */
    void makeMove(char c0, char r0, char c1, char r1) {
        makeMove(Move.move(c0, r0, c1, r1, null));
    }

    /** Make the multi-jump C0 R0-C1 R1..., where NEXT is C1R1....
     *  Assumes the result is legal. */
    void makeMove(char c0, char r0, char c1, char r1, Move next) {
        makeMove(Move.move(c0, r0, c1, r1, next));
    }

    /** Make the Move MOV on this Board, assuming it is legal. */
    void makeMove(Move mov) {
        assert legalMove(mov);
        PieceColor cur = get(mov.fromIndex());
        trackMoves.push(mov);
        if (!mov.isJump()) {
            if (mov.isRightMove()) {
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur, 1);
            } else if (mov.isLeftMove()) {
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur, -1);
            } else {
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur);
            }
        } else {
            while (mov != null) {
                cur = get(mov.fromIndex());
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur);
                set(mov.jumpedIndex(), EMPTY, 0);
                mov = mov.jumpTail();
            }
        }

        _whoseMove = _whoseMove.opposite();
        setChanged();
        notifyObservers();
    }

    /** Make the Move MOV on this Board, not assuming it is legal. */
    void makeMoveSetup(Move mov) {
        PieceColor cur = get(mov.fromIndex());
        trackMoves.push(mov);
        if (!mov.isJump()) {
            if (mov.isRightMove()) {
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur, 1);
            } else if (mov.isLeftMove()) {
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur, -1);
            } else {
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur);
            }
        } else {
            while (mov != null) {
                cur = get(mov.fromIndex());
                set(mov.fromIndex(), EMPTY, 0);
                set(mov.toIndex(), cur);
                set(mov.jumpedIndex(), EMPTY, 0);
                mov = mov.jumpTail();
            }
        }

        _whoseMove = _whoseMove.opposite();
        setChanged();
        notifyObservers();
    }

    /** Return true iff MOV is legal on the current board.
     * @param mov Move
     * @return boolean
     * */
    boolean legalMoveSetup(Move mov) {
        if (!(validSquare(mov.fromIndex()) && validSquare(mov.toIndex()))) {
            return false;
        }
        if (mov.isVestigial()) {
            return true;
        }
        if ((get(mov.fromIndex()).equals(EMPTY))) {
            return false;
        }
        if (mov.isJump()) {
            PieceColor cur =  get(mov.fromIndex());
            while (mov != null) {
                int from = mov.fromIndex();
                Tile fromTile = tiles.get(from);
                ArrayList<Integer> possJumps = fromTile.possibleJumps(from);
                if (!possJumps.contains(mov.toIndex())) {
                    return false;
                }
                PieceColor jumped = get(mov.jumpedIndex());
                if (!get(mov.toIndex()).equals(EMPTY)) {
                    return false;
                }
                if (!jumped.equals(cur.opposite())) {
                    return false;
                }
                mov = mov.jumpTail();
            }
            return true;
        } else {
            return legalMoveHelper(mov);
        }
    }

    /** Undo the last the last jump.
     * @param beginning int
     * @param mov Move
     * @param jumpedList ArrayList<Integer>
     * */
    void undoJump(Move mov, ArrayList<Integer> jumpedList, int beginning) {
        jumpedList.add(mov.jumpedIndex());
        if (mov.jumpTail() == null) {
            PieceColor cur = tiles.get(mov.toIndex()).getColor();
            for (int jumped : jumpedList) {
                set(jumped, cur.opposite());
            }
            tiles.get(mov.toIndex()).setColor(EMPTY);
            set(beginning, cur);
        } else {
            undoJump(mov.jumpTail(), jumpedList, beginning);
        }
    }

    /** Undo the last move, if any.*/
    void undo() {
        Move lastMove = (Move) trackMoves.pop();
        if (lastMove != null) {
            if (lastMove.isJump()) {
                undoJump(lastMove, new ArrayList<Integer>(),
                        lastMove.fromIndex());
            } else {
                Move dirIndicator;
                if (!trackMoves.empty()) {
                    dirIndicator = (Move) trackMoves.peek();
                } else {
                    dirIndicator = null;
                }
                Tile cur = tiles.get(lastMove.toIndex());
                int curDir = cur.direction();
                PieceColor curColor = cur.getColor();
                if (lastMove.isRightMove()) {
                    if (dirIndicator != null && dirIndicator.isRightMove()) {
                        set(lastMove.fromIndex(), curColor, curDir);
                        set(lastMove.toIndex(), EMPTY, 0);
                    } else {
                        set(lastMove.fromIndex(), curColor, 0);
                        set(lastMove.toIndex(), EMPTY, 0);
                    }
                } else if (lastMove.isLeftMove()) {
                    if (dirIndicator != null && dirIndicator.isLeftMove()) {
                        set(lastMove.fromIndex(), curColor, curDir);
                        set(lastMove.toIndex(), EMPTY, 0);
                    } else {
                        set(lastMove.fromIndex(), curColor, 0);
                        set(lastMove.toIndex(), EMPTY, 0);
                    }
                } else {
                    set(lastMove.fromIndex(), curColor, 0);
                    set(lastMove.toIndex(), EMPTY, 0);
                }
            }
        }
        _whoseMove = _whoseMove.opposite();
        updateGameOver();
        if (_gameOver) {
            _gameOver = false;
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return toString(false);
    }

    /** Return a text depiction of the board.  If LEGEND, supply row and
     *  column numbers around the edges. */
    String toString(boolean legend) {
        Formatter out = new Formatter();

        String[] col = new String[]{"a", "b", "c", "d", "e"};
        String[] row = new String[]{"1", "2", "3", "4", "5"};

        for (int i = 4; i > -1; i--) {
            for (int j = 0; j < 5; j++) {
                int cur = i * 5 + j;
                if (legend && (cur % 5) == 0) {
                    out.format("%s", row[i]);
                    out.format("%s", " ");
                    out.format("%s", tiles.get(cur).getColor().shortName());
                } else if ((cur % 5) == 0) {
                    out.format("%s", "  ");
                    out.format("%s", tiles.get(cur).getColor().shortName());
                } else {
                    out.format("%s", " ");
                    out.format("%s", tiles.get(cur).getColor().shortName());
                    if (j == 4 && (cur != 4)) {
                        out.format("%s", "\n");
                    }
                }
            }

        }
        if (legend) {
            out.format("%s", "\n");
            out.format("%s", "  ");
            for (int i = 0; i < 5; i++) {
                out.format("%s", col[i]);
                if (i != 4) {
                    out.format(" ");
                }
            }
        }
        return out.toString();
    }

    /** Updates if game is over. */
    public void updateGameOver() {
        if (!isMove()) {
            _gameOver = true;
        } else {
            _gameOver = false;
        }
    }

    /** Return true iff there is a move for the current player. */
    private boolean isMove() {
        return !getMoves().isEmpty();

    }

    /** Returns all the tiles.
     * @return ArrayList<Tile>
     * */
    public ArrayList<Tile> tiles() {
        return tiles;
    }

    /** Stores all the tiles in the board. */
    private ArrayList<Tile> tiles;

    /** Stores all the moves in the board. */
    private Stack<Move> trackMoves = new Stack<>();

    /** Return all the whose move on the board.
     * @param cur PieceColor
     * */
    void setWhoseMove(PieceColor cur) {
        _whoseMove = cur;
    }

    /** Player that is on move. */
    private PieceColor _whoseMove;

    /** Set true when game ends. */
    private boolean _gameOver;

    /** Convenience value giving values of pieces at each ordinal position. */
    static final PieceColor[] PIECE_VALUES = PieceColor.values();

    /** One cannot create arrays of ArrayList<Move>, so we introduce
     *  a specialized private list type for this purpose. */
    private static class MoveList extends ArrayList<Move> {
    }

    @Override
    public int hashCode() {
        return (tiles.hashCode() << 5)
                |
                tiles.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Board) {
            Board b = (Board) o;
            ArrayList<Integer> firstDirections = new ArrayList<>();
            ArrayList<Integer> secondDirections = new ArrayList<>();
            ArrayList<PieceColor> firstColors = new ArrayList<>();
            ArrayList<PieceColor> secondColors = new ArrayList<>();
            for (int i = 0; i < tiles.size(); i++) {
                firstDirections.add(tiles.get(i).direction());
                secondDirections.add(b.tiles.get(i).direction());
                firstColors.add(tiles.get(i).getColor());
                secondColors.add(b.tiles.get(i).getColor());
            }
            boolean directionsEqual = firstDirections.equals(secondDirections);
            boolean colorsComparison = firstColors.equals(secondColors);
            boolean whoseMoveComparison = _whoseMove.equals(b.whoseMove());
            return colorsComparison && whoseMoveComparison && directionsEqual;
        } else {
            return false;
        }
    }

    /** A read-only view of a Board. */
    private class ConstantBoard extends Board implements Observer {
        /** A constant view of this Board. */
        ConstantBoard() {
            super(Board.this);
            Board.this.addObserver(this);
        }

        @Override
        void copy(Board b) {
            assert false;
        }

        @Override
        void clear() {
            assert false;
        }

        @Override
        void makeMove(Move move) {
            assert false;
        }

        /** Undo the last move. */
        @Override
        void undo() {
            assert false;
        }

        @Override
        public void update(Observable obs, Object arg) {
            super.copy((Board) obs);
            setChanged();
            notifyObservers(arg);
        }
    }
}
