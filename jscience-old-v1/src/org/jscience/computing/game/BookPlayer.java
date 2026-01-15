/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game;

import java.io.Serializable;

import java.util.Hashtable;


/**
 * BookPlayer is a Player wrapper that can significantly improve normal
 * Player performance by predefining move selections. This is achieved by
 * internally using a Hashtable object as a <dfn>book</dfn>. When asked to
 * select a move in a game, the BookPlayer first checks its book (Hashtable)
 * to see whether a given game is present as a key; If so, it will return the
 * GameMove object stored as the associated value to that key (which is
 * assumed to be the desired move earlier put in the book (which is the
 * Hashtable). Only if the GameBook doesn't contain the game in question, the
 * selection is passed on to the embedded Player object for standard
 * procedure, as all other requests defined in the Player interface are routed
 * to the embedded player as well. Naturally, the BookPlayer relies on a
 * properly maintained book to perform well.
 *
 * @author Holger Antelmann
 */
public class BookPlayer implements Player, Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 2556195920558121632L;

    /**
     * DOCUMENT ME!
     */
    Hashtable<GamePlay, GameMove> book;

    /**
     * DOCUMENT ME!
     */
    Player player;

    /**
     * DOCUMENT ME!
     */
    String name;

    /**
     * DOCUMENT ME!
     */
    int bookHits = 0;

    /**
     * Creates a new BookPlayer object.
     *
     * @param player DOCUMENT ME!
     */
    public BookPlayer(Player player) {
        this(player.getPlayerName(), player);
    }

/**
     * allows the BookPlayer to have a name different from the embedded player
     *
     * @see #getPlayerName()
     */
    public BookPlayer(String name, Player player) {
        this.name = name;
        this.player = player;
        book = new Hashtable<GamePlay, GameMove>();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPlayerName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canPlayGame(GamePlay game) {
        return player.canPlayGame(game);
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
    public boolean pruneMove(GamePlay game, GameMove move, int[] role) {
        return player.pruneMove(game, move, role);
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     * @param level DOCUMENT ME!
     * @param milliseconds DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double evaluate(GamePlay game, GameMove move, int[] role, int level,
        long milliseconds) {
        return player.evaluate(game, move, role, level, milliseconds);
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
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        return player.heuristic(game, move, role);
    }

    /**
     * first checks the embedded book for a pre-stored move
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
        GameMove move = (GameMove) book.get(game);

        if (move != null) {
            bookHits++;

            return move;
        }

        return player.selectMove(game, role, level, milliseconds);
    }

    /**
     * returns the number of times that this BookPlayer had a hit on
     * the embedded GameBook while processing a move selection
     *
     * @return DOCUMENT ME!
     */
    public int bookHits() {
        return bookHits;
    }

    /**
     * Sets the internal book used by this BookPlayer. The given book
     * is expected to have GamePlay objects as keys and a GameMove object (to
     * be returned if selectMove() is called on an equal GamePlay object) as
     * values, where the move is expected to be a legal move for each game.
     *
     * @param book DOCUMENT ME!
     */
    public void setBook(Hashtable<GamePlay, GameMove> book) {
        this.book = book;
    }

    /**
     * allows access to the embedded book to add/remove
     * GamePlay-GameMove pairs
     *
     * @return DOCUMENT ME!
     */
    public Hashtable getBook() {
        return book;
    }

    /**
     * DOCUMENT ME!
     *
     * @param player DOCUMENT ME!
     */
    public void changePlayer(Player player) {
        this.player = player;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Player getPlayer() {
        return player;
    }
}
