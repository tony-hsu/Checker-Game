package qirkat;

import static qirkat.PieceColor.*;
import static qirkat.Command.Type.*;

/** A Player that receives its moves from its Game's getMoveCmnd method.
 *  @author Tony Hsu
 */
class Manual extends Player {

    /** A Player that will play MYCOLOR on GAME, taking its moves from
     *  GAME. */
    Manual(Game game, PieceColor myColor) {
        super(game, myColor);
        _prompt = myColor + ": ";
    }

    @Override
    Move myMove() {
        Command cur = game().getMoveCmnd(_prompt);
        if (cur != null) {
            String[] move = cur.operands();
            Move returnMove = Move.parseMove(move[0]);
            return returnMove;
        }
        return null;
    }

    /** Identifies the player serving as a source of input commands. */
    private String _prompt;
}

