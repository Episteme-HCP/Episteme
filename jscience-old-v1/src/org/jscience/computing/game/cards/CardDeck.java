/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.computing.game.cards;

import java.io.Serializable;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;


/**
 * represents a stack of cards
 *
 * @author Holger Antelmann
 */
public class CardDeck implements Cloneable, Serializable {
    /**
     * DOCUMENT ME!
     */
    static final long serialVersionUID = -766409150440034465L;

    /**
     * DOCUMENT ME!
     */
    Stack<Card> cards;

    /**
     * Creates a new CardDeck object.
     *
     * @param numberOfDecks DOCUMENT ME!
     * @param bigDeck DOCUMENT ME!
     * @param shuffled DOCUMENT ME!
     */
    public CardDeck(int numberOfDecks, boolean bigDeck, boolean shuffled) {
        cards = new Stack<Card>();

        for (int i = 0; i < numberOfDecks; i++) {
            if (bigDeck) {
                addBigDeck();
            } else {
                addSmallDeck();
            }
        }

        if (shuffled) {
            shuffle(new Random(System.currentTimeMillis()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Card takeCard() {
        return (Card) cards.pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int cardsLeft() {
        return cards.size();
    }

    /**
     * DOCUMENT ME!
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rnd DOCUMENT ME!
     */
    public void shuffle(Random rnd) {
        Collections.shuffle(cards, rnd);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void addCard(Card c) {
        cards.push(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param deck DOCUMENT ME!
     */
    public void addDeck(CardDeck deck) {
        cards.addAll(deck.cards);
    }

    /**
     * DOCUMENT ME!
     */
    private void addBigDeck() {
        addSmallDeck();
        cards.add(new Card(Card.SIX, Card.CLUBS));
        cards.add(new Card(Card.FIVE, Card.CLUBS));
        cards.add(new Card(Card.FOUR, Card.CLUBS));
        cards.add(new Card(Card.THREE, Card.CLUBS));
        cards.add(new Card(Card.TWO, Card.CLUBS));
        cards.add(new Card(Card.SIX, Card.SPADES));
        cards.add(new Card(Card.FIVE, Card.SPADES));
        cards.add(new Card(Card.FOUR, Card.SPADES));
        cards.add(new Card(Card.THREE, Card.SPADES));
        cards.add(new Card(Card.TWO, Card.SPADES));
        cards.add(new Card(Card.SIX, Card.HEARTS));
        cards.add(new Card(Card.FIVE, Card.HEARTS));
        cards.add(new Card(Card.FOUR, Card.HEARTS));
        cards.add(new Card(Card.THREE, Card.HEARTS));
        cards.add(new Card(Card.TWO, Card.HEARTS));
        cards.add(new Card(Card.SIX, Card.DIAMONDS));
        cards.add(new Card(Card.FIVE, Card.DIAMONDS));
        cards.add(new Card(Card.FOUR, Card.DIAMONDS));
        cards.add(new Card(Card.THREE, Card.DIAMONDS));
        cards.add(new Card(Card.TWO, Card.DIAMONDS));
    }

    /**
     * DOCUMENT ME!
     */
    private void addSmallDeck() {
        cards.add(new Card(Card.ACE, Card.CLUBS));
        cards.add(new Card(Card.KING, Card.CLUBS));
        cards.add(new Card(Card.QUEEN, Card.CLUBS));
        cards.add(new Card(Card.JACK, Card.CLUBS));
        cards.add(new Card(Card.TEN, Card.CLUBS));
        cards.add(new Card(Card.NINE, Card.CLUBS));
        cards.add(new Card(Card.EIGHT, Card.CLUBS));
        cards.add(new Card(Card.SEVEN, Card.CLUBS));
        cards.add(new Card(Card.ACE, Card.SPADES));
        cards.add(new Card(Card.KING, Card.SPADES));
        cards.add(new Card(Card.QUEEN, Card.SPADES));
        cards.add(new Card(Card.JACK, Card.SPADES));
        cards.add(new Card(Card.TEN, Card.SPADES));
        cards.add(new Card(Card.NINE, Card.SPADES));
        cards.add(new Card(Card.EIGHT, Card.SPADES));
        cards.add(new Card(Card.SEVEN, Card.SPADES));
        cards.add(new Card(Card.ACE, Card.HEARTS));
        cards.add(new Card(Card.KING, Card.HEARTS));
        cards.add(new Card(Card.QUEEN, Card.HEARTS));
        cards.add(new Card(Card.JACK, Card.HEARTS));
        cards.add(new Card(Card.TEN, Card.HEARTS));
        cards.add(new Card(Card.NINE, Card.HEARTS));
        cards.add(new Card(Card.EIGHT, Card.HEARTS));
        cards.add(new Card(Card.SEVEN, Card.HEARTS));
        cards.add(new Card(Card.ACE, Card.DIAMONDS));
        cards.add(new Card(Card.KING, Card.DIAMONDS));
        cards.add(new Card(Card.QUEEN, Card.DIAMONDS));
        cards.add(new Card(Card.JACK, Card.DIAMONDS));
        cards.add(new Card(Card.TEN, Card.DIAMONDS));
        cards.add(new Card(Card.NINE, Card.DIAMONDS));
        cards.add(new Card(Card.EIGHT, Card.DIAMONDS));
        cards.add(new Card(Card.SEVEN, Card.DIAMONDS));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        CardDeck newDeck = (CardDeck) super.clone();
        newDeck.cards = new Stack<Card>();

        Iterator i = cards.iterator();

        while (i.hasNext()) {
            newDeck.cards.push((Card) i.next());
        }

        return newDeck;
    }
}
