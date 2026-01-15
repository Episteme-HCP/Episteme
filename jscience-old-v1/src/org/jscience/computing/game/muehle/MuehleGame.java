/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.muehle;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;

import java.util.Vector;


/**
 * a representation of the game of Muehle
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.computing.game.muehle.MuehlePosition
 */
public class MuehleGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -5095231269269893687L;

    /**
     * DOCUMENT ME!
     */
    public static final int NUMBER_OF_PIECES = 9;

    /**
     * DOCUMENT ME!
     */
    public static final int EMPTY = -1;

    /**
     * DOCUMENT ME!
     */
    private int[] field;

    /**
     * DOCUMENT ME!
     */
    private int[] leftOnHand;

    /**
     * Creates a new MuehleGame object.
     */
    public MuehleGame() {
        this("Nine Men's Morris default game");
    }

    /**
     * Creates a new MuehleGame object.
     *
     * @param name DOCUMENT ME!
     */
    public MuehleGame(String name) {
        super(name, 2);
        field = new int[MuehlePosition.NUMBER_OF_FIELDS];

        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            field[i] = EMPTY;
        }

        leftOnHand = new int[] { NUMBER_OF_PIECES, NUMBER_OF_PIECES };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        if ((piecesLeft(0) < 3) || (piecesLeft(1) < 3)) {
            return null;
        }

        //if ((piecesLeft(0) == 3) && (piecesLeft(1) == 3)) return null;
        int player = nextPlayer();
        Vector<MuehleMove> v = new Vector<MuehleMove>();
iteration: 
        for (int pos = 0; pos < MuehlePosition.NUMBER_OF_FIELDS; pos++) {
            if (leftOnHand[player] > 0) {
                if (field[pos] != EMPTY) {
                    continue iteration;
                }

                if (isInMuehle(pos, player)) {
                    for (int x = 0; x < MuehlePosition.NUMBER_OF_FIELDS; x++) {
                        if ((field[x] != EMPTY) && (field[x] != player)) {
                            if (!isInMuehle(x, field[x]) ||
                                    allInMuehle(field[x])) {
                                v.add(new MuehleMove(new MuehlePosition(pos),
                                        player, new MuehlePosition(x)));
                            }
                        }
                    }
                } else {
                    v.add(new MuehleMove(new MuehlePosition(pos), player));
                }
            } else {
                if (field[pos] != player) {
                    continue iteration;
                }

                int[] cons;

                if (piecesLeft(player) == 3) {
                    cons = new int[MuehlePosition.NUMBER_OF_FIELDS];

                    for (int n = 0; n < MuehlePosition.NUMBER_OF_FIELDS; n++)
                        cons[n] = n;
                } else {
                    cons = MuehlePosition.getConnections(pos);
                }

                for (int newPos = 0; newPos < cons.length; newPos++) {
                    if (field[cons[newPos]] == EMPTY) {
                        int[] pairs = MuehlePosition.getMuehlePairs(cons[newPos]);

                        if (((player == field[pairs[0]]) &&
                                (player == field[pairs[1]]) &&
                                (pos != pairs[0]) && (pos != pairs[1])) ||
                                ((player == field[pairs[2]]) &&
                                (player == field[pairs[3]]) &&
                                (pos != pairs[2]) && (pos != pairs[3]))) {
                            for (int y = 0;
                                    y < MuehlePosition.NUMBER_OF_FIELDS; y++) {
                                if ((field[y] != EMPTY) &&
                                        (field[y] != player)) {
                                    if (!isInMuehle(y, field[y]) ||
                                            allInMuehle(field[y])) {
                                        v.add(new MuehleMove(
                                                new MuehlePosition(pos),
                                                new MuehlePosition(cons[newPos]),
                                                player, new MuehlePosition(y)));
                                    }
                                }
                            }
                        } else {
                            v.add(new MuehleMove(new MuehlePosition(pos),
                                    new MuehlePosition(cons[newPos]), player));
                        }
                    }
                }
            }
        }

        return v.toArray(new MuehleMove[v.size()]);
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
    public int[] getWinner() {
        if (piecesLeft(0) < 3) {
            return new int[] { 1 };
        }

        if (piecesLeft(1) < 3) {
            return new int[] { 0 };
        }

        if (getLegalMoves().length == 0) {
            return new int[] { (nextPlayer() == 0) ? 1 : 0 };
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        MuehleMove m = null;

        try {
            m = (MuehleMove) move;
        } catch (ClassCastException e) {
            return false;
        }

        if (m.getOldPosition() != null) {
            field[m.getOldPosition().asInteger()] = EMPTY;
        }

        field[m.getNewPosition().asInteger()] = m.getPlayer();

        if (m.getOption() != null) {
            field[((MuehlePosition) m.getOption()).asInteger()] = EMPTY;
        }

        leftOnHand[m.getPlayer()]--;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        MuehleMove move = (MuehleMove) getLastMove();
        field[move.getNewPosition().asInteger()] = EMPTY;

        if (move.getOldPosition() != null) {
            field[move.getOldPosition().asInteger()] = move.getPlayer();
        }

        if (move.getOption() != null) {
            field[((MuehlePosition) move.getOption()).asInteger()] = (move.getPlayer() == 0)
                ? 1 : 0;
        }

        leftOnHand[move.getPlayer()]++;

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValueAt(MuehlePosition pos) {
        return field[pos.asInteger()];
    }

    /**
     * isInMuehle() returns true only if a pair of corresponding
     * Positions to the given MuehlePosition contain pieces of the same player
     * as given.
     *
     * @param pos DOCUMENT ME!
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInMuehle(int pos, int player) {
        int[] pairs = MuehlePosition.getMuehlePairs(pos);

        if ((player == field[pairs[0]]) && (player == field[pairs[1]])) {
            return true;
        }

        if ((player == field[pairs[2]]) && (player == field[pairs[3]])) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean allInMuehle(int player) {
        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            if ((field[i] == player) && !isInMuehle(i, player)) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int piecesLeft(int player) {
        if ((player != 0) && (player != 1)) {
            String s = "Error in org.jscience.computing.game.muehle.MuehleGame.piecesLeft();";
            s += " the player parameter given should not be anything but 0 or 1, but";
            s += (" it was: " + player);
            throw (new IllegalArgumentException(s));
        }

        int count = 0;

        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            if (player == field[i]) {
                count++;
            }
        }

        if (leftOnHand[player] > 0) {
            count = count + leftOnHand[player];
        }

        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRemainingInHand(int player) {
        return leftOnHand[player];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "\n";
        s += "  @0@--------@1@--------@2@\n";
        s += "  |        |        |\n";
        s += "  |  @8@-----@9@-----@10@  |\n";
        s += "  |  |     |     |  |\n";
        s += "  |  |  @16@--@17@--@18@  |  |\n";
        s += "  |  |  |     |  |  |\n";
        s += "  @7@--@15@--@23@     @19@--@11@--@3@\n";
        s += "  |  |  |     |  |  |\n";
        s += "  |  |  @22@--@21@--@20@  |  |\n";
        s += "  |  |     |     |  |\n";
        s += "  |  @14@-----@13@-----@12@  |\n";
        s += "  |        |        |\n";
        s += "  @6@--------@5@--------@4@\n";

        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            switch (field[i]) {
            case EMPTY:
                s = s.replaceAll("@" + i + "@", " ");

                break;

            case 0:
                s = s.replaceAll("@" + i + "@", "X");

                break;

            case 1:
                s = s.replaceAll("@" + i + "@", "O");

                break;

            default:
                s = s.replaceAll("@" + i + "@", "error" + field[i]);
            }
        }

        s += "\nnext player: ";

        switch (nextPlayer()) {
        case 0:
            s += "X\n\n";

            break;

        case 1:
            s += "O\n\n";

            break;

        default:
            s += "-\n\n";
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
        MuehleGame newGame = (MuehleGame) super.clone();
        newGame.field = new int[MuehlePosition.NUMBER_OF_FIELDS];

        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            newGame.field[i] = field[i];
        }

        newGame.leftOnHand = new int[] { leftOnHand[0], leftOnHand[1] };

        return newGame;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof MuehleGame)) {
            return false;
        }

        MuehleGame o = (MuehleGame) obj;

        if (leftOnHand[0] != o.leftOnHand[0]) {
            return false;
        }

        if (leftOnHand[1] != o.leftOnHand[1]) {
            return false;
        }

        for (int i = 0; i < MuehlePosition.NUMBER_OF_FIELDS; i++) {
            if (field[i] != o.field[i]) {
                return false;
            }
        }

        if (nextPlayer() != o.nextPlayer()) {
            return false;
        }

        return true;
    }

    /**
     * still experimental
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int h = 0;
        h = h + (field[0] * 3);
        h = h + (field[1] * 7);
        h = h + (field[2] * 11);
        h = h + (field[3] * 13);
        h = h + (field[4] * 17);
        h = h + (field[5] * 23);
        h = h + (field[6] * 29);
        h = h + (field[7] * 31);
        h = h + (field[8] * 37);
        h = h + (field[9] * 47);

        return h;
    }
}
