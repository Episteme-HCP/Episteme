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
 * AutoPlay adds AI capabilites to a GamePlay by mapping Player objects
 * to the game roles of a GamePlay. This enables
 * to play against/with automated and intelligent opponents.
 * This also provides the basis for any type of automated game simulation.
 * <br>A word on the association of Player objects to GamePlay objects:
 * <br>A GamePlay object (read: a game) has a certain number of 'game roles',
 * each role can be assigned to a player. It is, however, possible that one
 * Player object plays more than just one role in a given game (to account
 * for any type of game setup). This is why - when dealing with game roles -
 * an array of game roles is passed on instead of just one game role.
 *
 * @author Holger Antelmann
 * @see org.jscience.computing.game.Player
 * @see org.jscience.computing.game.GamePlay
 */
public interface AutoPlay {
    /**
     * returns the underlying GamePlay object
     */
    GamePlay getGame();

    /**
     * The level in the game represents the 'depth' of the
     * search tree (number of subsequent moves) a Player
     * potentially searches through the game tree to evaluate
     * a move.
     */
    void setLevel(int level);

    /**
     * returns the game level
     *
     * @see org.jscience.computing.game.AutoPlay#setLevel(int)
     */
    int getLevel();

    /**
     * autoMove() carries out the next best move for the game
     * and returns it; null is returned if no move was carried
     * out.
     * The result of this method is determined by the player
     * to make the next move, the level and the response time
     * that is in place.
     */
    GameMove autoMove();

    /**
     * getPlayer(gameRole) returns the corresponding Player
     * to the given gameRole -
     * an integer between 0 and (getNumberOfPlayers()-1).
     * If that role is not associated with a player, the function
     * returns null.
     */
    Player getPlayer(int gameRole);

    /**
     * returns all Player objects that play the game; there may be duplicate
     * Player objects present
     */
    Player[] getPlayers();

    /**
     * changePlayer() enables to change the Player for a gameRole
     * while the game is in progress; the old Player object is returned
     */
    Player changePlayer(int gameRole, Player player) throws CannotPlayGameException;

    /**
     * Players could play multiple roles in a game,
     * so getRoles returns an array of Integers.
     * if a player doesn't play a role in the game, the
     * function returns null.
     */
    int[] getRoles(Player player);

    /**
     * sets the desired response time for the following functions:
     * <li>hint()</li>
     * <li>evaluateMove()</li>
     * <li>autoMove()</li>
     * A value of 0 indicates that the response time is only driven
     * by getLevel().
     */
    void setResponseTime(long milliseconds);

    /**
     * returns the response time that is used by the following
     * functions:
     * <li>hint()</li>
     * <li>evaluateMove()</li>
     * <li>autoMove()</li>
     */
    long getResponseTime();

    /**
     * hint(int) delegates the evaluation of the best move to make
     * to the Player object associated to the playerRole.
     * The result of this method is determined by the player
     * to make the next move, the level and the response time
     * that is in place.
     */
    GameMove hint(int playerRole);

    /**
     * The evaluation of the move is usually carried out
     * by the Player object who plays the given move; consequently,
     * the result should be interpreted from the perspective of
     * the game roles this particular Player plays in the game.
     */
    double evaluateMove(GameMove move);

    /**
     * returns a random legal move or null
     * if no legal moves are available
     */
    GameMove getRandomLegalMove();
}
