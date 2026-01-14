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

import org.jscience.computing.game.AbstractGame;
import org.jscience.computing.game.GameMove;
import org.jscience.computing.game.GameRuntimeException;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


/**
 * BlackJack implements the rules for playing BlackJack. The casino rules
 * of the game are embedded in this class with the implementations of
 * listLegalMoves(), dealerMove() and getResult(int playerRole). Todo for a
 * future version: strip out casino rules and make them available as options
 * that can be changed through public methods.
 *
 * @author Holger Antelmann
 */
public class BlackJack extends AbstractGame {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -8372899817004544134L;

    /**
     * DOCUMENT ME!
     */
    public static int NUMBER_OF_DECKS = 6;

    /**
     * DOCUMENT ME!
     */
    public static int MINIMUM_CARDS = 10;

    /**
     * DOCUMENT ME!
     */
    public static int BURN_CARDS = 10;

    /**
     * DOCUMENT ME!
     */
    Random random;

    /**
     * DOCUMENT ME!
     */
    CardDeck deck;

    /**
     * DOCUMENT ME!
     */
    Vector<BJHand> hands; // Vector of BJHand

    /**
     * DOCUMENT ME!
     */
    Vector<Card> dealerHand; // Vector of Card

    /**
     * DOCUMENT ME!
     */
    Card openDealerCard;

    /**
     * Creates a new BlackJack object.
     */
    public BlackJack() {
        this(10);
    }

    /**
     * Creates a new BlackJack object.
     *
     * @param bet DOCUMENT ME!
     */
    public BlackJack(float bet) {
        this(new float[] { bet });
    }

    /**
     * Creates a new BlackJack object.
     *
     * @param bet DOCUMENT ME!
     */
    public BlackJack(float[] bet) {
        this(bet, new Random(System.currentTimeMillis()));
    }

    /**
     * Creates a new BlackJack object.
     *
     * @param bet DOCUMENT ME!
     * @param random DOCUMENT ME!
     */
    public BlackJack(float[] bet, Random random) {
        this(newCardDeck(random), bet);
    }

    /**
     * Creates a new BlackJack object.
     *
     * @param deck DOCUMENT ME!
     * @param bet DOCUMENT ME!
     */
    public BlackJack(CardDeck deck, float[] bet) {
        this(deck, bet, new Random(System.currentTimeMillis()));
    }

    /**
     * Creates a new BlackJack object.
     *
     * @param deck DOCUMENT ME!
     * @param bet DOCUMENT ME!
     * @param random DOCUMENT ME!
     */
    public BlackJack(CardDeck deck, float[] bet, Random random) {
        super("BlackJack", bet.length);
        this.random = random;
        this.deck = deck;
        hands = new Vector<BJHand>(bet.length);

        BJHand pi;

        for (int i = 0; i < bet.length; i++) {
            pi = new BJHand();
            pi.bet = bet[i];
            pi.playerRole = i;
            pi.cards.add(getCard());
            pi.cards.add(getCard());

            if (isBlackJack(pi.cards)) {
                pi.gameOver = true;
            }

            hands.add(pi);
        }

        openDealerCard = getCard();
        dealerHand = new Vector<Card>(2);
        dealerHand.add(openDealerCard);
        dealerHand.add(getCard());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Card getOpenDealerCard() {
        return openDealerCard;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector<Card> getDealerCards() {
        if (!gameOver()) {
            throw (new UnsupportedOperationException("you are cheating"));
        }

        return dealerHand;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CardDeck getDeck() {
        if (!gameOver()) {
            throw (new UnsupportedOperationException("you are cheating"));
        }

        return deck;
    }

    /**
     * getCurrentHand() is internally a wrapper which returns a clone
     * of the actual BJHand used in the game
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BJHand getCurrentHand(int playerRole) {
        BJHand u = getActiveUnit(playerRole);

        if (u == null) {
            return null;
        }

        try {
            return (BJHand) u.clone();
        } catch (CloneNotSupportedException e) {
            String s = "*** A CloneNotSupportedException was thrown";
            s += "by BJHand in org.jscience.computing.game.cards.BlackJack.getCurrentHand()";
            System.err.println(s);

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cards DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getHandValue(Collection cards) {
        int v = 0;
        int aces = 0;
        Iterator i = cards.iterator();
        Card c = null;

        while (i.hasNext()) {
            c = (Card) i.next();

            if (c.getRank() == Card.ACE) {
                aces++;
            }

            v = v + getCardValue(c, true);
        }

        while ((v > 21) && (aces > 0)) {
            v = v - 10;
            aces--;
        }

        return v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cards DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isSoft(Collection cards) {
        Iterator i = cards.iterator();

        while (i.hasNext()) {
            if (((Card) i.next()).getRank() == Card.ACE) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param card DOCUMENT ME!
     * @param softCount DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getCardValue(Card card, boolean softCount) {
        switch (card.getRank()) {
        case Card.ACE:
            return ((softCount) ? 11 : 1);

        case Card.KING:
        case Card.QUEEN:
        case Card.JACK:
        case Card.TEN:
            return 10;

        case Card.NINE:
            return 9;

        case Card.EIGHT:
            return 8;

        case Card.SEVEN:
            return 7;

        case Card.SIX:
            return 6;

        case Card.FIVE:
            return 5;

        case Card.FOUR:
            return 4;

        case Card.THREE:
            return 3;

        case Card.TWO:
            return 2;

        default:
            throw new RuntimeException("invalid card in the deck: " +
                card.toString());
        }
    }

    /**
     * Note that a Vector of cards may not be considered BlackJack even
     * though this function returns true, which is the case if the particular
     * hand is derived from an Ace split.
     *
     * @param cards DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isBlackJack(Collection cards) {
        if (cards.size() != 2) {
            return false;
        }

        Iterator i = cards.iterator();
        Card c = (Card) i.next();

        if (c.getRank() == Card.ACE) {
            switch (((Card) i.next()).getRank()) {
            case Card.KING:
            case Card.QUEEN:
            case Card.JACK:
            case Card.TEN:
                return true;

            default:
                return false;
            }
        } else {
            switch (c.getRank()) {
            case Card.KING:
            case Card.QUEEN:
            case Card.JACK:
            case Card.TEN:

                if (((Card) i.next()).getRank() == Card.ACE) {
                    return true;
                } else {
                    return false;
                }

            default:
                return false;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void dealerMove() {
        //while (getHandValue(dealerHand) < 17) dealerHand.add(getCard());
        while ((getHandValue(dealerHand) < 17) ||
                ((getHandValue(dealerHand) == 17) && (isSoft(dealerHand))))
            dealerHand.add(getCard());
    }

    /**
     * DOCUMENT ME!
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private BJHand getActiveUnit(int playerRole) {
        if ((playerRole < 0) || (playerRole >= numberOfPlayers())) {
            throw (new GameRuntimeException(this, "player role not existing"));
        }

        BJHand u;
        Iterator i = hands.iterator();

        while (i.hasNext()) {
            u = (BJHand) i.next();

            if ((u.playerRole == playerRole) && (!u.gameOver)) {
                return u;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private Card getCard() {
        if (deck.cardsLeft() < MINIMUM_CARDS) {
            deck = newCardDeck(random);
        }

        return deck.takeCard();
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static CardDeck newCardDeck(Random r) {
        CardDeck d = new CardDeck(NUMBER_OF_DECKS, true, false);
        d.shuffle(r);

        // 'burn' a few cards to avoid complete counting
        for (int i = 1; i < BURN_CARDS; i++)
            d.takeCard();

        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean gameOver() {
        if (nextPlayer() == numberOfPlayers()) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getWinner() {
        if (!gameOver()) {
            return null;
        }

        Vector<Integer> winner = new Vector<Integer>(numberOfPlayers());

        for (int i = 0; i < numberOfPlayers(); i++) {
            if (getResult(i) > 0) {
                winner.add(new Integer(i));
            }
        }

        int[] win = new int[winner.size()];

        for (int i = 0; i < win.length; i++) {
            win[i] = ((Integer) winner.elementAt(i)).intValue();
        }

        return win;
    }

    /**
     * DOCUMENT ME!
     *
     * @param playerRole DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double getResult(int playerRole) {
        if (!gameOver()) {
            throw (new GameRuntimeException(this, "game is not over, yet"));
        }

        if ((playerRole < 0) || (playerRole >= numberOfPlayers())) {
            throw (new GameRuntimeException(this,
                "role doesn't exist in this game"));
        }

        float result = 0;
        int dealerValue = getHandValue(dealerHand);
        boolean dealerBJ = isBlackJack(dealerHand);
        Iterator i = hands.iterator();
        BJHand u;
loop: 
        while (i.hasNext()) {
            u = (BJHand) i.next();

            if (u.playerRole == playerRole) {
                int v = getHandValue(u.cards);
                boolean bj = ((v == 21) && !u.aceSplit && isBlackJack(u.cards));

                if (u.insurance) {
                    if (dealerBJ) {
                        result = result + u.bet;
                    } else {
                        result = result - (u.bet / (float) 2);
                    }
                }

                if (dealerBJ) {
                    if (!bj) {
                        result = result - u.bet;
                    }

                    continue loop;
                }

                if (u.surrender) {
                    result = result - (u.bet / (float) 2);

                    continue loop;
                }

                if (bj) {
                    result = result + (u.bet * (float) 1.5);

                    continue loop;
                }

                if (v > 21) {
                    result = result - u.bet;

                    continue loop;
                }

                if (v == dealerValue) {
                    continue loop;
                }

                if ((v > dealerValue) || (dealerValue > 21)) {
                    result = result + u.bet;
                } else {
                    result = result - u.bet;
                }
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int nextPlayer() {
        BJHand u;
        Iterator i = hands.iterator();

        while (i.hasNext()) {
            u = (BJHand) i.next();

            if (!u.gameOver) {
                return u.playerRole;
            }
        }

        return numberOfPlayers();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected GameMove[] listLegalMoves() {
        int next = nextPlayer();

        if (next == numberOfPlayers()) {
            return null;
        }

        Vector<GameMove> moves = new Vector<GameMove>(4);
        BJHand u = getActiveUnit(next);

        if (u.cards.size() == 2) {
            if (!u.insurance && !u.decline &&
                    (openDealerCard.getRank() == Card.ACE)) {
                return new BJMove[] {
                    new BJMove(next, BJMove.ACCEPT_INSURANCE),
                    new BJMove(next, BJMove.DECLINE_INSURANCE)
                };
            }

            if (!isBlackJack(dealerHand)) {
                moves.add(new BJMove(next, BJMove.SURRENDER));
            }

            moves.add(new BJMove(next, BJMove.DOUBLE));

            if ((openDealerCard.getRank() == Card.ACE) && (!u.even) &&
                    (!u.aceSplit) && (isBlackJack(u.cards))) {
                moves.add(new BJMove(next, BJMove.EVEN));
            }

            Card a = (Card) u.cards.elementAt(0);
            Card b = (Card) u.cards.elementAt(1);

            if (a.getRank() == b.getRank()) {
                moves.add(new BJMove(next, BJMove.SPLIT));
            }
        }

        int v = getHandValue(u.cards);

        if ((v < 21) && ((!u.aceSplit) || (u.cards.size() == 2))) {
            moves.add(new BJMove(next, BJMove.HIT));
        }

        if (v < 22) {
            moves.add(new BJMove(next, BJMove.STAY));
        }

        return (GameMove[]) moves.toArray(new GameMove[moves.size()]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param move DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean pushMove(GameMove move) {
        BJHand u = getActiveUnit(move.getPlayer());

        switch (((BJMove) move).getType()) {
        case BJMove.ACCEPT_INSURANCE:
            u.insurance = true;

            if (isBlackJack(dealerHand)) {
                u.gameOver = true;
            }

            break;

        case BJMove.DECLINE_INSURANCE:
            u.decline = true;

            if (isBlackJack(dealerHand)) {
                u.gameOver = true;
            }

            break;

        case BJMove.SURRENDER:
            u.surrender = true;
            u.gameOver = true;

            break;

        case BJMove.DOUBLE:
            u.bet = u.bet * 2;
            u.cards.add(getCard());
            u.gameOver = true;

            break;

        case BJMove.STAY:
            u.gameOver = true;

            break;

        case BJMove.HIT:
            u.cards.add(getCard());

            if (getHandValue(u.cards) >= 21) {
                u.gameOver = true;
            }

            break;

        case BJMove.EVEN:
            u.even = true;
            u.gameOver = true;

            break;

        case BJMove.SPLIT:

            Card c = (Card) u.cards.elementAt(0);
            u.cards.remove(c);
            u.cards.add(getCard());

            if (c.getRank() == Card.ACE) {
                u.aceSplit = true;
            }

            BJHand split = new BJHand();
            split.cards.add(c);
            split.cards.add(getCard());
            split.playerRole = u.playerRole;
            split.bet = u.bet;
            split.split = true;

            if (c.getRank() == Card.ACE) {
                split.aceSplit = true;
            }

            hands.add(split);

            break;

        default: // nothing

        }

        if (gameOver()) {
            dealerMove();
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean popMove() {
        return false;
    }

    /**
     * Note: the cloned object will have a separate Random object
     * assiciated with it, so that operations on the cloned object will not
     * affect the random numbers of the original.
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        BlackJack newGame = (BlackJack) super.clone();
        newGame.deck = (CardDeck) deck.clone();

        Iterator i;
        newGame.hands = new Vector<BJHand>(hands.size());
        i = hands.iterator();

        while (i.hasNext())
            newGame.hands.add((BJHand) ((BJHand) i.next()).clone());

        newGame.dealerHand = new Vector<Card>(dealerHand.size());
        i = dealerHand.iterator();

        while (i.hasNext())
            newGame.dealerHand.add((Card) i.next());

        newGame.random = new Random();

        return newGame;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String s = null;

        if (!gameOver()) {
            s = "open dealer card: " + openDealerCard;
        } else {
            s = "dealer cards: ";

            Iterator i = dealerHand.iterator();

            if (i.hasNext()) {
                s += i.next();
            }

            while (i.hasNext())
                s += (", " + i.next());
        }

        Iterator i = hands.iterator();

        while (i.hasNext())
            s += ("\n" + i.next());

        return s;
    }
}
