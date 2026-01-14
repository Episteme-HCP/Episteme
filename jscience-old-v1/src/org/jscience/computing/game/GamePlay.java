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

import java.io.Serializable;

/**
 * GamePlay provides all methods needed to play a game.
 * A game is played by players represented by integer game roles.
 * It is  assumed that an integer should always be sufficient to
 * represent a game role. AutoPlay then introduces the concept
 * of Player objects more in detail.
 * Further assumptions for GamePlay objects are that as long as the
 * game is in progress, there is a finite set of moves that can
 * be performed in any given game status and one of the moves
 * can be picked to advance the game to another state with its
 * own set of legal moves. Another important assumption is that
 * given any game status, there is always one player determinable
 * that makes the next move (if the game is not over).
 * Instead of implementing all functionality from scratch, it is
 * recommended to take advantage of classes that already provide
 * a good portion of the functionality - such as AbstractGame; for
 * many games, this class provides a good basis.
 * There are games that have many different variants of legal moves,
 * which may be hard to return with getLegalMoves(). If that is the
 * case, getLegalMoves() could simply return templates for legal moves;
 * the method isLegalMoves() would then be responsible to determine
 * whether the move constructed by a player is actually legal. This is
 * why both functions are part of this interface.
 * GamePlay extends Serializable to ensure that a game and its
 * moves can be used over e.g. an Internet connection or can be
 * written to a file.
 *
 * @author Holger Antelmann
 * @see AbstractGame
 * @see GameMove
 * @see Player
 * @see AutoPlay
 */
public interface GamePlay extends Serializable, Cloneable {
    /**
     * returns the name of the game for display in titles
     * (as toString() may be used to display something else)
     */
    String getGameName();

    /**
     * numberOfPlayers() returns the number of game roles
     * in the GamePlay. Each role in the game is represented
     * by an integer so that (0 <= game_role < numberOfPlayers()).
     * The association to actual Player objects is then done by
     * AutoPlay objects.
     *
     * @see AutoPlay
     * @see org.jscience.computing.game.Player
     */
    int numberOfPlayers();

    /**
     * If the move is successfully carried out, the function
     * returns true, false otherwise. This function should work
     * with any legal moves as returned by getLegalMoves().
     */
    boolean makeMove(GameMove move);

    /**
     * nextPlayer()
     * returns the integer representing the game role of the
     * next player.
     * If there is no next player, it is assumed that there are
     * no more legal moves left in the game,
     * in which case the value of nextPlayer() should be irrelevant.
     */
    int nextPlayer();

    /**
     * returns an array containing all moves that can be legally
     * performed with the current game status; each move should
     * successfully be able to advance the game when called with
     * makeMove().
     * Although it is not required that getLegalMoves() returns
     * all existing legal moves, it helps for most implementation
     * if it does (as isLegalMove() can then just check for a move
     * being in the array returned here); other implementations
     * (where it is hard to create a finite list of all possible moves)
     * may return templates to create moves where isLegalMoves() will
     * then check for their consistency.
     *
     * @see #isLegalMove(GameMove)
     */
    GameMove[] getLegalMoves();

    /**
     * This function is needed for games where getLegalMoves() may
     * not return a complete list of all possible moves (so that a
     * determination on whether the given move is legal could simply
     * be made by checking whether the move is included in the array
     * returned by getLegalMoves()); then,
     * isLegalMoves() will make the determination whether the given
     * move is legal.
     *
     * @see #getLegalMoves()
     */
    boolean isLegalMove(GameMove move);

    /**
     * returns an array containing all moves that have been applied
     * to the current game status; the last move that was applied is
     * the last move in the array at index (getMoveHistory().length-1)
     */
    GameMove[] getMoveHistory();

    /**
     * returns an array containing all moves that have been taken back
     * and could be reapplied; the GameMove at the index 0 is the next
     * object that can be reapplied.
     */
    GameMove[] getRedoList();

    /**
     * If the last move is successfully taken back, this method
     * returns true, false otherwise. If there are no moves to
     * be taken back, the method also returns false.
     * Undoing moves may not be supported by a game implementation,
     * in which case this method would simply always return false.
     */
    boolean undoLastMove();

    /**
     * returns true only if last move previously taken back is successfully
     * reapplied.
     */
    boolean redoMove();

    /**
     * getWinner() returns an array of all winning game roles - as
     * there could be more than one winner. If the function returns null,
     * it doesn't necessarily mean that the game is still in progress,
     * but it could also mean that there is no winner in this game
     * even though it is over (alternatively, an empty array could
     * be returned depending on the requirements of the game).
     * Also, if winners are returned, the game could still be in
     * progress. Maybe, a game keeps playing to determine who is
     * the second winner, etc.
     */
    int[] getWinner();

    /**
     * getResult() returns the final outcome for a player, which could be
     * a dollar amount, points gained or anything else
     *
     * @throws GameRuntimeException if there is not a final result determined
     *                              for the given player in the game, yet
     */
    double getResult(int playerRole) throws GameRuntimeException;

    /**
     * spawnChild returns a deep copy of an identical game that represents
     * the status of the game after the given move is carried out.
     * This method relies on the implementing class to provide a deep copy,
     * so that any alterations to the retuned game
     * (incl. its redoList etc.) don't affect the original game.
     *
     * @throws GameRuntimeException if the given move is not legal
     */
    GamePlay spawnChild(GameMove move) throws GameRuntimeException;

    /**
     * To support several other methods, the clone() method
     * needs public access and must be implemented so that the
     * returned object is a <i>deep copy</i> of the object at hand.
     *
     * @see #spawnChild(GameMove)
     */
    Object clone() throws CloneNotSupportedException;
}
