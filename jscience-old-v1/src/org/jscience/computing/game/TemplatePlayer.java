/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import org.jscience.util.Monitor;
import org.jscience.util.Stopwatch;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Vector;

/**
 * The TemplatePlayer provides a useful skeleton implementation
 * for the Player interface. Inheriting Player classes only need
 * to implement the following methods (at a minimum) to provide
 * the necessary game domain specific knowlege:
 * <ul>
 * <code><li>canPlayGame(GamePlay game)</li>
 * <li>heuristic(GamePlay game, GameMove move, int[] role)</li></code>
 * </ul>
 * The TemplatePlayer provides game tree search capabilities by utilizing
 * methods from the class GameUtilites and it can track its requests.
 * Note that if the Player is serialized, it looses its tracking information.
 * For a sample (non-game domain specific) implementation of the TemplatePlayer,
 * see the RandomPlayer.<br>
 * <b>Known limitation:</b><br>
 * The algorithm used for time constrained search is not quite satisfactory, yet.
 * If a move evaluation is performed with a time constraint, the result may not
 * be very informed as the tree may be searched 'uneven', as the algorithms
 * used here from the class GameUtils don't currently use iterative
 * deepening; see the description for the search algorithms in the
 * GameUtils class for more details.
 *
 * @author Holger Antelmann
 * @see Player
 * @see GameUtils
 * @see RandomPlayer
 */
public abstract class TemplatePlayer implements Player, Serializable {
    /**
     * when used as searchOption, MinMax Search algorithm is used
     */
    public static final int SEARCH_MINMAX = 1;
    /**
     * when used as searchOption, Alpha-Beta Search algorithm is used
     */
    public static final int SEARCH_ALPHABETA = 2;

    protected String playerName;
    protected int searchOption;
    protected int levelOverwrite = 0;
    protected boolean orderMoves;
    protected transient Vector<Monitor> monitors;

    /**
     * This constructor instanciates a Player with
     * the name "unnamed DefaultPlayer", no game tree search option,
     * tracking disabled and no game level overwrite.
     */
    public TemplatePlayer() {
        this("unnamed DefaultPlayer");
    }

    /**
     * instanciates a Player with no game tree search option,
     * disabled tracking and no game level overwrite
     */
    public TemplatePlayer(String playerName) {
        this(playerName, 0, false);
    }

    /**
     * instanciates a player with the given playerName, searchOption and
     * tracking; game level overwrite is set to 0; for searchOption values
     * see #setSearchOption().
     */
    public TemplatePlayer(String playerName, int searchOption, boolean trackingEnabled) {
        this.playerName = playerName;
        setSearchOption(searchOption);
        if (trackingEnabled) {
            monitors = new Vector<Monitor>();
        }
    }

    /**
     * enables tracking of requests by instanciating a List of
     * monitors that will be maintained when requests are made
     * to this player; if tracking is alreay enabled, nothing happens.
     * Tracked requests include calls to evaluate() and selectMove().
     * Calls to heuristic() are counted as it is called by this class within
     * the Monitors maintained for calls to evaluate() and selectMove().
     * Further direct tracking of calls to heuristic() is left to extending
     * classes, as heuristic() is abstract in this class TemplatePlayer.
     * To reset tracking, first call disableTracking() before you call
     * this method.
     * Note that this type of tracking does not maintain the game information
     * of the requests processed; if you wish to do so, overwrite the tracking
     * mechanism accordingly.
     *
     * @deprecated use setTracking(boolean enable) instead
     */
    @Deprecated
    public void enableTracking() {
        setTracking(true);
    }

    /**
     * disables tracking of requests; all previous tracked
     * information is lost
     *
     * @deprecated use setTracking(boolean enable) instead
     */
    @Deprecated
    public void disableTracking() {
        setTracking(false);
    }

    public synchronized void setTracking(boolean enable) {
        if (enable) {
            if (monitors == null) {
                monitors = new Vector<Monitor>();
            }
        } else {
            monitors = null;
        }
    }

    public boolean trackingEnabled() {
        return (monitors != null);
    }

    /**
     * returns the number of calls to evaluate() or selectMove()
     * since tracking has been enabled
     *
     * @throws IllegalStateException if tracking is disabled
     */
    public int numberOfRequests() throws IllegalStateException {
        if (trackingEnabled()) {
            return (monitors.size());
        }
        throw new IllegalStateException("tracking is disabled on this Player object");
    }

    /**
     * goes through all monitored requests and returns the sum of
     * the elapsed time
     *
     * @throws IllegalStateException if tracking is disabled
     */
    public long totalTimeTaken() throws IllegalStateException {
        if (!trackingEnabled()) {
            throw new IllegalStateException("tracking is disabled on this Player object");
        }
        long time = 0;
        Iterator i = monitors.iterator();
        while (i.hasNext()) {
            time = time + ((Monitor) i.next()).timer.elapsed();
        }
        return time;
    }

    /**
     * returns the sum of Monitor.getNumber() from all monitors
     * tracked in this player during calls to selectMove() and
     * evaluate() if tracking has been enabled
     *
     * @throws IllegalStateException if tracking is disabled
     */
    public int numberOfPositionsSearched() throws IllegalStateException {
        if (!trackingEnabled()) {
            throw new IllegalStateException("tracking is disabled on this Player object");
        }
        int count = 0;
        Iterator i = monitors.iterator();
        while (i.hasNext()) {
            count = count + ((Monitor) i.next()).getNumber();
        }
        return count;
    }

    /**
     * returns the quotient between numberOfPositionsSearched() and totalTimeTaken();
     * the result will be scaled to reflect how many positions have been searched
     * per second.
     *
     * @throws IllegalStateException if tracking is disabled
     */
    public float performanceRatio() throws IllegalStateException {
        if (!trackingEnabled()) {
            throw new IllegalStateException("tracking is disabled on this Player object");
        }
        return (float) numberOfPositionsSearched() / totalTimeTaken() * 1000;
    }

    /**
     * returns an array of all Monitors if tracking is enabled
     *
     * @throws IllegalStateException if tracking is disabled
     * @see org.jscience.util.Monitor
     */
    protected Monitor[] getMonitors() throws IllegalStateException {
        if (!trackingEnabled()) {
            throw new IllegalStateException("tracking is disabled on this Player object");
        }
        return (Monitor[]) monitors.toArray(new Monitor[monitors.size()]);
    }

    public int getSearchOption() {
        return searchOption;
    }

    /**
     * This function sets the search algorithm used in selectMove() and evaluate().
     * Valid values for searchOption are:
     * <li>0 for no tree search</li>
     * <li>SEARCH_MINMAX for MinMax tree search algorithm</li>
     * <li>SEARCH_ALPHABETA for AlphaBeta pruning during tree search</li>
     *
     * @throws IllegalArgumentException if searchOption is unknown
     */
    public void setSearchOption(int searchOption) throws IllegalArgumentException {
        switch (searchOption) {
            case SEARCH_MINMAX:
            case SEARCH_ALPHABETA:
            case 0:
                this.searchOption = searchOption;
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * This method returns 0 by default, meaning there is no level
     * overwrite in place. If set to another value than 0, the
     * TemplatePlayer will use this value instead of the level given
     * when selecting or evaluating a move.
     *
     * @see #setLevelOverwrite(int)
     */
    public int getLevelOverwrite() {
        return levelOverwrite;
    }

    /**
     * This method enables to overwrite the level parameter value given
     * to selectMove() and/or evaluate(), so that the implementation will
     * use this value instead of the level given by the parameters.
     *
     * @see #getLevelOverwrite()
     */
    public void setLevelOverwrite(int levelOverwrite) {
        this.levelOverwrite = levelOverwrite;
    }

    /**
     * if enabled and Alpha-Beta tree search algorithm is used,
     * the tree search employs ordering of the legal moves
     * by their heuristic before the next level
     * is searched (to enhance tree pruning)
     */
    public boolean getOrderMoves() {
        return orderMoves;
    }

    /**
     * If set to true, Alpha-Beta search algorithm - if used -
     * will order the legal moves according to their heuristic
     * before searching the next level. This option makes sense
     * if the used heuristic is discriminating enough to prune the
     * search efficiently; otherwise it just adds substantial overhead
     * to the search as every node (and not just the leafs) will have to
     * be run through the player's heuristic function.
     */
    public void setOrderMoves(boolean enable) {
        orderMoves = enable;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String name) {
        playerName = name;
    }

    /**
     * unless overwritten, this method returns always false
     */
    public boolean pruneMove(GamePlay game, GameMove move, int[] role) {
        return false;
    }

    /**
     * This method will simply call the protected evaluate() method
     * and in addition track the call if tracking is enabled.
     * If tracking is enabled, a Monitor is used to track the number
     * of node examinations, the total time taken for the request and
     * the result calculated (stored as a Double in the monitor's setObject());
     * also, the used monitor will be disabled in time given that milliseconds
     * is greater than 0 - otherwise, only the level will limit the search.
     * If tracking is disabled, no monitor will be used; game tree search
     * is also limited to level and given milliseconds; in that case, this
     * method does nothing but calling the protected evaluate() method.
     *
     * @see #evaluate(GamePlay,GameMove,int[],int,long,Monitor)
     */
    public double evaluate(GamePlay game, GameMove move, int[] role, int level, long milliseconds) throws CannotPlayGameException {
        /*
        if ((game == null) || (role == null)) {
            throw (new IllegalArgumentException("parameters passed in here are null - bugger off"));
        }
        if (!canPlayGame(game)) {
            String s = "Sorry, I don't know how to play this game. ";
            s += "Next time, ask for a heuristic from someone who knows about the game.";
            throw (new CannotPlayGameException(this, game, s));
        }
        */
        Monitor monitor = null;
        synchronized (this) {
            if (monitors != null) {
                monitor = new Monitor();
                monitors.add(monitor);
            }
        }
        double result = evaluate(game, move, role, level, milliseconds, monitor);
        if (monitor != null) {
            monitor.setObject(new Double(result));
            monitor.timer.pause();
            monitor.disable();
        }
        return result;
    }

    /**
     * Internal evaluate function called by public evaluate() and selectMove().
     * This function doesn't track the request, it uses the given monitor and
     * assumes that the calling function tracks the monitor properly.
     * Based on what search algorithm is configured for this player and whether
     * or not this is a time-limited evaluation or not, it will use
     * an appropriate method from the GameUtils class to perform the evaluation.
     * Note that with the given algorithms, the search goes strictly in a
     * depth first search (dfs) fashion, which may lead to unwanted results if
     * the search is cut off before all levels have been searched (because no
     * iterative deepening is used, wich means that in case the search is cut
     * off early, some moves may not be searched beyond level 0);
     * this is subject to future improvements!
     *
     * @see GameUtils#minMaxSearch(GamePlay,GameMove,Player,int[],int,Monitor)
     * @see GameUtils#alphaBetaSearch(GamePlay,GameMove,Player,int[],int,Monitor,boolean)
     * @see org.jscience.util.Monitor
     */
    protected double evaluate(GamePlay game, GameMove move, int[] role, int level, long milliseconds, Monitor monitor) {
        double result;
        if (monitor != null) {
            if (milliseconds > 0) monitor.disableLater(milliseconds);
            switch (searchOption) {
                case(SEARCH_MINMAX):
                    result = GameUtils.minMaxSearch(game, move, this, role, ((levelOverwrite == 0) ? level : levelOverwrite), monitor);
                    break;
                case(SEARCH_ALPHABETA):
                    result = GameUtils.alphaBetaSearch(game, move, this, role, ((levelOverwrite == 0) ? level : levelOverwrite), monitor, orderMoves);
                    break;
                default:
                    result = heuristic(game, move, role);
            }
        } else {
            if (milliseconds > 0) {
                long time = System.currentTimeMillis() + milliseconds;
                switch (searchOption) {
                    case(SEARCH_MINMAX):
                        result = GameUtils.minMaxSearch(game, move, this, role, ((levelOverwrite == 0) ? level : levelOverwrite), time);
                        break;
                    case(SEARCH_ALPHABETA):
                        result = GameUtils.alphaBetaSearch(game, move, this, role, ((levelOverwrite == 0) ? level : levelOverwrite), time, orderMoves);
                        break;
                    default:
                        result = heuristic(game, move, role);
                }
            } else {
                switch (searchOption) {
                    case(SEARCH_MINMAX):
                        result = GameUtils.minMaxSearch(game, move, this, role, ((levelOverwrite == 0) ? level : levelOverwrite));
                        break;
                    case(SEARCH_ALPHABETA):
                        result = GameUtils.alphaBetaSearch(game, move, this, role, ((levelOverwrite == 0) ? level : levelOverwrite), orderMoves);
                        break;
                    default:
                        result = heuristic(game, move, role);
                }
            }
        }
        return result;
    }

    /**
     * This implementation selects the best move according to the given configuration.
     * If the time is constrained by milliseconds > 0, a thread is spanned for
     * each move; each thread will evaluate that move within the given time constrain.
     * If no time constrain is given, all evaluations will happen in the same
     * thread limited by the given level.
     * If tracking is enabled, a monitor will be added containing the number of nodes examined,
     * the time taken and the best move stored in Monitor.setObject().
     *
     * @see TemplatePlayer.Synchronizer
     * @see TemplatePlayer.MoveEvaluater
     */
    public GameMove selectMove(GamePlay game, int[] role, int level, long milliseconds) {
        /*
        if ((game == null) || (role == null)) {
            throw (new IllegalArgumentException("parameters passed in here are null - bugger off"));
        }
        if (!canPlayGame(game)) {
            String s = "Sorry, I don't know how to play this game. ";
            s += "Next time, ask for a heuristic from someone who knows about the game.";
            throw (new CannotPlayGameException(this, game, s));
        }
        */
        Monitor monitor = null;
        long time = 0;
        synchronized (this) {
            if (monitors != null) {
                monitor = new Monitor();
                monitors.add(monitor);
            }
        }
        double bestEval = Double.NEGATIVE_INFINITY;
        GameMove bestMove = null;
        GameMove myMoves[] = game.getLegalMoves();
        if (milliseconds > 0) {
            double[] results = new double[myMoves.length];
            ThreadGroup group = new ThreadGroup("MoveEvaluaters");
            Synchronizer synch = new Synchronizer(myMoves.length);
            for (int i = 0; i < myMoves.length; i++) {
                new Thread(group, new MoveEvaluater(synch, game, myMoves[i], role, level, milliseconds, monitor)).start();
            }
            synchronized (synch) {
                try {
                    synch.wait();
                    //synch.wait(milliseconds);
                } catch (InterruptedException e) {
                    throw new Error();
                }
                bestMove = synch.getBestMove();
            }
        } else {
            for (int i = 0; i < myMoves.length; i++) {
                double tmp = evaluate(game, myMoves[i], role, level, milliseconds, monitor);
                if ((bestMove == null) || (tmp > bestEval)) {
                    bestEval = tmp;
                    bestMove = myMoves[i];
                }
            }
        }
        if (monitor != null) {
            monitor.setObject(bestMove);
            monitor.timer.pause();
            monitor.disable();
        }
        return bestMove;
    }

    /**
     * overwritten to return some information about the player
     */
    public String toString() {
        String s = "Player: " + playerName;
        s += " (TemplatePlayer properties; search option: ";
        switch (searchOption) {
            case 0:
                s += "none";
                break;
            case SEARCH_MINMAX:
                s += "MinMax";
                break;
            case SEARCH_ALPHABETA:
                s += "AlphaBeta";
                break;
        }
        s += ", level overwrite: " + levelOverwrite;
        s += ", tracking ";
        if (trackingEnabled()) {
            s += "enabled)";
        } else {
            s += "disabled)";
        }
        return s;
    }

    /**
     * returns a String with the Player's statistics for logging/printing
     */
    public String statsAsString() {
        String s = "Player " + playerName;
        if (trackingEnabled()) {
            s += "; avaliable stats:";
            s += "\nnumber of requests: " + numberOfRequests();
            s += "\nnumber of positions searched: " + numberOfPositionsSearched();
            s += "\ntime taken: " + Stopwatch.timeAsString(totalTimeTaken());
            s += "\nperformance ratio: " + performanceRatio();
        } else {
            s += " - no stats available";
        }
        return s;
    }

    /**
     * MoveEvaluater is used by the method selectMove() from the enclosing
     * TemplatePLayer class in case of a time-limited search to allow each
     * move to be examined efficiently in a separate thread.
     *
     * @author Holger Antelmann
     * @see TemplatePlayer#selectMove(GamePlay,int[],int,long)
     * @see TemplatePlayer.Synchronizer
     * @since 3/26/2002
     */
    protected class MoveEvaluater implements Runnable {
        Synchronizer synch;
        GamePlay game;
        GameMove move;
        int[] role;
        int level;
        long milliseconds;
        Monitor monitor;

        public MoveEvaluater(Synchronizer synch, GamePlay game, GameMove move, int[] role, int level, long milliseconds, Monitor monitor) {
            this.synch = synch;
            this.game = game;
            this.move = move;
            this.role = role;
            this.level = level;
            this.milliseconds = milliseconds;
            this.monitor = monitor;
        }

        /**
         * reports the heuristic (calculated by the evaluate() method) to the Synchronizer
         *
         * @see TemplatePlayer#evaluate(GamePlay,GameMove,int[],int,long,Monitor)
         * @see TemplatePlayer.Synchronizer
         */
        public void run() {
            synch.report(move, evaluate(game, move, role, level, milliseconds, monitor));
        }
    }

    /**
     * Synchronizer is used consolidate the MoveEvaluater threads spanned off
     * by the selectMove() method from the enclosing TemplatePlayer.
     *
     * @author Holger Antelmann
     * @see TemplatePlayer.MoveEvaluater
     * @see TemplatePlayer#selectMove(GamePlay,int[],int,long)
     * @since 3/26/2002
     */
    protected class Synchronizer {
        int numberOfMoves;
        double bestEval = Double.NEGATIVE_INFINITY;
        GameMove bestMove = null;

        public Synchronizer(int numberOfMoves) {
            this.numberOfMoves = numberOfMoves;
        }

        synchronized protected void report(GameMove move, double heuristic) {
            if (heuristic > bestEval) {
                bestEval = heuristic;
                bestMove = move;
            }
            numberOfMoves--;
            if (numberOfMoves <= 0) {
                if (bestMove == null) bestMove = move;
                notifyAll();
            }
        }

        protected GameMove getBestMove() {
            return bestMove;
        }
    }
}
