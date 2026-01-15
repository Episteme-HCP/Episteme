/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.cards;

import java.io.Serializable;

import java.util.Iterator;
import java.util.Vector;


/**
 * BJHand contains the information for a particular hand for a player in
 * BlackJack. If a player has gone through split moves, multiple BJHands may
 * belong to a player in a particular game. This class is public because it
 * may have to be used by a Player outside this package.
 *
 * @author Holger Antelmann
 */
class BJHand implements Cloneable, Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = 3178215053034716097L;

    /**
     * DOCUMENT ME!
     */
    public Vector<Card> cards = null; // Vector of Card

    /**
     * DOCUMENT ME!
     */
    public int playerRole = -1;

    /**
     * DOCUMENT ME!
     */
    public boolean gameOver = false;

    /**
     * DOCUMENT ME!
     */
    public boolean surrender = false;

    /**
     * DOCUMENT ME!
     */
    public boolean insurance = false;

    /**
     * DOCUMENT ME!
     */
    public boolean decline = false;

    /**
     * DOCUMENT ME!
     */
    public boolean split = false;

    /**
     * DOCUMENT ME!
     */
    public boolean aceSplit = false;

    /**
     * DOCUMENT ME!
     */
    public boolean even = false;

    /**
     * DOCUMENT ME!
     */
    public float bet = 0;

    /**
     * Creates a new BJHand object.
     */
    BJHand() {
        cards = new Vector<Card>(2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    protected Object clone() throws CloneNotSupportedException {
        BJHand newUnit = (BJHand) super.clone();
        newUnit.cards = new Vector<Card>(cards.size());

        Iterator i = cards.iterator();

        while (i.hasNext())
            newUnit.cards.add((Card) i.next());

        return newUnit;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = "Cards(" + playerRole + "): ";
        Iterator i = cards.iterator();

        if (i.hasNext()) {
            s += i.next();
        }

        while (i.hasNext())
            s += (", " + i.next());

        s += ("; player: " + playerRole);
        s += ((gameOver) ? "; (done)" : "; (active)");

        return s;
    }
}
