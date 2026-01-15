/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.cards;

import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GamePlay;

import java.util.Vector;


/**
 * a BlackJack Player object that cheats during evaluation by looking at
 * the next card that's on the deck
 *
 * @author Holger Antelmann
 */
public class BJCheater extends BJPlayer {
    /** DOCUMENT ME! */
    static final long serialVersionUID = -2163426428183451238L;

/**
     * Creates a new BJCheater object.
     */
    public BJCheater() {
        super("BlackJack Cheater");
        setSearchOption(0);
        setTracking(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param game DOCUMENT ME!
     * @param move DOCUMENT ME!
     * @param role DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public double heuristic(GamePlay game, GameMove move, int[] role) {
        BlackJack g = null;

        try {
            g = (BlackJack) game.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error();
        }

        Vector<Card> cards = g.getCurrentHand(role[0]).cards;

        int ovalue = BlackJack.getHandValue(cards);
        Card newcard = g.deck.takeCard();
        cards.add(newcard);

        int nvalue = BlackJack.getHandValue(cards);

        switch (((BJMove) move).getType()) {
        case BJMove.SURRENDER:

            if ((nvalue > 21) && (ovalue < 15)) {
                return 8;
            }

            return -10;

        case BJMove.SPLIT:

            if (ovalue == 21) {
                return -5;
            }

            if (ovalue == 20) {
                return -5;
            }

            if (ovalue < 14) {
                return 5;
            }

            return 0;

        case BJMove.DOUBLE:

            if (nvalue == 21) {
                return 10;
            }

            if (nvalue == 20) {
                return 9;
            }

            return -1;

        case BJMove.STAY:

            if (nvalue > 21) {
                return 7;
            }

            return -9;

        case BJMove.HIT:

            if (nvalue <= 21) {
                return 8;
            }

            return -1;

        case BJMove.ACCEPT_INSURANCE:

            if (BlackJack.getCardValue(newcard, false) == 10) {
                return 2;
            }

            return -2;

        case BJMove.DECLINE_INSURANCE:

            if (BlackJack.getCardValue(newcard, false) != 10) {
                return 2;
            }

            return -2;

        case BJMove.EVEN:
            return -3;

        default:
            throw new Error();
        }
    }
}
