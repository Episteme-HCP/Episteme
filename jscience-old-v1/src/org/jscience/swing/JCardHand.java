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

package org.jscience.swing;

import org.jscience.mathematics.statistics.StatisticsUtils;

import java.awt.*;

import java.io.Serializable;

import javax.swing.*;


/**
 * This class is a container that holds cards.
 *
 * @author Kyle Siegrist
 * @author Dawn Duehring
 * @version August, 2003
 */
public class JCardHand extends JPanel implements Serializable {
    /** DOCUMENT ME! */
    private int cardCount;

    /** DOCUMENT ME! */
    private boolean faceUp;

    /** DOCUMENT ME! */
    private int[] deck = new int[52];

/**
     * This general constructor creates a new card hand with a specified number
     * of cards and a specified state (face up or down)
     *
     * @param n the number of cards.
     * @param b DOCUMENT ME!
     */
    public JCardHand(int n, boolean b) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createLineBorder(Color.black));
        setToolTipText("Card Hand");
        faceUp = b;

        for (int i = 0; i < 52; i++)
            deck[i] = i;

        setCardCount(n);
        validate();
    }

/**
     * This special constructor creates a new card hand with a specified number
     * of cards dealt face down.
     *
     * @param n DOCUMENT ME!
     */
    public JCardHand(int n) {
        this(n, false);
    }

/**
     * This default constructor creates a new card hand with 5 cards dealt face
     * down.
     */
    public JCardHand() {
        this(5);
    }

    /**
     * This method sets the number of cards. If the new card count is
     * greater than the old, an appropriate number of new cards are added. If
     * the new card count is smaller than the old, an appropriate number of
     * cards are removed. The card count is then updated.
     *
     * @param n the number of cars in the hand
     */
    public void setCardCount(int n) {
        int k;
        int u;
        int temp;
        JCard card;

        if (n < 1) {
            n = 1;
        }

        if (n > 52) {
            n = 52;
        }

        if (n > cardCount) {
            for (int i = cardCount; i < n; i++) {
                k = 52 - i;
                u = (int) (Math.random() * k);
                card = new JCard(deck[u], faceUp);
                temp = deck[k - 1];
                deck[k - 1] = deck[u];
                deck[u] = temp;
                card.setToolTipText("Card " + (i + 1));
                add(card);
            }

            cardCount = n;
        } else if (n < cardCount) {
            for (int i = n; i < cardCount; i++)
                remove(n);

            cardCount = n;
        }

        revalidate();
    }

    /**
     * Ths method gets the number of cards in the deck.
     *
     * @return the number of cards
     */
    public int getCardCount() {
        return cardCount;
    }

    /**
     * This method gets a specified card.
     *
     * @param i the card index.
     *
     * @return the card correpsonding to the index.
     */
    public JCard getCard(int i) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        return (JCard) getComponent(i);
    }

    /**
     * This method randomly deals a specified number of cards.
     */
    public void deal() {
        int[] p = new int[52];

        for (int i = 0; i < 52; i++)
            p[i] = i;

        setCardNumbers(StatisticsUtils.getSample(p, cardCount, 0));
    }

    /**
     * This method shows  the cards.
     *
     * @param b true to show the card fronts, false to show the card backs.
     */
    public void setFaceUp(boolean b) {
        faceUp = b;

        for (int i = 0; i < cardCount; i++)
            ((JCard) getComponent(i)).setFaceUp(b);

        repaint();
    }

    /**
     * This method returns the showing state of the deck.
     *
     * @return true if the card faces are showing, false if the card backs are
     *         showing
     */
    public boolean isFaceUp() {
        return faceUp;
    }

    /**
     * This method sets the array of values.
     *
     * @param v the array of values
     */
    public void setValues(int[] v) {
        if (v.length == cardCount) {
            for (int i = 0; i < cardCount; i++)
                getCard(i).setValue(v[i]);
        }
    }

    /**
     * This method returns the array of values.
     *
     * @return the array of values
     */
    public int[] getValues() {
        int[] v = new int[cardCount];

        for (int i = 0; i < cardCount; i++)
            v[i] = getCard(i).getValue();

        return v;
    }

    /**
     * This method sets the value of an individual coin.
     *
     * @param i the index
     * @param x the value of the coin
     */
    public void setValues(int i, int x) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        getCard(i).setValue(x);
    }

    /**
     * This method returns the value of an individual coin
     *
     * @param i the index
     *
     * @return the value of the coin with the index
     */
    public int getValues(int i) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        return getCard(i).getValue();
    }

    /**
     * This method sets the array of suits.
     *
     * @param s the array of suits
     */
    public void setSuits(int[] s) {
        if (s.length == cardCount) {
            for (int i = 0; i < cardCount; i++)
                getCard(i).setSuit(s[i]);
        }
    }

    /**
     * This method returns the array of suits.
     *
     * @return the array of suits
     */
    public int[] getSuits() {
        int[] s = new int[cardCount];

        for (int i = 0; i < cardCount; i++)
            s[i] = getCard(i).getSuit();

        return s;
    }

    /**
     * This method sets the suit of an individual card.
     *
     * @param i the index
     * @param x the suit of the card
     */
    public void setSuits(int i, int x) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        getCard(i).setSuit(x);
    }

    /**
     * This method returns the suit of an individual card
     *
     * @param i the index
     *
     * @return the suit of the card with the index
     */
    public int getSuits(int i) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        return getCard(i).getSuit();
    }

    /**
     * This method sets the array of card numbers.
     *
     * @param n the array of card numbers
     */
    public void setCardNumbers(int[] n) {
        if (n.length == cardCount) {
            for (int i = 0; i < cardCount; i++)
                getCard(i).setCardNumber(n[i]);
        }
    }

    /**
     * This method returns the array of card numbers.
     *
     * @return the array of card numbers
     */
    public int[] getCardNumbers() {
        int[] n = new int[cardCount];

        for (int i = 0; i < cardCount; i++)
            n[i] = getCard(i).getCardNumber();

        return n;
    }

    /**
     * This method sets the card number of an individual card.
     *
     * @param i the index
     * @param x the card number of the card
     */
    public void setCardNumbers(int i, int x) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        getCard(i).setCardNumber(x);
    }

    /**
     * This method returns the card number of an individual card
     *
     * @param i the index
     *
     * @return the card number of the card with the index
     */
    public int getCardNumbers(int i) {
        if (i < 0) {
            i = 0;
        } else if (i > (cardCount - 1)) {
            i = cardCount - 1;
        }

        return getCard(i).getCardNumber();
    }
}
