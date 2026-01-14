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

package org.jscience.computing.game.cards;

import java.io.Serializable;


/**
 * a representation of a game card
 *
 * @author Holger Antelmann
 */
public class Card implements Serializable {
    /** DOCUMENT ME! */
    static final long serialVersionUID = 6892906178549182113L;

    // constants
    /** DOCUMENT ME! */
    public static final boolean BIG_DECK = true;

    /** DOCUMENT ME! */
    public static final boolean SMALL_DECK = false;

    /** DOCUMENT ME! */
    public static final byte CLUBS = 12;

    /** DOCUMENT ME! */
    public static final byte SPADES = 11;

    /** DOCUMENT ME! */
    public static final byte HEARTS = 10;

    /** DOCUMENT ME! */
    public static final byte DIAMONDS = 9;

    /** DOCUMENT ME! */
    public static final int ACE = 14;

    /** DOCUMENT ME! */
    public static final int KING = 13;

    /** DOCUMENT ME! */
    public static final int QUEEN = 12;

    /** DOCUMENT ME! */
    public static final int JACK = 11;

    /** DOCUMENT ME! */
    public static final int TEN = 10;

    /** DOCUMENT ME! */
    public static final int NINE = 9;

    /** DOCUMENT ME! */
    public static final int EIGHT = 8;

    /** DOCUMENT ME! */
    public static final int SEVEN = 7;

    /** DOCUMENT ME! */
    public static final int SIX = 6;

    /** DOCUMENT ME! */
    public static final int FIVE = 5;

    /** DOCUMENT ME! */
    public static final int FOUR = 4;

    /** DOCUMENT ME! */
    public static final int THREE = 3;

    /** DOCUMENT ME! */
    public static final int TWO = 2;

    /** DOCUMENT ME! */
    public static final int JOKER = 25;

    // member variables
    /** DOCUMENT ME! */
    private final int rank;

    /** DOCUMENT ME! */
    private final byte suit;

/**
     * to instanciate a Joker only; accepts only JOKER as type
     *
     * @param type DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Card(int type) throws IllegalArgumentException {
        if (type != JOKER) {
            throw new IllegalArgumentException(
                "this constructor only accepts joker");
        }

        rank = type;
        suit = 0;
    }

/**
     * use static members of this class for parameters
     *
     * @param rank DOCUMENT ME!
     * @param suit DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Card(int rank, byte suit) throws IllegalArgumentException {
        switch (rank) {
        case ACE:
        case KING:
        case QUEEN:
        case JACK:
        case TEN:
        case NINE:
        case EIGHT:
        case SEVEN:
        case SIX:
        case FIVE:
        case FOUR:
        case THREE:
        case TWO:
            this.rank = rank;

            break;

        default:
            throw (new IllegalArgumentException(
                "Card cannot be instanciated; value not valid."));
        }

        switch (suit) {
        case CLUBS:
        case SPADES:
        case HEARTS:
        case DIAMONDS:
            this.suit = suit;

            break;

        default:
            throw (new IllegalArgumentException(
                "Card cannot be instanciated; suit not valid."));
        }
    }

    /**
     * returns true only if the Card is one that belongs in a small
     * deck (Ace, King, Queen, Jack, Ten, Nine, Eight, Seven)
     *
     * @return DOCUMENT ME!
     */
    public boolean isInSmallDeck() {
        switch (rank) {
        case ACE:
        case KING:
        case QUEEN:
        case JACK:
        case TEN:
        case NINE:
        case EIGHT:
        case SEVEN:
            return true;

        default:
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte getSuit() {
        return suit;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!(obj instanceof Card)) {
            return false;
        }

        return (((sameRank((Card) obj)) && (sameSuit((Card) obj))) ? true : false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean sameRank(Card c) {
        return ((c.getRank() == rank) ? true : false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean sameSuit(Card c) {
        return ((c.getSuit() == suit) ? true : false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRank() {
        return rank;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = null;

        switch (rank) {
        case ACE:
            s = "A";

            break;

        case KING:
            s = "K";

            break;

        case QUEEN:
            s = "Q";

            break;

        case JACK:
            s = "J";

            break;

        case TEN:
            s = "10";

            break;

        case NINE:
            s = "9";

            break;

        case EIGHT:
            s = "8";

            break;

        case SEVEN:
            s = "7";

            break;

        case SIX:
            s = "6";

            break;

        case FIVE:
            s = "5";

            break;

        case FOUR:
            s = "4";

            break;

        case THREE:
            s = "3";

            break;

        case TWO:
            s = "2";

            break;

        case JOKER:
            return "Joker";
        }

        switch (suit) {
        case CLUBS:
            s += "c";

            break;

        case SPADES:
            s += "s";

            break;

        case HEARTS:
            s += "h";

            break;

        case DIAMONDS:
            s += "d";

            break;
        }

        return s;
    }
}
