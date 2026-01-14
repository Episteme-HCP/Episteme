/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.computing.game.checkers;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;

import java.util.Vector;


/**
 * an implementation of the game checkers.
 *
 * @author Holger Antelmann
 */
public class CheckersGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 878531751489546944L;

    /**
     * DOCUMENT ME!
     */
    CheckersBoard board;

    /**
     * DOCUMENT ME!
     */
    private Vector<GameMove> followupMoves;

    /**
     * Creates a new CheckersGame object.
     */
    public CheckersGame() {
        this("Checkers game");
    }

    /**
     * Creates a new CheckersGame object.
     *
     * @param name DOCUMENT ME!
     */
    public CheckersGame(String name) {
        super(name, 2);
        board = new CheckersBoard();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        if (getFollowupMoves().size() > 0) {
            return (CheckersMove[]) getFollowupMoves()
                                        .toArray(new CheckersMove[getFollowupMoves()
                                                                      .size()]);
        }

        Vector<GameMove> moves = new Vector<GameMove>();
        int next = nextPlayer();
        CheckersPiece piece;
        int position;
        boolean promotion;

        // capture moves first
        for (int i = 0; i < CheckersBoard.POSITIONS.length; i++) {
            position = CheckersBoard.POSITIONS[i];
            piece = board.getPieceAt(position);

            if ((piece != null) && (piece.getPlayer() == next)) {
                if ((piece.getPlayer() == 0) || (piece.isKing())) {
                    try { // up left

                        if (board.getPieceAt(position - 18) == null) {
                            piece = board.getPieceAt(position - 9);

                            if ((piece != null) && (piece.getPlayer() != next)) {
                                promotion = (!board.getPieceAt(position).isKing() &&
                                    (((position - 18) % 10) == 8));
                                moves.add(new CheckersMove(next, position,
                                        position - 18,
                                        board.getPieceAt(position - 9),
                                        promotion));
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try { //up right

                        if (board.getPieceAt(position + 22) == null) {
                            piece = board.getPieceAt(position + 11);

                            if ((piece != null) && (piece.getPlayer() != next)) {
                                promotion = (!board.getPieceAt(position).isKing() &&
                                    (((position + 22) % 10) == 8));
                                moves.add(new CheckersMove(next, position,
                                        position + 22,
                                        board.getPieceAt(position + 11),
                                        promotion));
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }

                piece = board.getPieceAt(position);

                if ((piece.getPlayer() == 1) || (piece.isKing())) {
                    try { // down left

                        if (board.getPieceAt(position - 22) == null) {
                            piece = board.getPieceAt(position - 11);

                            if ((piece != null) && (piece.getPlayer() != next)) {
                                promotion = (!board.getPieceAt(position).isKing() &&
                                    (((position - 22) % 10) == 1));
                                moves.add(new CheckersMove(next, position,
                                        position - 22,
                                        board.getPieceAt(position - 11),
                                        promotion));
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try { // down right

                        if (board.getPieceAt(position + 18) == null) {
                            piece = board.getPieceAt(position + 9);

                            if ((piece != null) && (piece.getPlayer() != next)) {
                                promotion = (!board.getPieceAt(position).isKing() &&
                                    (((position + 18) % 10) == 1));
                                moves.add(new CheckersMove(next, position,
                                        position + 18,
                                        board.getPieceAt(position + 9),
                                        promotion));
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        // enforcing 'zugzwang' for capture moves
        if (moves.size() > 0) {
            return (CheckersMove[]) moves.toArray(new CheckersMove[moves.size()]);
        }

        // checking other moves
        for (int i = 0; i < CheckersBoard.POSITIONS.length; i++) {
            position = CheckersBoard.POSITIONS[i];
            piece = board.getPieceAt(position);

            if ((piece != null) && (piece.getPlayer() == next)) {
                if ((piece.getPlayer() == 0) || (piece.isKing())) {
                    try { // up left

                        if (board.getPieceAt(position - 9) == null) {
                            promotion = (!board.getPieceAt(position).isKing() &&
                                (((position - 9) % 10) == 8));
                            moves.add(new CheckersMove(next, position,
                                    position - 9, promotion));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try { // up right

                        if (board.getPieceAt(position + 11) == null) {
                            promotion = (!board.getPieceAt(position).isKing() &&
                                (((position + 11) % 10) == 8));
                            moves.add(new CheckersMove(next, position,
                                    position + 11, promotion));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }

                piece = board.getPieceAt(position);

                if ((piece.getPlayer() == 1) || (piece.isKing())) {
                    try { // down left

                        if (board.getPieceAt(position - 11) == null) {
                            promotion = (!board.getPieceAt(position).isKing() &&
                                (((position - 11) % 10) == 1));
                            moves.add(new CheckersMove(next, position,
                                    position - 11, promotion));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }

                    try { // down right

                        if (board.getPieceAt(position + 9) == null) {
                            promotion = (!board.getPieceAt(position).isKing() &&
                                (((position + 9) % 10) == 1));
                            moves.add(new CheckersMove(next, position,
                                    position + 9, promotion));
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
            }
        }

        return (CheckersMove[]) moves.toArray(new CheckersMove[moves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        followupMoves = null;

        CheckersMove m = (CheckersMove) move;
        board.setPieceAt(board.getPieceAt(m.getFrom()), m.getTo());
        board.setPieceAt(null, m.getFrom());

        if (m.getTaken() != null) {
            board.setPieceAt(null, m.getFrom() -
                ((m.getFrom() - m.getTo()) / 2));
        }

        if (m.getPromotion()) {
            board.getPieceAt(m.getTo()).setKing(true);
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        followupMoves = null;

        CheckersMove m = (CheckersMove) getLastMove();
        board.setPieceAt(board.getPieceAt(m.getTo()), m.getFrom());
        board.setPieceAt(null, m.getTo());

        if (m.getTaken() != null) {
            board.setPieceAt(m.getTaken(),
                m.getFrom() - ((m.getFrom() - m.getTo()) / 2));
        }

        if (m.getPromotion()) {
            board.getPieceAt(m.getFrom()).setKing(false);
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        if (numberOfMoves() == 0) {
            return 0;
        }

        if (getFollowupMoves().isEmpty()) {
            return (getLastMove().getPlayer() == 0) ? 1 : 0;
        } else {
            return getLastMove().getPlayer();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Vector<GameMove> getFollowupMoves() {
        if (followupMoves != null) {
            return followupMoves;
        }

        followupMoves = new Vector<GameMove>(3);

        if (numberOfMoves() == 0) {
            return followupMoves;
        }

        if (((CheckersMove) getLastMove()).getTaken() == null) {
            return followupMoves;
        }

        if (((CheckersMove) getLastMove()).getPromotion()) {
            return followupMoves;
        }

        boolean promotion;
        int position = ((CheckersMove) getLastMove()).getTo();
        int oldPosition = ((CheckersMove) getLastMove()).getFrom();
        int player = getLastMove().getPlayer();
        CheckersPiece piece = board.getPieceAt(position);

        if ((piece.getPlayer() == 0) || (piece.isKing())) {
            // up left
            try {
                if (((position - 18) != oldPosition) &&
                        (board.getPieceAt(position - 18) == null)) {
                    piece = board.getPieceAt(position - 9);

                    if ((piece != null) && (piece.getPlayer() != player)) {
                        promotion = (!board.getPieceAt(position).isKing() &&
                            (((position - 18) % 10) == 8));
                        followupMoves.add(new CheckersMove(player, position,
                                position - 18, board.getPieceAt(position - 9),
                                promotion));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            //up right
            try {
                if (((position + 22) != oldPosition) &&
                        (board.getPieceAt(position + 22) == null)) {
                    piece = board.getPieceAt(position + 11);

                    if ((piece != null) && (piece.getPlayer() != player)) {
                        promotion = (!board.getPieceAt(position).isKing() &&
                            (((position + 22) % 10) == 8));
                        followupMoves.add(new CheckersMove(player, position,
                                position + 22, board.getPieceAt(position + 11),
                                promotion));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        piece = board.getPieceAt(position);

        if ((piece.getPlayer() == 1) || (piece.isKing())) {
            // down left
            try {
                if (((position - 22) != oldPosition) &&
                        (board.getPieceAt(position - 22) == null)) {
                    piece = board.getPieceAt(position - 11);

                    if ((piece != null) && (piece.getPlayer() != player)) {
                        promotion = (!board.getPieceAt(position).isKing() &&
                            (((position - 22) % 10) == 1));
                        followupMoves.add(new CheckersMove(player, position,
                                position - 22, board.getPieceAt(position - 11),
                                promotion));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            // down right
            try {
                if (((position + 18) != oldPosition) &&
                        (board.getPieceAt(position + 18) == null)) {
                    piece = board.getPieceAt(position + 9);

                    if ((piece != null) && (piece.getPlayer() != player)) {
                        promotion = (!board.getPieceAt(position).isKing() &&
                            (((position + 18) % 10) == 1));
                        followupMoves.add(new CheckersMove(player, position,
                                position + 18, board.getPieceAt(position + 9),
                                promotion));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }

        return followupMoves;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        // solves both cases:
        // a) if no pieces are left for a player or
        // b) if player cannot move given the piece positions
        if (getLegalMoves().length != 0) {
            return null;
        }

        return new int[] { ((nextPlayer() == 0) ? 1 : 0) };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CheckersBoard getBoard() {
        return board;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = board.toString();
        s += "\nnext player: ";

        switch (nextPlayer()) {
        case 0:
            s += "X";

            break;

        case 1:
            s += "O";

            break;

        default:
            throw new Error();
        }

        if (getFollowupMoves().size() != 0) {
            s += " (follow up at: ";
            s += (((CheckersMove) getLastMove()).getTo() + ")");
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
        CheckersGame ng = (CheckersGame) super.clone();
        ng.board = (CheckersBoard) board.clone();
        ng.followupMoves = new Vector<GameMove>(followupMoves);

        return ng;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof CheckersGame)) {
            return false;
        }

        CheckersGame o = (CheckersGame) obj;

        if (!board.equals(o.board)) {
            return false;
        }

        if (o.nextPlayer() != nextPlayer()) {
            return false;
        }

        return true;
    }
}
