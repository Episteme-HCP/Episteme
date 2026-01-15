/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.fourwins;

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;

import java.util.Vector;


/**
 * FourWinsGame represents the game of 4-wins or 4-connects. (other names
 * for the game: four wins, connect four, vier gewinnt)
 *
 * @author Holger Antelmann
 */
public class FourWinsGame extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -7580479947357059116L;

    /**
     * DOCUMENT ME!
     */
    public static final int WINNING_LINE_LENGTH = 4;

    /**
     * DOCUMENT ME!
     */
    public final int MAX_MOVES;

    /**
     * DOCUMENT ME!
     */
    public final int boardWidth;

    /**
     * DOCUMENT ME!
     */
    public final int boardHeight;

    /**
     * DOCUMENT ME!
     */
    protected int[][] board;

/**
     * creates the standard game board with 7x6
     */
    public FourWinsGame() {
        this("FourWins standard game", 7, 6);
    }

    /**
     * Creates a new FourWinsGame object.
     *
     * @param name DOCUMENT ME!
     */
    public FourWinsGame(String name) {
        this(name, 7, 6);
    }

    /**
     * Creates a new FourWinsGame object.
     *
     * @param name DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public FourWinsGame(String name, int width, int height)
        throws IllegalArgumentException {
        super(name, 2);

        if ((width < 4) || (width > 10) || (height < 4) || (height > 10)) {
            throw (new IllegalArgumentException(
                "Error initializing GameBoard for FourWins: width/height values are only supported between 4 and 10."));
        }

        MAX_MOVES = width * height;
        boardWidth = width;
        boardHeight = height;
        board = new int[boardWidth][boardHeight];

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                board[column][row] = -1;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        if (numberOfMoves() >= MAX_MOVES) {
            return null;
        }

        if (getWinner() != null) {
            return null;
        }

        Vector<FourWinsMove> v = new Vector<FourWinsMove>();
        int nextPlayer = nextPlayer();

        for (int i = 0; i < boardWidth; i++) {
            if (board[i][boardHeight - 1] == -1) {
                v.add((new FourWinsMove(nextPlayer, i)));
            }
        }

        return v.toArray(new FourWinsMove[v.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        return ((numberOfMoves() % 2) == 0) ? 0 : 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        int lastMoveColumn = ((FourWinsMove) getLastMove()).getColumn();
        int lastMoveRow = freePositionInColumn(lastMoveColumn) - 1;
        board[lastMoveColumn][lastMoveRow] = -1;

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
        int column = ((FourWinsMove) move).getColumn();
        board[column][freePositionInColumn(column)] = nextPlayer();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        int win = -1;

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                if (board[column][row] != -1) {
                    win = checkPositionWin(column, row);

                    if (win != -1) {
                        return (new int[] { win });
                    }
                }
            }
        }

        if (numberOfMoves() == MAX_MOVES) {
            return new int[] {  };
        }

        return null;
    }

    /**
     * Returns the value for a specified position in the game board.
     * The parameters are directly used to retrieve the array elements;
     * therefore, ArrayIndexOutOfBoundsException may be thrown
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ArrayIndexOutOfBoundsException DOCUMENT ME!
     */
    public int getValueAt(int column, int row)
        throws ArrayIndexOutOfBoundsException {
        return (board[column][row]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    protected int freePositionInColumn(int column)
        throws IllegalArgumentException {
        if ((column >= boardWidth) || (column < 0)) {
            throw (new IllegalArgumentException(
                "Error - column argument is out of bounds"));
        }

        int i = 0;

        while ((i < boardHeight) && (board[column][i] > -1))
            i++;

        return (i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected int checkPositionWin(int column, int row) {
        int line;
        int delta;
        //checking right
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < boardWidth)) {
            if (board[column][row] != board[column + delta][row]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        //checking up
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) && ((row + delta) < boardHeight)) {
            if (board[column][row] != board[column][row + delta]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        //checking diagonal up
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < boardWidth) &&
                ((row + delta) < boardHeight)) {
            if (board[column][row] != board[column + delta][row + delta]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        //checking diagonal down
        line = 1;
        delta = 1;

        while ((WINNING_LINE_LENGTH > delta) &&
                ((column + delta) < boardWidth) && ((row - delta) >= 0)) {
            if (board[column][row] != board[column + delta][row - delta]) {
                break;
            }

            line++;
            delta++;
        }

        if (line == WINNING_LINE_LENGTH) {
            return (board[column][row]);
        }

        return (-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return boardWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return boardHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String print = "\n";

        for (int row = boardHeight - 1; row >= 0; row--) {
            for (int column = 0; column < boardWidth; column++) {
                print = print + " ";

                switch (board[column][row]) {
                case -1:
                    print = print + "_";

                    break;

                case 0:
                    print = print + "X";

                    break;

                case 1:
                    print = print + "O";

                    break;
                }
            }

            print = print + "\n";
        }

        print = print + " _____________\n";
        print = print + " 1 2 3 4 5 6 7\n";
        print = print + "\nNext Player: ";

        switch (nextPlayer()) {
        case 0:
            print = print + "X";

            break;

        case 1:
            print = print + "O";

            break;

        default:
            print = print + "none";
        }

        return print;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        FourWinsGame newGame = (FourWinsGame) super.clone();
        newGame.board = new int[boardWidth][boardHeight];

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                newGame.board[column][row] = board[column][row];
            }
        }

        return newGame;
    }

    /**
     * experimental, but working
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int h = 0;
        h = h + board[0][0];
        h = h + (board[1][0] * 3);
        h = h + (board[2][0] * 6);
        h = h + (board[3][0] * 9);
        h = h + (board[4][0] * 12);
        h = h + (board[5][0] * 15);
        h = h + (board[6][0] * 18);

        return h;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof FourWinsGame)) {
            return false;
        }

        FourWinsGame o = (FourWinsGame) obj;

        if ((boardWidth != o.boardWidth) || (boardHeight != o.boardHeight)) {
            return false;
        }

        for (int column = 0; column < boardWidth; column++) {
            for (int row = 0; row < boardHeight; row++) {
                if (board[column][row] != o.board[column][row]) {
                    return false;
                }
            }
        }

        return true;
    }
}
