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

package org.jscience.computing.game.wolfsheep;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;
import org.jscience.computing.game.GameUtils;
import org.jscience.computing.game.TemplatePlayer;
import org.jscience.computing.game.chess.BoardPosition;
import org.jscience.computing.game.wolfsheep.WolfsheepGame.Move;

import java.util.Random;


/**
 * WSPlayer adds AI to the WolfsheepGame; can play both, wolf and sheep.
 * Note: It seems that for the sheeps to win, the game level needs to be set
 * to 7 or higher. If the sheeps move with sufficient intelligence, they
 * cannot loose; the wolf can only win based on mistakes of the sheeps. Also,
 * if the level is set high, the wolf tends to play too defensively (as it
 * anticipates the <i>right</i> move from the sheep that won't let it get
 * through, anyway), so if you want to play against the wolf, set the level
 * lower to make it play more dangerous.
 *
 * @author Holger Antelmann
 *
 * @see WolfsheepGame
 */
public class WSPlayer extends TemplatePlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2664224340132676620L;

    /** DOCUMENT ME! */
    Random random;

/**
     * Creates a new WSPlayer object.
     */
    public WSPlayer() {
        this("unnamed Wolfsheep player");
    }

/**
     * Creates a new WSPlayer object.
     *
     * @param name DOCUMENT ME!
     */
    public WSPlayer(String name) {
        super(name, SEARCH_ALPHABETA, false);
        random = new Random();
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return (game instanceof WolfsheepGame);
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
     * overwritten to provide a sensible opening that doesn't suffer
     * from the horizon effect
     *
     * @param game DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param milliseconds DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GameMove selectMove(GamePlay game, int[] role, int level,
        long milliseconds) {
        int n = game.getMoveHistory().length;

        if (((WolfsheepGame) game).firstPlayer == WolfsheepGame.SHEEP) {
            n++;
        }

        switch (n) {
        // book moves
        // (even: wolf moves, odd: sheep moves; first move is 0 or 1 if sheep plays first)
        case 1:
            return new Move(game.nextPlayer(), new BoardPosition('a', 1),
                new BoardPosition('b', 2));

        case 3:
            return new Move(game.nextPlayer(), new BoardPosition('c', 1),
                new BoardPosition('d', 2));

        case 5:
            return new Move(game.nextPlayer(), new BoardPosition('e', 1),
                new BoardPosition('f', 2));

        case 7:
            return new Move(game.nextPlayer(), new BoardPosition('g', 1),
                new BoardPosition('h', 2));

        default:
            return super.selectMove(game, role, level, milliseconds);
        }
    }

    /**
     * only cares for the first given role in the role array
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        // finding out whether the role refers to a sheep or a wolf
        // (while only considering the first configured role; others are ignored
        WolfsheepGame g = (WolfsheepGame) game.spawnChild(move);
        double result;

        switch (role[0]) {
        case 0:

            if (g.firstPlayer == WolfsheepGame.WOLF) {
                result = wolfHeuristic(g, role);

                // add (1/counterHeuristic) to the mix
                int other = (role[0] == 0) ? 1 : 0;
                result = result +
                    (1d / sheepHeuristic(g, new int[] { other }));

                break;
            } else {
                result = sheepHeuristic(g, role);

                // add (1/counterHeuristic) to the mix
                int other = (role[0] == 0) ? 1 : 0;
                result = result + (1d / wolfHeuristic(g, new int[] { other }));

                break;
            }

        case 1:

            if (g.firstPlayer == WolfsheepGame.SHEEP) {
                result = wolfHeuristic(g, role);

                // add (1/counterHeuristic) to the mix
                int other = (role[0] == 0) ? 1 : 0;
                result = result +
                    (1d / sheepHeuristic(g, new int[] { other }));

                break;
            } else {
                result = sheepHeuristic(g, role);

                // add (1/counterHeuristic) to the mix
                int other = (role[0] == 0) ? 1 : 0;
                result = result + (1d / wolfHeuristic(g, new int[] { other }));

                break;
            }

        default:
            throw new Error();
        }

        result = result * 100;

        if (random != null) {
            result = result + random.nextDouble();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double wolfHeuristic(WolfsheepGame game, int[] role) {
        if (game.getWinner() == null) {
            double result = 8 - game.wolf.getPosition().getRank();

            return result;
        }

        int result = GameUtils.checkForWin(game, role);
        result = result * 1000;

        // ensuring that a victory in the next move is considered
        // better than a victory in a couple of moves or an immediate
        // loss is worse than a later loss
        result = result +
            ((result < 0) ? game.getMoveHistory().length
                          : (-game.getMoveHistory().length));

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected double sheepHeuristic(WolfsheepGame game, int[] role) {
        if (game.getWinner() == null) {
            // ideally, all sheeps are in the same rank;
            // so, the heuristic calculates the distance
            // of each sheep from the average rank of all
            // sheeps
            double r1 = game.sheep[0].getPosition().getRank();
            double r2 = game.sheep[1].getPosition().getRank();
            double r3 = game.sheep[2].getPosition().getRank();
            double r4 = game.sheep[3].getPosition().getRank();
            double av = ((r1 + r2 + r3 + r4) / 4d);
            double r = ((double) 0) - Math.abs(av - r1);
            r = r - Math.abs(av - r2);
            r = r - Math.abs(av - r3);
            r = r - Math.abs(av - r4);

            return r;
        }

        int result = GameUtils.checkForWin(game, role);
        result = result * 1000;

        // ensuring that a victory in the next move is considered
        // better than a victory in a couple of moves or an immediate
        // loss is worse than a later loss
        result = result +
            ((result < 0) ? game.getMoveHistory().length
                          : (-game.getMoveHistory().length));

        return result;
    }
}
