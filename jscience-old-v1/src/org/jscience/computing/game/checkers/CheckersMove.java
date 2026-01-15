/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.checkers;

import org.jscience.computing.game.MoveTemplate;


/**
 * CheckersMove implements a GameMove for a CheckersGame. Note that a
 * capture move that involves more than one jump is represented as multiple
 * moves by the same player, i.e. each jump is a separate move carried out
 * separately. The game logic in CheckersGame ensures that it is always the
 * correct player's turn.
 *
 * @author Holger Antelmann
 *
 * @see CheckersGame
 */
class CheckersMove extends MoveTemplate {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 2552976547877711318L;

    /** DOCUMENT ME! */
    private int from;

    /** DOCUMENT ME! */
    private int to;

    /** DOCUMENT ME! */
    private CheckersPiece taken;

    /** DOCUMENT ME! */
    private double heuristic;

    /** DOCUMENT ME! */
    private boolean promotion;

/**
     * Constructs a normal (non-capture) move.
     *
     * @param player        DOCUMENT ME!
     * @param from          DOCUMENT ME!
     * @param to            DOCUMENT ME!
     * @param kingPromotion DOCUMENT ME!
     */
    public CheckersMove(int player, int from, int to, boolean kingPromotion) {
        this(player, from, to, null, kingPromotion);
    }

/**
     * Constructs a capture move (taken piece needed to enable undo)
     *
     * @param player        DOCUMENT ME!
     * @param from          DOCUMENT ME!
     * @param to            DOCUMENT ME!
     * @param taken         DOCUMENT ME!
     * @param kingPromotion DOCUMENT ME!
     */
    public CheckersMove(int player, int from, int to, CheckersPiece taken,
        boolean kingPromotion) {
        super(player);
        this.from = from;
        this.to = to;
        this.taken = taken;
        promotion = kingPromotion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFrom() {
        return from;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTo() {
        return to;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getPromotion() {
        return promotion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CheckersPiece getTaken() {
        return taken;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ("from: " + from + " to: " + to);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        CheckersMove m = (CheckersMove) obj;

        if (player != m.player) {
            return false;
        }

        if (from != m.from) {
            return false;
        }

        if (to != m.to) {
            return false;
        }

        if (promotion != promotion) {
            return false;
        }

        if (taken == null) {
            if (m.taken != null) {
                return false;
            }
        } else {
            if ((m.taken == null) || (!m.taken.equals(taken))) {
                return false;
            }
        }

        return true;
    }
}
