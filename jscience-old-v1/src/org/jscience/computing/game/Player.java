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

package org.jscience.computing.game;

/**
 * The Player ojbect adds Artificial Intelligence
 * to a GamePlay. The association between the game roles of a
 * GamePlay and a Player object is done through the AutoPlay
 * interface.
 *
 * @author Holger Antelmann
 * @see org.jscience.computing.game.TemplatePlayer
 * @see org.jscience.computing.game.AutoPlay
 * @see SocketPlayer
 * @see SocketPlayerServer
 */
public interface Player {
    String getPlayerName();

    /**
     * canPlayGame() returns true only if the Player provides
     * an applicable heuristic for the type of game given.
     * This way, a player can control whether it can a game.
     * <code><pre>
     * //Example:
     * if (game instanceof MyGameClass) return true; else return false;
     * // or:
     * if (game.getClass() == myFavoriteGame.getClass()) return true; else return false;
     * </pre></code>
     */
    boolean canPlayGame(GamePlay game);

    /**
     * evaluate() asks the Player to rate a move in the context of a given game
     * stage relative to its role (if the Player has multiple roles in the game,
     * they will all be found in the role array - giving the Player maximum
     * flexibility) while considering the game level for potential game tree
     * search operations and then using heuristic() to evaluate the leaves of
     * the game tree. Helpful tools to implement this function can be found
     * in the classes TemplatePlayer and GameUtils.
     * Any double value is allowed for a return value. In general, the higher the
     * rating, the higher the return value should be. Note, though, that the
     * result is not connected to the actual result that a game
     * may return for a player, which may be completely different.
     * The parameter milliseconds is used to indicate that the function is expected
     * to return within that time frame; if milliseconds is 0, only the level limits
     * the thoroughness of the evaluation.
     *
     * @throws GameRuntimeException    if the move is not a legal move
     * @throws CannotPlayGameException if the game cannot be played by the player
     * @see org.jscience.computing.game.GameUtils
     * @see org.jscience.computing.game.TemplatePlayer
     * @see org.jscience.computing.game.GamePlay#getResult(int)
     */
    double evaluate(GamePlay game,
                    GameMove move,
                    int[] role,
                    int level,
                    long milliseconds) throws CannotPlayGameException, GameRuntimeException;

    /**
     * This function - often used as a callback function - evaluates the given move
     * in the context of the given game; it is expected to return quickly.
     * This function is really the only function that contains proprietory knowlege about
     * the game (as all other functions could be implemented generically without domain
     * knowlege; this is why the class TemplatePlayer provides already most methods
     * except this function for easy implementations of this interface);
     * heuristic() asks for a heuristic of the move given the game status - treating the
     * status as a leaf in a potential search tree (whereas evaluate() may perform a game tree
     * search before returning a value).
     *
     * @throws GameRuntimeException    if the move is not a legal move
     * @throws CannotPlayGameException if the game cannot be played by the player
     * @see org.jscience.computing.game.TemplatePlayer
     */
    double heuristic(GamePlay game, GameMove move, int[] role) throws CannotPlayGameException, GameRuntimeException;

    /**
     * selectMove() asks the Player to select a move out of GamePlay.legalMove()
     * based on the role it plays; milliseconds is an indication of how long
     * the calling function is willing to wait for an answer before continuing
     * with potentially a randomMove or something else.
     * The functionality of selectMove() can easily be implemented generically
     * by using evaluate() and/or heuristic(), but for a Player that may rely
     * on user interaction or remote connections to Player objects, this
     * function call is more natural.
     * If milliseconds is set to 0, only the level is used to limit
     * the evaluation during move selection; otherwise
     * milliseconds indicates that the function is expected to return within
     * the timeframe given through milliseconds.
     * A template implementation of this function (utilizing evaluate() and
     * heuristic()) is available in the class TemplatePlayer.
     *
     * @see org.jscience.computing.game.TemplatePlayer
     */
    GameMove selectMove(GamePlay game, int[] role, int level, long milliseconds) throws CannotPlayGameException;

    /**
     * This method allows the Player to prune a game tree branch by determining
     * that a particular move is not to be considered for further recursive tree search;
     * this method is expected to return quickly. <p>
     * By default, this method should always return false unless there is a good reason
     * found by a Player to dismiss the tree branch emerging from this move.
     *
     * @return true only if the given move is to be pruned from the game tree search;
     *         false otherwise
     */
    boolean pruneMove(GamePlay game, GameMove move, int[] role);
}
