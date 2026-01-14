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
