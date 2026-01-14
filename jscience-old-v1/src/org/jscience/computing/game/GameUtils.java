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

import org.jscience.util.Monitor;

import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;
import java.util.Vector;


/**
 * The class GameUtils provides several algorithms for operating on
 * GamePlay objects. These algorithms include several game tree searches such
 * as MinMax or AlphaBeta, depth-first-search, breadth-first-search and
 * best-first-search with GamePlay objects as nodes. These algorithms are
 * commonly used by AutoPlay or Player objects. In addition, there are also
 * some other convenience functions that ease standard operations GamePlay
 * objects.
 *
 * @author Holger Antelmann
 *
 * @see GamePlay
 * @see AutoPlay
 * @see Player
 */
public final class GameUtils {
    /**
     * Creates a new GameUtils object.
     */
    private GameUtils() {
    }

    /**
     * returns a JGamePlay based on a GUI-popup selection
     *
     * @return DOCUMENT ME!
     */
    public static JGamePlay selectJGamePlay() {
        String[] games = new String[] {
                "Awari", "BlackJack", "Checkers", "Chess", "EightQueens",
                "FourWins", "Go-moku", "MineSweeper", "Muehle", "Reversi",
                "Solitaire", "Tick Tack Toe", "Tile Puzzle", "Wolf & Sheep"
            };
        String selection = (String) javax.swing.JOptionPane.showInputDialog(null,
                "select the game type", "starting a game",
                javax.swing.JOptionPane.PLAIN_MESSAGE, null, games, null);

        if (selection == null) {
            System.exit(0);
        }

        if (selection.equals("Awari")) {
            return new org.jscience.computing.game.awari.JAwari();
        }

        if (selection.equals("BlackJack")) {
            return new org.jscience.computing.game.cards.JBlackJack();
        }

        if (selection.equals("Checkers")) {
            return new org.jscience.computing.game.checkers.JCheckers();
        }

        if (selection.equals("Chess")) {
            return new org.jscience.computing.game.chess.JChess();
        }

        if (selection.equals("EightQueens")) {
            return new JDefaultGame(new org.jscience.computing.game.puzzle.EightQueens());
        }

        if (selection.equals("FourWins")) {
            return new org.jscience.computing.game.fourwins.JFourWins();
        }

        if (selection.equals("Go-moku")) {
            return new org.jscience.computing.game.gomoku.JGomoku();
        }

        if (selection.equals("MineSweeper")) {
            return new JDefaultGame(new org.jscience.computing.game.mine.MineSweeper());
        }

        if (selection.equals("Muehle")) {
            return new org.jscience.computing.game.muehle.JMuehle();
        }

        if (selection.equals("Reversi")) {
            return new org.jscience.computing.game.reversi.JReversi();
        }

        if (selection.equals("Tile Puzzle")) {
            return new org.jscience.computing.game.puzzle.JTilePuzzle();
        }

        if (selection.equals("Solitaire")) {
            return new org.jscience.computing.game.puzzle.JSolitaire();
        }

        if (selection.equals("Tick Tack Toe")) {
            return new JDefaultGame(new org.jscience.computing.game.tictactoe.TickTackToe(),
                new Player[] {
                    new RandomPlayer("X", System.currentTimeMillis(), true,
                        TemplatePlayer.SEARCH_ALPHABETA, true),
                    new RandomPlayer("O", System.currentTimeMillis(), true,
                        TemplatePlayer.SEARCH_ALPHABETA, true)
                }, 8,
                new org.jscience.io.ExtensionFileFilter("ttt",
                    "TickTackToe games (*.ttt)"));
        }

        if (selection.equals("Wolf & Sheep")) {
            return new org.jscience.computing.game.wolfsheep.JWS();
        }

        return null;
    }

    /**
     * getLegalMovesSorted() sorts the legal moves of the given game
     * descending or ascending by their heuristic calculated by the given
     * player based on the roles.
     *
     * @param game DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param descending DOCUMENT ME!
     *
     * @return the sorted array of the legal GameMove objects for the game
     */
    public static GameMove[] getLegalMovesSorted(GamePlay game, Player player,
        int[] roles, boolean descending) {
        GameMove[] moves = game.getLegalMoves();
        GameMove[] sorted = new GameMove[moves.length];
        double[] heuristics = new double[moves.length];
        double h;

        for (int i = 0; i < moves.length; i++) {
            h = player.heuristic(game, moves[i], roles);

            int position;

            // finding insertion index -> position
            for (position = 0; position < i; position++) {
                if (descending) {
                    if (heuristics[position] < h) {
                        break;
                    }
                } else {
                    if (heuristics[position] > h) {
                        break;
                    }
                }
            }

            // shifting the exising moves up between end and insertion index
            for (int x = i; x > position; x--) {
                sorted[x] = sorted[x - 1];
                heuristics[x] = heuristics[x - 1];
            }

            sorted[position] = moves[i];
            heuristics[position] = h;
        }

        return sorted;
    }

    /**
     * implements MinMax Search algorithm and returns the evaluation
     * given by the player's heuristic functions at the leaves; tracking is
     * enabled tracking through a Monitor object. Use of the Monitor (suitable
     * for multi-threaded environments): minMaxSearch() - provided that the
     * game level is also still greater than 0 - only deepens the search
     * further if the monitor is still enabled. When player.heuristic() is
     * called by this function, it will also call monitor.increment() to
     * indicate to an observing thread how many times a leaf node has been
     * examined. Note that if a search is cut off prematurely by disabling the
     * monitor, the returned value may reflect an unbalanced result due to the
     * depth-first-search nature of the algorithm.
     *
     * @param game the GamePlay containing the game state information with the
     *        ability to create its children
     * @param move the move to be evaluated
     * @param player used to call the Player's heuristic function if node is a
     *        leaf
     * @param role an int[] containing the roles the given Player plays in the
     *        game; this is used to determine whether it's the Player's or the
     *        opponent's move
     * @param level the game level used to limit the tree search
     * @param monitor used to cut off search externally and provide running
     *        feedback to a listening thread
     *
     * @return DOCUMENT ME!
     *
     * @see org.jscience.computing.game.Player
     * @see org.jscience.util.Monitor
     */
    public static double minMaxSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, Monitor monitor) {
        if ((level <= 0) || monitor.disabled()) {
            monitor.increment();

            return player.heuristic(game, move, role);
        }

        GamePlay child = game.spawnChild(move);
        GameMove[] childMove = child.getLegalMoves();

        if (childMove.length == 0) {
            monitor.increment();

            return player.heuristic(game, move, role);
        }

        boolean max = isInArray(role, child.nextPlayer());
        double best = (max) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        double tmp;

        for (int i = 0; i < childMove.length; i++) {
            if (player.pruneMove(child, childMove[i], role)) {
                tmp = player.heuristic(child, childMove[i], role);
            } else {
                tmp = minMaxSearch(child, childMove[i], player, role,
                        level - 1, monitor);
            }

            if (max) {
                if (tmp > best) {
                    best = tmp;
                }
            } else {
                if (tmp < best) {
                    best = tmp;
                }
            }
        }

        return best;
    }

    /**
     * implements MinMax Search algorithm and returns the evaluation
     * given by the player's heuristic functions at the leaves; will only run
     * as deep as given by the level and only as long as given time is less
     * than System.currentTimeMillis(). Note that searches that are limited by
     * time may only be as 'valuable' as a 0-level search if the search is cut
     * off prematurely, as some tree branches may only be searched as deep as
     * 0 - due to the depth-first-search nature of the algorithm.
     *
     * @param game the GamePlay containing the game state information with the
     *        ability to create its children
     * @param move the move to be evaluated
     * @param player used to call the Player's heuristic function if node is a
     *        leaf
     * @param role an int[] containing the roles the given Player plays in the
     *        game; this is used to determine whether it's the Player's or the
     *        opponent's move
     * @param level the game level used to limit the tree search
     * @param time used to cut off search when the given time is before
     *        System.currentTimeMillis()
     *
     * @return DOCUMENT ME!
     *
     * @see org.jscience.computing.game.Player
     */
    public static double minMaxSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, long time) {
        if ((level <= 0) || (System.currentTimeMillis() >= time)) {
            return player.heuristic(game, move, role);
        }

        GamePlay child = game.spawnChild(move);
        GameMove[] childMove = child.getLegalMoves();

        if (childMove.length == 0) {
            return player.heuristic(game, move, role);
        }

        boolean max = isInArray(role, child.nextPlayer());
        double best = (max) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        double tmp;

        for (int i = 0; i < childMove.length; i++) {
            if (player.pruneMove(child, childMove[i], role)) {
                tmp = player.heuristic(child, childMove[i], role);
            } else {
                tmp = minMaxSearch(child, childMove[i], player, role,
                        level - 1, time);
            }

            if (max) {
                if (tmp > best) {
                    best = tmp;
                }
            } else {
                if (tmp < best) {
                    best = tmp;
                }
            }
        }

        return best;
    }

    /**
     * implements MinMax Search algorithm and returns the evaluation
     * given by the player's heuristic functions at the leaves; limited only
     * by a deepening level
     *
     * @param game the GamePlay containing the game state information with the
     *        ability to create its children
     * @param move the move to be evaluated
     * @param player used to call the Player's heuristic function if node is a
     *        leaf
     * @param role an int[] containing the roles the given Player plays in the
     *        game; this is used to determine whether it's the Player's or the
     *        opponent's move
     * @param level the game level used to limit the tree search
     *
     * @return DOCUMENT ME!
     *
     * @see org.jscience.computing.game.Player
     */
    public static double minMaxSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level) {
        if (level <= 0) {
            return player.heuristic(game, move, role);
        }

        GamePlay child = game.spawnChild(move);
        GameMove[] childMove = child.getLegalMoves();

        if (childMove.length == 0) {
            return player.heuristic(game, move, role);
        }

        boolean max = isInArray(role, child.nextPlayer());
        double best = (max) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        double tmp;

        for (int i = 0; i < childMove.length; i++) {
            if (player.pruneMove(child, childMove[i], role)) {
                tmp = player.heuristic(child, childMove[i], role);
            } else {
                tmp = minMaxSearch(child, childMove[i], player, role, level -
                        1);
            }

            if (max) {
                if (tmp > best) {
                    best = tmp;
                }
            } else {
                if (tmp < best) {
                    best = tmp;
                }
            }
        }

        return best;
    }

    /**
     * This function implements Alpha-Beta Search algorithm and returns
     * the evaluation given by the player's heuristic functions at the leaves
     * (intelligently pruned MinMax algorithm) - monitor version (search is
     * cut off when monitor disabled). Parameters passed and monitor usage are
     * identical to corresponding minMaxSearch function with the addition of
     * the boolean orderMoves, which - if enabled - will order the legal moves
     * according to their heuristic (best heuristic value first) before
     * continuing the search in the next level (to improve tree pruning).
     * Using the parameter orderMoves trades off between faster tree search
     * (orderMoves = false) vs. potentially more effective tree pruning
     * (orderMoves = true). Note that if a search is cut off prematurely by
     * disabling the monitor, the returned value may reflect an unbalanced
     * result due to the depth-first-search nature of the algorithm. The
     * function simply calls the other alphaBetaSearch function by appending
     * Double.NEGATIVE_INFINITY and Double.POSITIVE_INFINITY as values for
     * alpha and beta.
     *
     * @see org.jscience.util.Monitor
     */
    public static double alphaBetaSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, Monitor monitor,
        boolean orderMoves) {
        return alphaBetaSearch(game, move, player, role, level,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, monitor,
            orderMoves);
    }

    /**
     * This function is usually just called by the other corresponding
     * monitored alphaBetaSearch function. This function can be called
     * directly if one wishes to set initial values for alpha and beta
     * explicitly. The use of the monitor is equivalent to corresponding
     * minMaxSearch()'s use of the monitor. Note that if a search is cut off
     * prematurely by disabling the monitor, the returned value may reflect an
     * unbalanced result due to the depth-first-search nature of the
     * algorithm.
     *
     * @see org.jscience.util.Monitor
     */
    public static double alphaBetaSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, double alpha, double beta,
        Monitor monitor, boolean orderMoves) {
        if ((level <= 0) || (alpha >= beta) || monitor.disabled()) {
            monitor.increment();

            return player.heuristic(game, move, role);
        }

        GamePlay child = game.spawnChild(move);
        boolean max = isInArray(role, child.nextPlayer());
        GameMove[] childMove;

        if (orderMoves) {
            childMove = getLegalMovesSorted(child, player, role, max);
        } else {
            childMove = child.getLegalMoves();
        }

        if (childMove.length == 0) {
            monitor.increment();

            return player.heuristic(game, move, role);
        }

        double tmp;

        for (int i = 0; ((i < childMove.length) && (alpha < beta)); i++) {
            if (player.pruneMove(child, childMove[i], role)) {
                tmp = player.heuristic(child, childMove[i], role);
            } else {
                tmp = alphaBetaSearch(child, childMove[i], player, role,
                        level - 1, alpha, beta, monitor, orderMoves);
            }

            if (max) {
                if (tmp > alpha) {
                    alpha = tmp;
                }
            } else {
                if (tmp < beta) {
                    beta = tmp;
                }
            }
        }

        if (max) {
            return alpha;
        } else {
            return beta;
        }
    }

    /**
     * This function implements Alpha-Beta Search algorithm
     * (intelligently pruned MinMax algorithm) and returns the evaluation
     * given by the player's heuristic functions at the leaves - timed version
     * (search cut off at given time). The function simply calls the other
     * alphaBetaSearch function by appending Double.NEGATIVE_INFINITY and
     * Double.POSITIVE_INFINITY as values for alpha and beta.
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param time DOCUMENT ME!
     * @param orderMoves DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double alphaBetaSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, long time, boolean orderMoves) {
        return alphaBetaSearch(game, move, player, role, level,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, time, orderMoves);
    }

    /**
     * This function is usually just called by the other timed
     * alphaBetaSearch function. This function can be called directly if one
     * wishes to set initial values for alpha and beta explicitly. Search is
     * cut off when level is reached or when System.currentTimeMillis() is
     * larger than given time. Note that searches that are limited by time may
     * only be as 'valuable' as a 0-level search if the search is cut off
     * prematurely, as some tree branches may only be searched as deep as 0 -
     * due to the depth-first-search nature of the algorithm.
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param alpha DOCUMENT ME!
     * @param beta DOCUMENT ME!
     * @param time DOCUMENT ME!
     * @param orderMoves DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double alphaBetaSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, double alpha, double beta,
        long time, boolean orderMoves) {
        if ((level <= 0) || (alpha >= beta) ||
                (System.currentTimeMillis() >= time)) {
            return player.heuristic(game, move, role);
        }

        GamePlay child = game.spawnChild(move);
        GameMove[] childMove;
        boolean max = isInArray(role, child.nextPlayer());

        if (orderMoves) {
            childMove = getLegalMovesSorted(child, player, role, max);
        } else {
            childMove = child.getLegalMoves();
        }

        if (childMove.length == 0) {
            return player.heuristic(game, move, role);
        }

        double tmp;

        for (int i = 0; ((i < childMove.length) && (alpha < beta)); i++) {
            if (player.pruneMove(child, childMove[i], role)) {
                tmp = player.heuristic(child, childMove[i], role);
            } else {
                tmp = alphaBetaSearch(child, childMove[i], player, role,
                        level - 1, alpha, beta, time, orderMoves);
            }

            if (max) {
                if (tmp > alpha) {
                    alpha = tmp;
                }
            } else {
                if (tmp < beta) {
                    beta = tmp;
                }
            }
        }

        if (max) {
            return alpha;
        } else {
            return beta;
        }
    }

    /**
     * This function implements Alpha-Beta Search algorithm
     * (intelligently pruned MinMax algorithm) and returns the evaluation
     * given by the player's heuristic functions at the leaves; limited only
     * by deepening level. Parameters passed are identical to corresponding
     * minMaxSearch The function simply calls the other alphaBetaSearch
     * function by appending Double.NEGATIVE_INFINITY and
     * Double.POSITIVE_INFINITY as values for alpha and beta.
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param orderMoves DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double alphaBetaSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, boolean orderMoves) {
        return alphaBetaSearch(game, move, player, role, level,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, orderMoves);
    }

    /**
     * This function is usually just called by the other corresponding
     * alphaBetaSearch function. This function can be called directly if one
     * wishes to set initial values for alpha and beta explicitly.
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param alpha DOCUMENT ME!
     * @param beta DOCUMENT ME!
     * @param orderMoves DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double alphaBetaSearch(GamePlay game, GameMove move,
        Player player, int[] role, int level, double alpha, double beta,
        boolean orderMoves) {
        if ((level <= 0) || (alpha >= beta)) {
            return player.heuristic(game, move, role);
        }

        GamePlay child = game.spawnChild(move);
        GameMove[] childMove;
        boolean max = isInArray(role, child.nextPlayer());

        if (orderMoves) {
            childMove = getLegalMovesSorted(child, player, role, max);
        } else {
            childMove = child.getLegalMoves();
        }

        if (childMove.length == 0) {
            return player.heuristic(game, move, role);
        }

        double tmp;

        for (int i = 0; ((i < childMove.length) && (alpha < beta)); i++) {
            if (player.pruneMove(child, childMove[i], role)) {
                tmp = player.heuristic(child, childMove[i], role);
            } else {
                tmp = alphaBetaSearch(child, childMove[i], player, role,
                        level - 1, alpha, beta, orderMoves);
            }

            if (max) {
                if (tmp > alpha) {
                    alpha = tmp;
                }
            } else {
                if (tmp < beta) {
                    beta = tmp;
                }
            }
        }

        if (max) {
            return alpha;
        } else {
            return beta;
        }
    }

    /**
     * This convenience function checks whether there is a winner in
     * the game corresponding to one of the given roles in the array; if so,
     * it returns 1 in case the Player is among the winners, and -1 if that's
     * not the case; a zero is returned if there are no winners or if
     * GamePlay.getWinner() returns null.
     *
     * @param game DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int checkForWin(GamePlay game, int[] role) {
        int[] winners = game.getWinner();

        if (winners != null) {
            for (int i = 0; i < role.length; i++) {
                if (isInArray(winners, role[i])) {
                    return 1;
                }
            }

            if (winners.length > 0) {
                return -1;
            }
        }

        return 0;
    }

    /**
     * depthFirstSearch() is a 'dfs puzzle-solver' that tries to find a
     * path to a winning game position defined by the given roles within the
     * given number of moves. The search is done recursively in
     * 'depth-first-search' manner with backtracking, i.e. the method is not
     * guaranteed to find the shortest path. If the given monitor is not null,
     * it can be used to cut off the recursive calls by disabling the monitor
     * from a different thread; also, monitor.increment() is called for every
     * time the function is entered - counting the number of nodes examined.
     * This function doesn't check for who is making the moves; it's really
     * more intended to solve 'puzzles' that are represented by a GamePlay.
     *
     * @param game DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param numberOfMoves DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return null if no win can be found or the GamePlay that represents the
     *         winning position that can be reached within the given number of
     *         moves; with GamePlay.getMoveHistory() one can then examine the
     *         winning path included in the game status.
     */
    public static GamePlay depthFirstSearch(GamePlay game, int[] roles,
        int numberOfMoves, Monitor monitor) {
        if (monitor != null) {
            monitor.increment();
        }

        if (checkForWin(game, roles) == 1) {
            return game;
        }

        if ((monitor != null) && monitor.disabled()) {
            return null;
        }

        if (numberOfMoves <= 0) {
            return null;
        }

        GameMove[] moves = game.getLegalMoves();
        GamePlay child = null;

        for (int i = 0; i < moves.length; i++) {
            child = depthFirstSearch(game.spawnChild(moves[i]), roles,
                    numberOfMoves - 1, monitor);

            if (child != null) {
                return child;
            }
        }

        return null;
    }

    /**
     * This depthFirstSearch() variant sorts the moves according to
     * their heuristics provided by the given player before performing its
     * otherwise 'depth-first-search' algorithm. If the given monitor is not
     * null, it can be used to cut off the recursive calls by disabling the
     * monitor from a different thread; also, monitor.increment() is called
     * for every time the function is entered - counting the number of nodes
     * examined. This function doesn't check for who is making the moves; it's
     * really more intended to solve 'puzzles' that are represented by a
     * GamePlay.
     *
     * @param game DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param numberOfMoves DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return null if no win can be found or the GamePlay that represents the
     *         winning position that can be reached within the given number of
     *         moves; with GamePlay.getMoveHistory() one can then examine the
     *         winning path included in the game status.
     */
    public static GamePlay depthFirstSearch(GamePlay game, int[] roles,
        int numberOfMoves, Player player, Monitor monitor) {
        if (monitor != null) {
            monitor.increment();
        }

        if (checkForWin(game, roles) == 1) {
            return game;
        }

        if ((monitor != null) && monitor.disabled()) {
            return null;
        }

        if (numberOfMoves <= 0) {
            return null;
        }

        GameMove[] moves = getLegalMovesSorted(game, player, roles, true);
        GamePlay child = null;

        for (int i = 0; i < moves.length; i++) {
            child = depthFirstSearch(game.spawnChild(moves[i]), roles,
                    numberOfMoves - 1, player, monitor);

            if (child != null) {
                return child;
            }
        }

        return null;
    }

    /**
     * A 'best-first-search' algorithm that looks for a path to win the
     * game according to the given roles. Note that the given player
     * <b>must</b> return a heuristic value that is consistent  with the
     * GamePlay's equals() method, i.e. if two GamePlay objects are the same
     * according to the equals() method, they must return the same heuristic
     * by the player. Use of monitor (if not null): monitor.increment() is
     * called for every node examined. A call to this function uses high
     * amounts of memory due to maintaining an open and closed list.
     *
     * @param game DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param maxNumberOfNodes the maximum number of nodes to be examined;
     *        since this function does not go strictly breadth or depth first,
     *        this number does not correlate with the number of moves away
     *        from the initial game status;
     * @param player DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws OutOfMemoryError DOCUMENT ME!
     */
    public static GamePlay bestFirstSearch(GamePlay game, int[] roles,
        int maxNumberOfNodes, Player player, Monitor monitor)
        throws OutOfMemoryError {
        java.util.TreeSet<Object> openList = new java.util.TreeSet<Object>();
        java.util.HashSet<Object> closedList = new java.util.HashSet<Object>();
        openList.add(new ComparableGame(game, Double.NEGATIVE_INFINITY));

        return bestFirstSearch(openList, closedList, roles, maxNumberOfNodes,
            player, monitor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param openSet DOCUMENT ME!
     * @param closedSet DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param maxNumberOfNodes DOCUMENT ME!
     * @param player DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static GamePlay bestFirstSearch(SortedSet<Object> openSet,
        Set<Object> closedSet, int[] roles, int maxNumberOfNodes,
        Player player, Monitor monitor) {
        if (openSet.isEmpty()) {
            return null;
        }

        ComparableGame current = (ComparableGame) openSet.last();
        GamePlay game = current.game;

        if (checkForWin(game, roles) == 1) {
            return game;
        }

        maxNumberOfNodes--;

        if (monitor != null) {
            monitor.increment();
        }

        if (openSet.size() < maxNumberOfNodes) {
            openSet.remove(current);
            closedSet.add(current);

            GameMove[] moves = game.getLegalMoves();

            for (int i = 0; i < moves.length; i++) {
                if (openSet.size() > maxNumberOfNodes) {
                    break;
                }

                GamePlay child = game.spawnChild(moves[i]);
                double heuristic = player.heuristic(game, moves[i], roles);
                ComparableGame node = new ComparableGame(child, heuristic);

                if (!closedSet.contains(node)) {
                    openSet.add(node);
                }
            }
        }

        return bestFirstSearch(openSet, closedSet, roles, maxNumberOfNodes,
            player, monitor);
    }

    /**
     * breadthFirstSearch() is a 'breadth-first-search' puzzle-solver.
     * The method is guaranteed to find the shortest path if one exists; the
     * drawback is that it uses extensive resources (any non-leaf game status
     * needs to be kept in memory). If the given monitor is not null, it can
     * be used to cut off the recursive calls by disabling the monitor from a
     * different thread; also, monitor.increment() is called for every time
     * the function is entered - counting the number of nodes examined.
     * Additionally, monitor.runTask() is called when a new level is reached
     * (i.e. when the number of moves is increased). This function doesn't
     * check for who is making the moves; it's really more intended to solve
     * 'puzzles' that are represented by a GamePlay.
     *
     * @param game DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param numberOfMoves DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return null if no win can be found or the GamePlay that represents the
     *         winning position that can be reached within the given number of
     *         moves; with GamePlay.getMoveHistory() one can then examine the
     *         winning path included in the game status. Note that this
     *         function can easily throw an OutOfMemoryError, if the number of
     *         game positions to be searched grows too large (since the bfs
     *         algorithm needs to maintain the list of moves to be followed up
     *         on), so keep the numberOfMoves in reasonable limits.
     *
     * @throws OutOfMemoryError if too many game positions are to be searched
     */
    public static GamePlay breadthFirstSearch(GamePlay game, int[] roles,
        int numberOfMoves, Monitor monitor) throws OutOfMemoryError {
        Vector<Object> openList = new Vector<Object>();
        openList.add(game);

        //Vector closedList = new Vector();
        return breadthFirstSearch(openList, roles, numberOfMoves, monitor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param openList DOCUMENT ME!
     * @param roles DOCUMENT ME!
     * @param numberOfMoves DOCUMENT ME!
     * @param monitor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static GamePlay breadthFirstSearch(Vector<Object> openList, int[] roles,
        int numberOfMoves, Monitor monitor) {
        if (openList.isEmpty()) {
            return null;
        }

        Iterator it = openList.iterator();

        while (it.hasNext()) {
            GamePlay game = (GamePlay) it.next();

            if (monitor != null) {
                monitor.increment();

                if (monitor.disabled()) {
                    return null;
                }
            }

            if (checkForWin(game, roles) == 1) {
                return game;
            }
        }

        if (numberOfMoves <= 0) {
            return null;
        }

        GamePlay[] games = (GamePlay[]) openList.toArray(new GamePlay[openList.size()]);
        openList.removeAllElements();

        GameMove[] moves;

        for (int i = 0; i < games.length; i++) {
            if ((monitor != null) && monitor.disabled()) {
                return null;
            }

            moves = ((GamePlay) games[i]).getLegalMoves();

            for (int x = 0; x < moves.length; x++) {
                openList.add(games[i].spawnChild(moves[x]));
            }
        }

        monitor.runTask();

        return breadthFirstSearch(openList, roles, numberOfMoves - 1, monitor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public boolean matchInArrays(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < b.length; j++) {
                if (a[i] == b[j]) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param array DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public boolean isInArray(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (element == array[i]) {
                return true;
            }
        }

        return false;
    }

    /**
     * The proper use of this class requires that if a GamePlay object
     * is the same according to its equals() method, the heuristic is also the
     * same.
     *
     * @see #bestFirstSearch(GamePlay,int[],int,Player,Monitor)
     */
    static class ComparableGame implements Comparable<Object> {
        /**
         * DOCUMENT ME!
         */
        GamePlay game;

        /**
         * DOCUMENT ME!
         */
        double heuristic;

        /**
         * Creates a new ComparableGame object.
         *
         * @param game DOCUMENT ME!
         * @param heuristic DOCUMENT ME!
         */
        ComparableGame(GamePlay game, double heuristic) {
            this.game = game;
            this.heuristic = heuristic;
        }

        /**
         * DOCUMENT ME!
         *
         * @param o DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public int compareTo(Object o) {
            ComparableGame ln = (ComparableGame) o;

            if (heuristic == ln.heuristic) {
                return 0;
            }

            if (heuristic > ln.heuristic) {
                return 1;
            }

            return -1;
        }

        /**
         * DOCUMENT ME!
         *
         * @param obj DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public boolean equals(Object obj) {
            return game.equals(((ComparableGame) obj).game);
        }
    }
}
