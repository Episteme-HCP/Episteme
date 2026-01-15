/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.wolfsheep;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameBoardMove;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.chess.BoardPosition;

import java.io.Serializable;

import java.util.Vector;


/**
 * an implementation of the wolf&sheep game which is played on a chess-like
 * board; it uses the BoardPosition from the chess package for convenience.
 * The game is played with 1 'wolf' and 4 'sheeps'; the setup and rules are
 * somewhat simillar to checker, but way simpler. Wolf and sheeps start on
 * opposite sites of the board. The sheeps can only move forward diagonal; the
 * wolf can move back&forth, but only diagonal, also. Hence, each sheep has 2
 * move option at most and the wolf has 4 move options at most. The sheeps'
 * goal is to contain the wolf, so that it cannot move anymore. The wolf's
 * goal is to bypass the sheeps closing line and reach the other side of the
 * board. If the wolf moves first, the wolf represents game role 0 and the
 * sheeps represent 1. If the sheeps move first, these game roles are
 * reversed.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.chess.BoardPosition
 */
public class WolfsheepGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 72759558890034844L;

    /**
     * DOCUMENT ME!
     */
    public static final byte SHEEP = 4;

    /**
     * DOCUMENT ME!
     */
    public static final byte WOLF = 5;

    /**
     * DOCUMENT ME!
     */
    Piece wolf;

    /**
     * DOCUMENT ME!
     */
    Piece[] sheep;

    /**
     * DOCUMENT ME!
     */
    byte firstPlayer;

/**
     * initiates the game with the wolf being the first player
     */
    public WolfsheepGame() {
        this(WOLF);
    }

/**
     * can initiate the game with either the wolf or the sheeps
     * as the first player;
     *
     * @see #WOLF
     * @see #SHEEP
     */
    public WolfsheepGame(byte firstPlayer) {
        super("Wolfsheep Game", 2);
        sheep = new Piece[] {
                new Piece(SHEEP, new BoardPosition('a', 1)),
                new Piece(SHEEP, new BoardPosition('c', 1)),
                new Piece(SHEEP, new BoardPosition('e', 1)),
                new Piece(SHEEP, new BoardPosition('g', 1))
            };
        wolf = new Piece(WOLF, new BoardPosition('d', 8));
        setFirstPlayer(firstPlayer);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    byte getFirstPlayer() {
        return firstPlayer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     */
    private void setFirstPlayer(byte player) {
        if ((player != SHEEP) && (player != WOLF)) {
            throw new IllegalArgumentException();
        }

        firstPlayer = player;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        if (wolfPassed()) {
            return null;
        }

        switch (nextPlayer()) {
        case 0:

            if (firstPlayer == SHEEP) {
                return sheepMoves();
            } else {
                return wolfMoves();
            }

        case 1:

            if (firstPlayer == SHEEP) {
                return wolfMoves();
            } else {
                return sheepMoves();
            }

        default:
            throw new Error();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    GameMove[] sheepMoves() {
        Vector<Move> v = new Vector<Move>(8);

        for (int i = 0; i < sheep.length; i++) {
            BoardPosition tmp = sheep[i].getPosition().relativePosition(-1, 1);

            if (isValidTarget(tmp)) {
                v.add(new Move(nextPlayer(), sheep[i].getPosition(), tmp));
            }

            tmp = sheep[i].getPosition().relativePosition(1, 1);

            if (isValidTarget(tmp)) {
                v.add(new Move(nextPlayer(), sheep[i].getPosition(), tmp));
            }
        }

        return (Move[]) v.toArray(new Move[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    GameMove[] wolfMoves() {
        Vector<Move> v = new Vector<Move>(4);
        BoardPosition pos = wolf.getPosition().relativePosition(1, 1);

        if (isValidTarget(pos)) {
            v.add(new Move(nextPlayer(), wolf.getPosition(), pos));
        }

        pos = wolf.getPosition().relativePosition(1, -1);

        if (isValidTarget(pos)) {
            v.add(new Move(nextPlayer(), wolf.getPosition(), pos));
        }

        pos = wolf.getPosition().relativePosition(-1, 1);

        if (isValidTarget(pos)) {
            v.add(new Move(nextPlayer(), wolf.getPosition(), pos));
        }

        pos = wolf.getPosition().relativePosition(-1, -1);

        if (isValidTarget(pos)) {
            v.add(new Move(nextPlayer(), wolf.getPosition(), pos));
        }

        return v.toArray(new Move[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean isValidTarget(BoardPosition pos) {
        if (pos == null) {
            return false;
        }

        if (wolf.getPosition().equals(pos)) {
            return false;
        }

        if (sheep[0].getPosition().equals(pos)) {
            return false;
        }

        if (sheep[1].getPosition().equals(pos)) {
            return false;
        }

        if (sheep[2].getPosition().equals(pos)) {
            return false;
        }

        if (sheep[3].getPosition().equals(pos)) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        if (wolf.getPosition().equals(((Move) move).getOldPosition())) {
            wolf.setPosition((BoardPosition) ((Move) move).getNewPosition());

            return true;
        }

        if (sheep[0].getPosition().equals(((Move) move).getOldPosition())) {
            sheep[0].setPosition((BoardPosition) ((Move) move).getNewPosition());

            return true;
        }

        if (sheep[1].getPosition().equals(((Move) move).getOldPosition())) {
            sheep[1].setPosition((BoardPosition) ((Move) move).getNewPosition());

            return true;
        }

        if (sheep[2].getPosition().equals(((Move) move).getOldPosition())) {
            sheep[2].setPosition((BoardPosition) ((Move) move).getNewPosition());

            return true;
        }

        if (sheep[3].getPosition().equals(((Move) move).getOldPosition())) {
            sheep[3].setPosition((BoardPosition) ((Move) move).getNewPosition());

            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        Move move = (Move) getLastMove();

        if (wolf.getPosition().equals(move.getNewPosition())) {
            wolf.setPosition((BoardPosition) move.getOldPosition());

            return true;
        }

        if (sheep[0].getPosition().equals(move.getNewPosition())) {
            sheep[0].setPosition((BoardPosition) (move.getOldPosition()));

            return true;
        }

        if (sheep[1].getPosition().equals(move.getNewPosition())) {
            sheep[1].setPosition((BoardPosition) (move.getOldPosition()));

            return true;
        }

        if (sheep[2].getPosition().equals(move.getNewPosition())) {
            sheep[2].setPosition((BoardPosition) (move.getOldPosition()));

            return true;
        }

        if (sheep[3].getPosition().equals(move.getNewPosition())) {
            sheep[3].setPosition((BoardPosition) (move.getOldPosition()));

            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        if ((numberOfMoves() % 2) == 0) {
            return 0;
        } else {
            return 1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean wolfPassed() {
        int rank = 8;

        if (sheep[0].getPosition().getRank() < rank) {
            rank = sheep[0].getPosition().getRank();
        }

        if (sheep[1].getPosition().getRank() < rank) {
            rank = sheep[1].getPosition().getRank();
        }

        if (sheep[2].getPosition().getRank() < rank) {
            rank = sheep[2].getPosition().getRank();
        }

        if (sheep[3].getPosition().getRank() < rank) {
            rank = sheep[3].getPosition().getRank();
        }

        if (wolf.getPosition().getRank() <= rank) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (wolfPassed()) {
            return ((firstPlayer == WOLF) ? new int[] { 0 } : new int[] { 1 });
        }

        if (!gameOver()) {
            return null;
        }

        return ((nextPlayer() == 0) ? new int[] { 1 } : new int[] { 0 });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BoardPosition getWolfPosition() {
        return wolf.getPosition();
    }

    /**
     * accepts index 0 - 3
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public BoardPosition getSheepPosition(int index)
        throws ArrayIndexOutOfBoundsException {
        return sheep[index].getPosition();
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        WolfsheepGame o = (WolfsheepGame) obj;

        if (!wolf.equals(o.wolf)) {
            return false;
        }

        if (!sheep[0].equals(o.sheep[0])) {
            return false;
        }

        if (!sheep[1].equals(o.sheep[1])) {
            return false;
        }

        if (!sheep[2].equals(o.sheep[2])) {
            return false;
        }

        if (!sheep[3].equals(o.sheep[3])) {
            return false;
        }

        if (firstPlayer != o.firstPlayer) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "\n";

        for (int row = 8; row >= 1; row--) {
            s += (" " + row + " ");

            for (char column = 'a'; column <= 'h'; column++) {
                s += (getPieceAsStringAt(new BoardPosition(column, row)) + " ");
            }

            s += "\n";
        }

        s += "   A B C D E F G H \n";
        s += "\nnext player: ";

        switch (nextPlayer()) {
        case 0:

            if (firstPlayer == WOLF) {
                s += "Wolf";
            } else {
                s += "Sheep";
            }

            break;

        case 1:

            if (firstPlayer == SHEEP) {
                s += "Wolf";
            } else {
                s += "Sheep";
            }

            break;
        }

        return (s + "\n");
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    String getPieceAsStringAt(BoardPosition pos) {
        if (wolf.getPosition().equals(pos)) {
            return "W";
        }

        if (sheep[0].getPosition().equals(pos)) {
            return "S";
        }

        if (sheep[1].getPosition().equals(pos)) {
            return "S";
        }

        if (sheep[2].getPosition().equals(pos)) {
            return "S";
        }

        if (sheep[3].getPosition().equals(pos)) {
            return "S";
        }

        return "_";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        WolfsheepGame newgame = (WolfsheepGame) super.clone();
        newgame.wolf = new Piece(wolf.getType(),
                (BoardPosition) wolf.getPosition().clone());
        newgame.sheep = new Piece[] {
                new Piece(sheep[0].getType(),
                    (BoardPosition) sheep[0].getPosition().clone()),
                new Piece(sheep[1].getType(),
                    (BoardPosition) sheep[1].getPosition().clone()),
                new Piece(sheep[2].getType(),
                    (BoardPosition) sheep[2].getPosition().clone()),
                new Piece(sheep[3].getType(),
                    (BoardPosition) sheep[3].getPosition().clone())
            };

        return newgame;
    }

    /**
     * implements the GameMove for the WolfsheepGame
     */
    static class Move extends GameBoardMove {
        /**
         * DOCUMENT ME!
         */
        static final long serialVersionUID = 4602385911093738983L;

        /**
         * Creates a new Move object.
         *
         * @param player DOCUMENT ME!
         * @param from DOCUMENT ME!
         * @param to DOCUMENT ME!
         */
        public Move(int player, BoardPosition from, BoardPosition to) {
            super(player, from, to);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public String toString() {
            String s = "from: " + getOldPosition();
            s += (" to: " + getNewPosition());

            return s;
        }
    }

    /**
     * represents either a wolf or a sheep for WolfsheepGame
     */
    static class Piece implements Serializable {
        /**
         * DOCUMENT ME!
         */
        static final long serialVersionUID = 6487514326164888803L;

        /**
         * DOCUMENT ME!
         */
        BoardPosition pos;

        /**
         * DOCUMENT ME!
         */
        byte type;

        /**
         * Creates a new Piece object.
         *
         * @param type DOCUMENT ME!
         * @param pos DOCUMENT ME!
         */
        public Piece(byte type, BoardPosition pos) {
            this.pos = pos;
            this.type = type;

            if ((type != SHEEP) && (type != WOLF)) {
                throw new IllegalArgumentException();
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public BoardPosition getPosition() {
            return pos;
        }

        /**
         * DOCUMENT ME!
         *
         * @param pos DOCUMENT ME!
         */
        void setPosition(BoardPosition pos) {
            this.pos = pos;
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public byte getType() {
            return type;
        }

        /**
         * DOCUMENT ME!
         *
         * @param obj DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object obj) {
            Piece o = (Piece) obj;

            if (type != o.type) {
                return false;
            }

            if (!pos.equals(o.pos)) {
                return false;
            }

            return true;
        }
    }
}
