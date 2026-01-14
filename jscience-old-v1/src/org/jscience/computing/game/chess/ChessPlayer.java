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

package org.jscience.computing.game.chess;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.GameUtils;
import org.jscience.computing.game.TemplatePlayer;

import java.util.Random;


/**
 * provides AI for a ChessGame - unfortunately, it's still a dumb player
 *
 * @author Holger Antelmann
 */
public class ChessPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 4314048066398438032L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new ChessPlayer object.
     */
    public ChessPlayer() {
        this("unnamed Chess Player");
    }

/**
     * Creates a new ChessPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public ChessPlayer(String name) {
        this(name, SEARCH_ALPHABETA, false);
    }

/**
     * Creates a new ChessPlayer object.
     *
     * @param name           DOCUMENT ME!
     * @param searchOption   DOCUMENT ME!
     * @param enableTracking DOCUMENT ME!
     */
    public ChessPlayer(String name, int searchOption, boolean enableTracking) {
        this(name, searchOption, enableTracking, System.currentTimeMillis());
    }

/**
     * Creates a new ChessPlayer object.
     *
     * @param name           DOCUMENT ME!
     * @param searchOption   DOCUMENT ME!
     * @param enableTracking DOCUMENT ME!
     * @param randomSeed     DOCUMENT ME!
     */
    public ChessPlayer(String name, int searchOption, boolean enableTracking,
        long randomSeed) {
        super(name, searchOption, enableTracking);

        if (randomSeed != 0) {
            random = new Random(randomSeed);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return (game instanceof ChessGame);
    }

    /**
     * if seed = 0, randomization is disabled
     *
     * @param seed DOCUMENT ME!
     */
    public void setRandomSeed(long seed) {
        if (random != null) {
            random.setSeed(seed);

            if (seed == 0) {
                random = null;
            }
        } else if (seed != 0) {
            random = new Random(seed);
        }
    }

    /**
     * this heuristic still needs some major work; it only looks at
     * piece value at this point
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        game = game.spawnChild(move);

        double result = GameUtils.checkForWin(game, role);

        if (result == 0) {
            double v = 0;
            ChessBoard board = ((ChessGame) game).getBoard();
            ChessPiece[] pieces = board.getPieces();

            // determining piece count balance
            for (int i = 0; i < pieces.length; i++) {
                if ((pieces[i].getColor() == role[0])) {
                    v = v + getPieceValue(pieces[i]);
                } else {
                    v = v - getPieceValue(pieces[i]);
                }
            }

            result = (v * 100);

            if (random != null) {
                result = result + random.nextFloat();
            }
        } else {
            result = result * 1000000;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    double heuristic2(GamePlay game, GameMove move, int[] role) {
        double h = 0;
        double v = 0;
        double p = 0;
        double a = 0;
        game = game.spawnChild(move);

        ChessBoard board = ((ChessGame) game).getBoard();
        ChessPiece[] pieces = board.getPieces();

        // determining piece count balance
        for (int i = 0; i < pieces.length; i++) {
            if ((pieces[i].getColor() == role[0])) {
                v = v + getPieceValue(pieces[i]);
            } else {
                v = v - getPieceValue(pieces[i]);
            }

            // also, check Pawn position; the more advanced, the 'slightly' better
            if (pieces[i].isPawn()) {
                double r = pieces[i].getPosition().getRank();

                if (role[0] == ChessBoard.BLACK) {
                    r = 9 - r;
                }

                p = p + (r / 100); // 100 is a rather random measure here
            }
        }

        // checking number of possible position attacks
        for (int row = 1; row <= 8; row++) {
            for (char column = 'a'; column <= 'h'; column++) {
                if (board.positionAttackedBy(new BoardPosition(column, row),
                            role[0])) {
                    a = a + ((double) 1 / 64);
                }
            }
        }

        h = v + p + a;

        return h;
    }

    /**
     * DOCUMENT ME!
     *
     * @param piece DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public int getPieceValue(ChessPiece piece) {
        switch (piece.getPieceChar()) {
        case 'K':
            return 200;

        case 'Q':
            return 10;

        case 'N':
        case 'B':
            return 3;

        case 'R':
            return 5;

        case 'P':
            return 1;

        default:
            throw new Error();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString();
        s += "; ChessPlayer properties: randomization ";

        if (random == null) {
            s += "disabled";
        } else {
            s += "enabled";
        }

        return s;
    }
}
