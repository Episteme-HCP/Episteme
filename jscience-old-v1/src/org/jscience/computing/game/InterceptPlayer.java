/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;


//import org.jscience.computing.game.RandomPlayer;
//import org.jscience.computing.game.GamePlay;
//import org.jscience.computing.game.GameMove;
import org.jscience.util.Monitor;


/**
 * InterceptPlayer is an intermediate Player object that allows any
 * 'owning' object (for example a GUI interface) to intercept and react on
 * calls to selectMove() and return the value set by the object controlling
 * the monitor. One potential use for this Player object is to e.g. place it
 * into a SocketPlayerServer and then have a GUI wait for move selection
 * requests that this InterceptPlayer will pass on. into the constructor.
 * Apart from that, it acts as a RandomPlayer.
 *
 * @author Holger Antelmann
 *
 * @see SocketPlayerServer
 */
class InterceptPlayer extends RandomPlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 5926359634408162108L;

    /** DOCUMENT ME! */
    GamePlay gameclass;

    /** DOCUMENT ME! */
    public Monitor monitor;

    /** DOCUMENT ME! */
    public int[] role;

    /** DOCUMENT ME! */
    public int level;

    /** DOCUMENT ME! */
    public long milliseconds;

    /** DOCUMENT ME! */
    public GamePlay game;

/**
     * game is the type of game that can be played; sets monitor.test to true
     * until selectMove() is called
     *
     * @param game    DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     */
    public InterceptPlayer(GamePlay game, Monitor monitor) {
        gameclass = game;
        this.monitor = monitor;
        monitor.test = true;
    }

    /**
     * returns true only if the given game class matches the class of
     * the game given in the constructor
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        if (game.getClass() == gameclass.getClass()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * selectMove() will store all parameters in its public member
     * variables - making them accessible to the 'owner' of this NetPlayer
     * object; it then calls monitor.runTask(), sets monitor.test = false and
     * waits until being interrupted or monitor.test = true (practially waits
     * for the 'owner' to act). The function will then retrieve
     * monitor.getObject() and returns it as a GameMove (assuming that the
     * 'owner' will have put a GameMove object there and then set monitor.test
     * = true again or interrupted this object).
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
        this.game = game;
        this.role = role;
        this.level = level;
        this.milliseconds = milliseconds;
        monitor.test = false;
        monitor.runTask();

        while (!monitor.test) {
            try {
                synchronized (this) {
                    wait(1);
                }
            } catch (InterruptedException e) {
            }
        }

        return (GameMove) monitor.getObject();
    }
}
