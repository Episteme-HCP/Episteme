/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import org.jscience.util.Stopwatch;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * The GameDriver provides an implementation for an AutoPlay.
 * In addition, it handles Player objects
 * that represent game roles in the GamePlay object, to provide
 * for automated moves.
 *
 * @author Holger Antelmann
 */
public class GameDriver implements AutoPlay, Serializable {
    static final long serialVersionUID = -7185051701630336298L;

    GamePlay game;
    Player[] players;
    Stopwatch[] timer;
    int level;
    long responseTime;
    Random random;

    /**
     * initializes with RandomPlayer objects and level 0
     */
    public GameDriver(GamePlay game) {
        this.game = game;
        players = new Player[game.numberOfPlayers()];
        timer = new Stopwatch[players.length];
        for (int i = 0; i < players.length; i++) {
            players[i] = new RandomPlayer();
            timer[i] = new Stopwatch(false);
        }
        level = 0;
        random = new Random();
    }

    public GameDriver(GamePlay game, Player[] players, int level) {
        if (players.length != game.numberOfPlayers()) {
            throw (new IllegalArgumentException("number of player roles doesn't match the number of Players"));
        }
        timer = new Stopwatch[players.length];
        for (int i = 0; i < players.length; i++) {
            if (!players[i].canPlayGame(game)) {
                throw (new CannotPlayGameException(players[i], game, "player passed into constructor cannot play the given game"));
            }
            timer[i] = new Stopwatch(false);
        }
        this.game = game;
        this.players = players;
        this.level = level;
        random = new Random();
    }

    /**
     * sets the seed for the Random object used for generating
     * a random legal move
     *
     * @see org.jscience.computing.game.GameDriver#getRandomLegalMove()
     */
    public void setRandomSeed(long seed) {
        random.setSeed(seed);
    }

    public Player changePlayer(int gameRole, Player player) throws CannotPlayGameException {
        if ((gameRole < 0) || (gameRole >= game.numberOfPlayers())) {
            throw (new IllegalArgumentException("changing the player unsuccessful: the gameRole is not existing in this game"));
        }
        if (!player.canPlayGame(game)) {
            throw (new CannotPlayGameException(player, game, "changing the player unsuccessful: player cannot play this game"));
        }
        Player tmp = players[gameRole];
        players[gameRole] = player;
        return tmp;
    }

    public long getElapsedTime(int playerRole) {
        if ((playerRole < 0) || (playerRole >= players.length)) {
            throw (new IllegalArgumentException("playerRole doesn't exist in this game"));
        }
        return timer[playerRole].elapsed();
    }

    /*
    public GamePlay getEmbeddedGame () {
        return game;
    }
    */

    public final void setLevel(int level) {
        this.level = level;
    }

    public final int getLevel() {
        return level;
    }

    /**
     * autoMove() makes the move based on heuristics given by the Player;
     * the time taken by the Player to evaluate the move will effect the
     * time kept for that Player.
     * This means that the derived class needs to first determine nextPlayer(),
     * so that hint(nextPlayer()) can be executed.
     * The returned move will be carried out.
     */
    public GameMove autoMove() {
        synchronized (game) {
            if (game.getLegalMoves().length < 1) {
                return null;
            }
            int next = game.nextPlayer();
            timer[next].resume();
            GameMove bestMove = hint(next);
            if (game.makeMove(bestMove)) {
                timer[next].pause();
                return bestMove;
            }
            timer[next].pause();
            return null;
        }
    }

    public GameMove makeRandomMove() {
        synchronized (game) {
            GameMove myMove = getRandomLegalMove();
            if (myMove == null) {
                return null;
            }
            if (game.makeMove(myMove)) {
                return myMove;
            }
            return null;
        }
    }

    public Player getPlayer(int gameRole) throws IndexOutOfBoundsException {
        return players[gameRole];
    }

    public int[] getRoles(Player player) {
        int[] intList = null;
        List<Integer> myRoles = new Vector<Integer>();
        for (int i = 0; i < game.numberOfPlayers(); i++) {
            if (player.equals(players[i])) {
                myRoles.add(new Integer(i));
            }
        }
        if (myRoles.size() > 0) {
            intList = new int[myRoles.size()];
            for (int i = 0; i < myRoles.size(); i++) {
                intList[i] = ((Integer) myRoles.get(i)).intValue();
            }
        }
        return intList;
    }

    /**
     * sets the response time limit for the following functions:
     * <li>hint()</li>
     * <li>autoMove()</li>
     * <li>evaluateMove()</li>
     */
    public void setResponseTime(long milliseconds) {
        responseTime = milliseconds;
    }

    /**
     * returns the response time limit used by the following functions:
     * <li>hint()</li>
     * <li>autoMove()</li>
     * <li>evaluateMove()</li>
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * hint() interfaces with the Player object that corresponds to the
     * game role of this game. The function will select a move
     * based on selectMove() by the Player object and passes on the desired
     * response time set for this AutoPlay object.
     * Note that this implementation doesn't necessarily delegate the evaluation
     * to the player who plays the move, but to GamePlay.nextPlayer(), which may
     * be different at times depending on the game at hand; also, direct calls to
     * this function do not effect the time kept for the Player.
     *
     * @return the best legal move based on the preferences of the associated Player
     *         object
     * @throws IndexOutOfBoundsException if the given playerRole doesn't exist
     * @see org.jscience.computing.game.Player#selectMove(GamePlay,int[],int,long)
     */
    public GameMove hint(int playerRole) throws IndexOutOfBoundsException {
        synchronized (game) {
            if (game.getLegalMoves().length == 0) return null;
            GameMove bestMove = getPlayer(playerRole).selectMove(game, getRoles(getPlayer(playerRole)), level, responseTime);
            return bestMove;
        }
    }

    public Player[] getPlayers() {
        return players;
    }

    public GameMove getRandomLegalMove() {
        synchronized (game) {
            if (game.getLegalMoves().length == 0) {
                return null;
            }
            int randomStart = (int) (random.nextDouble() * game.getLegalMoves().length);
            return (game.getLegalMoves()[randomStart]);
        }
    }

    /**
     * This function interfaces with the Player and returns
     * the value of evaluate() while passing on the response time;
     * note that a call to this function does not effect the time
     * kept for the Player in the game.
     * set for this AutoPlay object
     *
     * @see org.jscience.computing.game.Player#evaluate(GamePlay,GameMove,int[],int,long)
     */
    public double evaluateMove(GameMove move) {
        synchronized (game) {
            return getPlayer(move.getPlayer()).evaluate(game, move, getRoles(getPlayer(move.getPlayer())), level, responseTime);
        }
    }

    /**
     * just a convenience function
     */
    public void saveToFile(String fileLocation) {
        try {
            FileOutputStream saveFile = new FileOutputStream(fileLocation);
            ObjectOutputStream s = new ObjectOutputStream(saveFile);
            s.writeObject(this);
            s.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new GameRuntimeException(game, "couldn't save game");
        }
    }

    /**
     * just a convenience function
     */
    public static GameDriver loadFromFile(String fileLocation) {
        GameDriver play;
        try {
            FileInputStream saveFile = new FileInputStream(fileLocation);
            ObjectInputStream s = new ObjectInputStream(saveFile);
            play = (GameDriver) s.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new GameRuntimeException(null, "a ClassNotFoundException was thrown");
        } catch (IOException e) {
            e.printStackTrace();
            throw new GameRuntimeException(null, "couldn't load game");
        }
        return play;
    }

    public String toString() {
        String s = "GameDriver embedded game:\n";
        s += game.toString() + "\n";
        s += players.length + " Players in the game:\n";
        if (game.getLegalMoves().length != 0) {
            s += "next player: ";
            s += players[game.nextPlayer()].getPlayerName() + "\n";
        }
        s += "game level: " + level + "; response time: " + responseTime + "\n";
        for (int i = 0; i < players.length; i++) {
            s += players[i].getPlayerName() + "'s time: ";
            s += timer[i];
            s += "\n";
        }
        return s;
    }

    /**
     * Player objects are not cloned, but their references are maintained
     * in the cloned object.
     */
    public Object clone() throws CloneNotSupportedException {
        GameDriver newObj = (GameDriver) super.clone();
        newObj.game = (GamePlay) game.clone();
        return newObj;
    }

    public GamePlay getGame() {
        return game;
    }

    /*
    // GamePlay functions passed from to the embedded game
    public String   getGameName ()             { return game.getGameName(); }
    public int numberOfPlayers ()              { return game.numberOfPlayers(); }
    public boolean makeMove (GameMove move)    { return game.makeMove(move); }
    public int nextPlayer ()                   { return game.nextPlayer(); }
    public GameMove[] getLegalMoves ()         { return game.getLegalMoves(); }
    public boolean isLegalMove (GameMove move) { return game.isLegalMove(move); }
    public GameMove[] getMoveHistory ()        { return game.getMoveHistory(); }
    public GameMove[] getRedoList ()           { return game.getRedoList(); }
    public boolean undoLastMove ()             { return game.undoLastMove(); }
    public boolean redoMove ()                 { return game.redoMove(); }
    public int [] getWinner ()                 { return game.getWinner(); }
    public double getResult (int playerRole)   { return game.getResult(playerRole); }
    public GamePlay spawnChild (GameMove move) { return game.spawnChild(move); }
    */
}
