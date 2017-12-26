package qirkat;

import java.util.ArrayList;


import static qirkat.PieceColor.*;

/**
 * A Player that computes its own moves.
 *
 * @author Tony Hsu
 */
class AI extends Player {

    /**
     * Maximum minimax search depth before going to static evaluation.
     */
    private static final int MAX_DEPTH = 5;
    /**
     * A position magnitude indicating a win (for white if positive, black
     * if negative).
     */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 1;
    /**
     * A magnitude greater than a normal value.
     */
    private static final int INFTY = Integer.MAX_VALUE;
    /**
     * The move found by the last call to one of the ...FindMove methods
     * below.
     */
    private Move _lastFoundMove;

    /**
     * A new AI for GAME that will play MYCOLOR.
     */
    AI(Game game, PieceColor myColor) {
        super(game, myColor);
    }

    @Override
    Move myMove() {
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();


        return move;
    }

    /**
     * Return a move for me from the current position, assuming there
     * is a move.
     */
    private Move findMove() {
        Board b = new Board(board());
        if (myColor() == WHITE) {
            findMove(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            findMove(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /**
     * Find a move from position BOARD and return its value, recording
     * the move found in _lastFoundMove iff SAVEMOVE. The move
     * should have maximal value or have value > BETA if SENSE==1,
     * and minimal value or value < ALPHA if SENSE==-1. Searches up to
     * DEPTH levels.  Searching at level 0 simply returns a static estimate
     * of the board value and does not set _lastMoveFound.
     * Reference was from the wikipedia.
     */
    private int findMove(Board board, int depth, boolean saveMove, int sense,
                         int alpha, int beta) {
        Move best;
        best = null;
        if ((depth == 0) || board.gameOver()) {
            return staticScore(board);
        }
        ArrayList<Move> possibleMoves = board.getMoves();
        ArrayList<Move> filteredMoves = filterColorMoves(
                possibleMoves, board);
        int v;
        int score;
        if (sense == 1) {
            v = -INFTY;
            for (Move cur : filteredMoves) {
                Board temp = new Board(board);
                temp.makeMove(cur);
                score = findMove(temp, depth - 1, false, sense * -1,
                        alpha, beta);
                v = Math.max(score, v);
                alpha = Math.max(alpha, v);
                if (score == v) {
                    best = cur;
                }
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            v = INFTY;
            for (Move cur : filteredMoves) {
                Board temp = new Board(board);
                temp.makeMove(cur);
                score = findMove(temp, depth - 1, false, sense * -1,
                        alpha, beta);
                v = Math.min(score, v);
                beta = Math.min(beta, v);
                if (score == v) {
                    best = cur;
                }
                if (beta <= alpha) {
                    break;
                }
            }
        }
        if (saveMove) {
            _lastFoundMove = best;
        }
        return v;
    }

    /**
     * Return a heuristic value for BOARD.
     * @param cur ArrayList<Move>
     * @param board Board
     * @return ArrayList<Move>
     */
    private ArrayList<Move> filterColorMoves(ArrayList<Move> cur, Board board) {
        ArrayList<Move> result = new ArrayList<>();
        PieceColor temp = board.whoseMove();
        for (Move i : cur) {
            int fromIndex = i.fromIndex();
            PieceColor curColor = board.get(fromIndex);
            if (curColor.equals(temp)) {
                result.add(i);
            }
        }
        return result;
    }


    /**
     * Return a heuristic value for BOARD.
     */
    private int staticScore(Board board) {
        int result = 0;
        int result2 = 0;
        for (int i = 0; i < Move.MAX_INDEX; i++) {
            PieceColor cur = board.get(i);
            if (cur.equals(WHITE)) {
                result = result + 1;
            } else if (cur.equals(BLACK)) {
                result2 = result2 + 1;
            }
        }
        return (result - result2);
    }
}
