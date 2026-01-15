/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.chess;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;

import java.util.Iterator;
import java.util.Vector;


/**
 * ChessGame provides all external functions needed to play chess. It
 * contains a ChessBoard which represents the game status. It overwrites
 * equals() and hashCode() to properly support serialized games.
 *
 * @author Holger Antelmann
 */
public class ChessGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -767028146326411959L;

    /**
     * DOCUMENT ME!
     */
    final static int DRAW = 2;

    /**
     * DOCUMENT ME!
     */
    private ChessBoard board;

    /**
     * DOCUMENT ME!
     */
    private ChessMove tempMove = null;

    /** used to enable black to move first in case the board was set up */
    boolean blackMovesFirst = false;

    /**
     * Creates a new ChessGame object.
     */
    public ChessGame() {
        this("default chess game");
    }

    /**
     * Creates a new ChessGame object.
     *
     * @param name DOCUMENT ME!
     */
    public ChessGame(String name) {
        super(name, 2);
        board = new ChessBoard(this);
    }

/**
     * can be used to instanciate a game where the board was set up manually
     */
    ChessGame(String name, ChessPiece[] pieces, boolean whiteToMove) {
        super(name, 2);
        board = new ChessBoard(this, pieces);

        if (!whiteToMove) {
            blackMovesFirst = true;
        }
    }

    /**
     * overwritten to provide package access
     */
    protected void resetLists() {
        super.resetLists();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (gameOver()) {
            if ((getMoveHistory().length > 0) &&
                    (((ChessMove) getLastMove()).check()) &&
                    (getLegalMoves().length == 0)) {
                return (new int[] { getLastPlayer() });
            }

            return null; // it's a draw, so no winner
                         // (so that game strategies don't try to achieve this)
        }

        return null; // game not over and no winner yet - obviously
    }

    /**
     * Current limitations: gameOver() doesn't currently check for
     * things like insufficient material for checkmate or 3rd repititions.
     *
     * @return DOCUMENT ME!
     */
    public boolean gameOver() {
        if (getMoveHistory().length == 0) {
            return false;
        }

        if ((((ChessMove) getLastMove()).check()) &&
                board.findKing(nextPlayer()).underAttack()) {
            return true;
        }

        // todo: also consider: stalemate, insufficient material for mate, 3rd repitition, etc.
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        int next = (numberOfMoves() % 2);

        if (blackMovesFirst) {
            next = (next == 0) ? 1 : 0;
        }

        return next;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameMove[] listLegalMoves() {
        Vector<ChessMove> moves = new Vector<ChessMove>();
        int player = nextPlayer();
        ChessPiece[] pieces = board.getPieces();

        for (int i = 0; i < pieces.length; i++) {
            if (pieces[i].getColor() == player) {
                Iterator pieceMoves = pieces[i].legalMoves().iterator();

                while (pieceMoves.hasNext()) {
                    moves.add((ChessMove) pieceMoves.next());
                }
            }
        }

        return moves.toArray(new ChessMove[moves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        ChessMove move;

        if (tempMove == null) {
            move = (ChessMove) getLastMove();
        } else {
            move = tempMove;
        }

        BoardPosition piecePos = (BoardPosition) move.getNewPosition();

        if (move.isEnpassant()) {
            switch (move.getPlayer()) {
            case ChessBoard.WHITE:
                piecePos = piecePos.relativePosition(0, 1);

                break;

            case ChessBoard.BLACK:
                piecePos = piecePos.relativePosition(0, -1);

                break;
            }
        }

        if (board.getPieceAt(piecePos).undoMove(move)) {
            if (move.getPromotion() != null) {
                board.getPieceAt((BoardPosition) move.getOldPosition())
                     .removeFromBoard();
                (new Pawn(move.getPlayer(), board,
                    (BoardPosition) move.getOldPosition())).placeOnBoard(board,
                    (BoardPosition) move.getOldPosition());
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        ChessMove m = null;

        try {
            m = (ChessMove) move;
        } catch (ClassCastException e) {
            return false;
        }

        ChessPiece piece = board.getPieceAt((BoardPosition) m.getOldPosition());
        BoardPosition pos = (BoardPosition) m.getNewPosition();

        if (m.getPromotion() != null) {
            return (((Pawn) piece).promotionMoveTo(pos, m.getPromotion()));
        }

        return (piece.moveTo(pos));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ChessBoard getBoard() {
        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     */
    void setTempMove(ChessMove move) {
        tempMove = move;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean kingSideCastleEnabled(int color) {
        GameMove[] history = getMoveHistory();
        int pos1 = ((color == ChessBoard.WHITE) ? 81 : 88);
        int pos2 = ((color == ChessBoard.WHITE) ? 51 : 58);
        int pos;

        for (int i = 0; i < history.length; i++) {
            pos = ((ChessMove) history[i]).getOldPosition().asInteger();

            if ((pos == pos1) || (pos == pos2)) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean queenSideCastleEnabled(int color) {
        GameMove[] history = getMoveHistory();
        int pos1 = ((color == ChessBoard.WHITE) ? 11 : 18);
        int pos2 = ((color == ChessBoard.WHITE) ? 51 : 58);
        int pos;

        for (int i = 0; i < history.length; i++) {
            pos = ((ChessMove) history[i]).getOldPosition().asInteger();

            if ((pos == pos1) || (pos == pos2)) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    char enpassantEnableColumn() {
        if (numberOfMoves() == 0) {
            return 'x'; // just a non-valid row
        }

        ChessMove m = (ChessMove) getLastMove();

        if ((m.getPieceChar() == 'P') &&
                ((Math.abs(m.getOldPosition().asInteger() -
                    m.getNewPosition().asInteger()) % 10) == 2)) {
            return ((BoardPosition) m.getOldPosition()).getColumn();
        } else {
            return 'x'; // just a non-valid row
        }
    }

    /**
     * This implementation of equals() does currently not care about
     * the move history, it only considers the state of the board. That means
     * that a board may be considered equal even though in one case a castling
     * or enpassant move is possible and not in the other.
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof ChessGame)) {
            return false;
        }

        ChessGame g = (ChessGame) obj;

        if (!board.equals(g.board)) {
            return false;
        }

        if (nextPlayer() != g.nextPlayer()) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return board.hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = board.toString();
        s += "Next Player: ";

        switch (nextPlayer()) {
        case ChessBoard.WHITE:
            s += "White\n\n";

            break;

        case ChessBoard.BLACK:
            s += "Black\n\n";

            break;

        default:
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        ChessGame newGameBoard = (ChessGame) super.clone();
        newGameBoard.board = new ChessBoard(newGameBoard, board.getPieces());

        return newGameBoard;
    }
}
