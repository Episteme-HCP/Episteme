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

import java.io.*;

import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.Vector;


/**
 * This class implements the generic behaviour of a game to ease GamePlay
 * implementations. The following functions are abstract and need to be
 * implemented to represent the domain knowlege: <br><pre>
 * protected GameMove[] listLegalMoves ();
 * protected boolean    pushMove       (GameMove);
 * protected boolean    popMove        ();</pre>If the game does not
 * support undoing of moves, the method <code>popMove()</code> may always
 * simply return false.<br>
 * Other than the given abstract functions, the following methods need to be
 * declared by an inheriting class to account for the fact that AbstractGame
 * implements GamePlay: <br><pre>public int    nextPlayer ()
 * public int[]  getWinner  ()</pre>The function <code>getResult(int
 * playerRole)</code> is implemented in a way that it returns 1 if game is
 * won, -1 if game is lost and 0 if no winners are present, which is
 * appropriate for many games, but may have to be overwritten. Finally, the
 * method <code>clone()</code> must be overwritten in case the extending class
 * has non-primitive members to provide a deep copy. It is probably a good
 * idea to also implement a useful <code>toString()</code> method, so that the
 * game can be visualized properly.<br>
 * In addition to the above, you will also have to provide a
 * <code>GameMove</code> object that represents a move that can be applied to
 * a game to alter its status. <br>
 * Note that this implementation of <code>GamePlay</code> is designed for game
 * that  have a finite rather easily determinable list of legal moves. Also,
 * this implementation assumes that the next player in a game is well defined
 * at any given state.
 *
 * @author Holger Antelmann
 * @version 0.8
 *
 * @see org.jscience.computing.game.GamePlay
 * @see GameMove
 */
public abstract class AbstractGame implements GamePlay {
    //member variables
    /**
     * DOCUMENT ME!
     */
    private String name;

    /**
     * DOCUMENT ME!
     */
    private int numberOfPlayers;

    /**
     * DOCUMENT ME!
     */
    private Stack<GameMove> moveList;

    /**
     * DOCUMENT ME!
     */
    private Stack<GameMove> redoList;

    /**
     * DOCUMENT ME!
     */
    private GameMove[] legalMoves;

    //static final long serialVersionUID = -4609743189826102568L;
    /**
     * Creates a new AbstractGame object.
     *
     * @param name DOCUMENT ME!
     * @param numberOfPlayers DOCUMENT ME!
     */
    public AbstractGame(String name, int numberOfPlayers) {
        moveList = new Stack<GameMove>();
        redoList = new Stack<GameMove>();
        this.name = name;
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * listLegalMoves() returns the legal moves for this game. This
     * function is used by the final method getLegalMoves(), which is a
     * wrapper around this method to avoid the overhead of generating the
     * legal moves over and over again when called multiple times (as
     * calculating legal moves is commonly expensive). Note that
     * implementations of listLegalMoves() must not check for gameOver(),
     * unless gameOver() is overridden not to check for listLegalMoves(). The
     * same applies to isLegalMove().
     *
     * @return DOCUMENT ME!
     */
    abstract protected GameMove[] listLegalMoves();

    /**
     * pushMove takes a GameMove and alters the game according to to
     * the move.
     *
     * @param move DOCUMENT ME!
     *
     * @return true only if the move was put on the board successfully.
     */
    abstract protected boolean pushMove(GameMove move);

    /**
     * popMove undoes the last move by altering the game board to the
     * stage before the last move happened.
     *
     * @return true only if the move was taken back successfully.
     */
    abstract protected boolean popMove();

    /**
     * 
    DOCUMENT ME!
     *
     * @return the name of this game which is supposed to be a qualified
     *         identifier
     */
    public String getGameName() {
        return (name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newName DOCUMENT ME!
     */
    void setGameName(String newName) {
        name = newName;
    }

    /**
     * isWinner() checks whether the given gameRole is in the array
     * retruned by getWinner(). If getWinner() returns null, a
     * GameRuntimeException is thrown.
     *
     * @see GamePlay#getWinner()
     */
    public boolean isWinner(int gameRole) throws GameRuntimeException {
        int[] win = getWinner();

        if (win == null) {
            throw new GameRuntimeException(this, "no winner info avaliable yet");
        }

        if (GameUtils.isInArray(win, gameRole)) {
            return true;
        }

        return false;
    }

    /**
     * getLastPlayer() is a convenience function which simply looks up
     * the last move and then returns the playerRole that played it
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException if no move has been made, yet.
     */
    public int getLastPlayer() throws GameRuntimeException {
        try {
            return getLastMove().getPlayer();
        } catch (NullPointerException e) {
            String s = "Cannot perform getLastPlayer() - no move history available";
            throw (new GameRuntimeException(this, s));
        }
    }

    /**
     * This convenience function gameOver() simply checks whether there
     * are any legal moves left; consequently, unless this function is
     * overridden, listLegalMoves() must not check for gameOver().
     *
     * @return DOCUMENT ME!
     */
    public boolean gameOver() {
        if (getLegalMoves().length == 0) {
            return true;
        }

        return false;
    }

    /**
     * getLastMove() is a convenience function which returns the last
     * element of the GameMove[] from getMoveHistory() in case the array has
     * any elements in it; null is returned otherwise.
     *
     * @return DOCUMENT ME!
     */
    public GameMove getLastMove() {
        try {
            return ((GameMove) moveList.peek());
        } catch (EmptyStackException e) {
            return null;
        }
    }

    /**
     * Instead of overwriting this (final) function, implement
     * listLegalMoves() instead. This function serves as an optimization to
     * ensure that if called more than once on the same game, this function
     * will only have to compile the list of legal moves once. This function
     * never returns null; if no legal moves are avaliable, it returns an
     * array with 0 elements. If the method needs to recalculate the legal
     * moves by calling listLegalMoves(), it is done so in a synchronized
     * fashion.
     *
     * @return DOCUMENT ME!
     */
    public final GameMove[] getLegalMoves() {
        if (legalMoves == null) {
            synchronized (this) {
                legalMoves = listLegalMoves();
            }

            if (legalMoves == null) {
                legalMoves = new GameMove[0];
            }
        }

        return legalMoves;
    }

    /**
     * forces a recalculation of getLegalMoves() with listLegalMoves();
     * needed if the extending class alters the game status without calling
     * makeMove(), redoMove() or undoLastMove() and the move history and redo
     * list is to be maintained
     */
    protected final void resetLegalMoves() {
        legalMoves = null;
    }

    /**
     * may be called by the subclass if certain events alter the game
     * status so that the given redo moves cannot be applied anymore
     */
    protected final void clearRedoList() {
        redoList.removeAllElements();
    }

    /**
     * resetList() forces a recalculation of getLegalMoves() and also
     * resets the move list and the redo list. This is provided to allow an
     * extending class to account for a situation where the game is altered to
     * an extend where neither the move listory, the redo list, nor the the
     * legal moves apply anymore.
     */
    protected void resetLists() {
        resetLegalMoves();
        clearRedoList();
        moveList.removeAllElements();
    }

    /**
     * this implementation checks whether the move is contained in the
     * array returned by getLegalMoves(); thus this method relies on
     * GameMove.equals() being implemented properly for the move in question
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLegalMove(GameMove move) {
        if (Arrays.asList(getLegalMoves()).contains(move)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * getLegalMoves(playerRole) returns the subset of getLegalMoves()
     * where player == move.getPlayer().
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final GameMove[] getLegalMoves(int playerRole) {
        Vector<GameMove> myMoves = new Vector<GameMove>();
        GameMove[] myLegalMoves = getLegalMoves();

        for (int i = 0; i < myLegalMoves.length; i++) {
            if (playerRole == myLegalMoves[i].getPlayer()) {
                myMoves.add(myLegalMoves[i]);
            }
        }

        return myMoves.toArray(new GameMove[myMoves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean makeMove(GameMove move) {
        if (move == null) {
            return false;
        }

        if (isLegalMove(move)) {
            if (pushMove(move)) {
                moveList.push(move);
                clearRedoList();
                legalMoves = null;

                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean redoMove() {
        if (!redoList.empty()) {
            if (pushMove((GameMove) redoList.peek())) {
                moveList.push((GameMove) redoList.peek());
                redoList.pop();
                legalMoves = null;

                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int getNumberOfRedoMoves() {
        return redoList.size();
    }

    /**
     * calls undoLastMove() either n times or as many times as there
     * are numberOfMoves(), whichever is smaller
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean undoMoves(int n) {
        int count = n;

        while ((count > 0) && (numberOfMoves() > 0)) {
            if (!undoLastMove()) {
                break;
            }

            count--;
        }

        if (count == 0) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean undoLastMove() {
        if (numberOfMoves() == 0) {
            return false;
        }

        if (popMove()) {
            redoList.push(getLastMove());
            moveList.pop();
            legalMoves = null;

            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int numberOfMoves() {
        return moveList.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int numberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * a default implementation for convenience which may suit most
     * games that do not involve betting or any form of measuring how 'big'
     * the win was.
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return 1 if game is won, -1 if game is lost and 0 if no winners are
     *         present
     *
     * @throws GameRuntimeException if the game is still in progress
     */
    public double getResult(int playerRole) throws GameRuntimeException {
        if (!gameOver()) {
            throw (new GameRuntimeException(this, "game is still in progress"));
        }

        return GameUtils.checkForWin(this, new int[] { playerRole });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final GameMove[] getMoveHistory() {
        return (GameMove[]) moveList.toArray(new GameMove[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final GameMove[] getRedoList() {
        return (GameMove[]) redoList.toArray(new GameMove[0]);
    }

    /**
     * spawnChild() creates a clone of the current game and advances
     * the game by the given GameMove. The purpose of this function is to have
     * a separate game instance that can be used by a Player to do whatever
     * with it while determining heuristics. Note that the proper
     * functionality of this method relies on the inheriting class to properly
     * implement the <code>clone()</code> method to ensure a deep copy if the
     * subclass has non-primitive members.
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException if the given move is not legal or cannot be
     *         performed because the game is not cloneable
     *
     * @see org.jscience.computing.game.GameMove
     * @see #clone()
     */
    public GamePlay spawnChild(GameMove move) throws GameRuntimeException {
        try {
            AbstractGame childGame = (AbstractGame) this.clone();

            if (childGame.makeMove(move)) {
                return (childGame);
            } else {
                String s = "The GameMove for spawnChild is not legal. Details:\n";
                s += ("Printing Game: " + this + "\n");
                s += ("Move that is invalid: " + move);
                throw (new GameRuntimeException(s));
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            throw new GameRuntimeException(this, "game not cloneable");
        }
    }

    /**
     * Any inheriting class with non-primitive members <b>must</b>
     * overwrite this clone() method to provide a full deep copy of the
     * object, which is essential for <code>spawnChild()</code> to work
     * correctly. Ay overwriting of this method should still begin with a call
     * to <code>super.clone()</code>, though. Also, note that this clone()
     * method does not clone the embedded GameMove objects, i.e. those are
     * expected not to change (except for their heuristics, which shouldn't
     * affect game functionality) - or the subclass will have to take care of
     * this.
     *
     * @see #spawnChild(GameMove)
     */
    public Object clone() throws CloneNotSupportedException {
        AbstractGame newGame = (AbstractGame) super.clone();
        newGame.name = getGameName();
        newGame.moveList = new Stack<GameMove>();

        for (int i = 0; i < moveList.size(); i++) {
            newGame.moveList.add(i, moveList.get(i));
        }

        newGame.redoList = new Stack<GameMove>();

        for (int i = 0; i < redoList.size(); i++) {
            newGame.redoList.add(i, redoList.get(i));
        }

        if (legalMoves != null) {
            newGame.legalMoves = new GameMove[legalMoves.length];

            for (int i = 0; i < legalMoves.length; i++) {
                newGame.legalMoves[i] = legalMoves[i];
            }
        }

        return (newGame);
    }

    /**
     * just a convenience function
     *
     * @param fileLocation DOCUMENT ME!
     *
     * @throws GameRuntimeException DOCUMENT ME!
     */
    public void saveToFile(String fileLocation) {
        try {
            FileOutputStream saveFile = new FileOutputStream(fileLocation);
            ObjectOutputStream s = new ObjectOutputStream(saveFile);
            s.writeObject(this);
            s.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GameRuntimeException(this, "couldn't save game");
        }
    }

    /**
     * just a convenience function
     *
     * @param fileLocation DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws GameRuntimeException DOCUMENT ME!
     */
    public synchronized static AbstractGame loadFromFile(String fileLocation) {
        AbstractGame game;

        try {
            FileInputStream saveFile = new FileInputStream(fileLocation);
            ObjectInputStream s = new ObjectInputStream(saveFile);
            game = (AbstractGame) s.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new GameRuntimeException(null,
                "a ClassNotFoundException was thrown");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GameRuntimeException(null, "couldn't load game");
        }

        return game;
    }

    /**
     * overwritten to provide useful information about the game
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = super.toString() +
            ", AbstractGame info follows; game name: " + name;
        s += (", number of players: " + numberOfPlayers);
        s += (", moves performed: " + moveList.size());
        s += (", available legal moves: " + getLegalMoves().length);
        s += (", available redo moves: " + redoList.size());

        int[] w = getWinner();

        if (w != null) {
            s += ", winners: [";

            if (w.length == 0) {
                s += "none";
            }

            for (int i = 0; i < w.length; i++) {
                s += w[i];

                if (i < (w.length - 1)) {
                    s += ", ";
                }
            }

            s += "]";
        }

        return s;
    }
}
