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

package org.jscience.io.fits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Represents the header of a FITS HDU. The header consists of list of
 * keyword-value pairs which specify the metadata for an HDU. Note that
 * changing the required keywords in a header does not change the
 * corresponding data object (if there is one).
 */
public class FitsHeader {
    /** DOCUMENT ME! */
    private List cards;

    /** DOCUMENT ME! */
    private Map index;

    /** DOCUMENT ME! */
    private boolean isComplete;

/**
     * Create a new empty header.
     */
    public FitsHeader() {
        cards = new ArrayList();
        index = new HashMap();
        isComplete = false;
    } // end of constructor

    /**
     * returns true if this header has been completely read or
     * constructed. In general a header is complete when it contains an END
     * keyword.
     *
     * @return DOCUMENT ME!
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * returns the number of blocks (including partial blocks) occupied
     * by the header
     *
     * @return DOCUMENT ME!
     */
    public int blockCount() {
        return ((cards.size() + FitsCard.CARDS_PER_BLOCK) - 1) / FitsCard.CARDS_PER_BLOCK;
    }

    /**
     * add a card to the header. The caller is responsible for ensuring
     * that added cards conform to the FITS standard. If you add an END card,
     * the header will be marked as complete. If the header is incomplete,
     * then this method will append the card to the existing ones. If the
     * header is complete, this method will add the card just before the END
     * card.
     *
     * @param card the new card to be added.
     */
    public void add(FitsCard card) {
        if (isComplete) {
/**
             * add before the END card
             */
            cards.add(cards.size() - 1, card);
        } else {
/**
             * no end card yet
             */
            cards.add(card);
        }

        index.put(card.key(), card);

        if (card.key().equals("END")) {
            isComplete = true;
        }
    } // end of add card method

    /**
     * add a block of 2880 bytes to the header. This method is useful
     * for reading a header from a data source. The general user will probably
     * not need this method, since it is used internally by the {@link
     * FitsFile} subclasses. If the array is larger than 2880 bytes, only the
     * first 2880 bytes will be read.
     *
     * @param block an array of 2880 bytes to add to this header.
     *
     * @return true if this is the last block of the header
     */
    public boolean add(byte[] block) {
        int offset = 0;

        for (int i = 0; (i < FitsCard.CARDS_PER_BLOCK) && !isComplete(); ++i) {
            add(new FitsCard(block, i * FitsCard.LENGTH));
        }

        return isComplete();
    } // end of add data method

    /**
     * returns true if the header has at least one card with the given
     * keyword
     *
     * @param key the name of the keyword to search for
     *
     * @return DOCUMENT ME!
     */
    public boolean hasCard(String key) {
        return index.containsKey(key);
    }

    /**
     * returns a card with the given keyword. If there are multiple
     * cards with the same key, this will return the last one added to this
     * header
     *
     * @param key the keyword to search for.
     *
     * @return a card with the given keyword
     *
     * @throws NoSuchFitsCardException if the header does not contain the
     *         requested keyword.
     */
    public FitsCard card(String key) throws NoSuchFitsCardException {
        FitsCard card = (FitsCard) index.get(key);

        if (card == null) {
            throw new NoSuchFitsCardException("No key " + key + " in " + this);
        }

        return card;
    } // end of card by key method

    /**
     * returns the specified card .
     *
     * @param i the index of the card. The first card is numbered 0.
     *
     * @return the ith card in the header.
     *
     * @throws NoSuchFitsCardException if the index is out of bounds.
     */
    public FitsCard card(int i) throws NoSuchFitsCardException {
        try {
            FitsCard card = (FitsCard) cards.get(i);

            return card;
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchFitsCardException("No card " + i + " in " + this);
        }
    } // end of FitsCard by index number

    /**
     * returns the number of cards in the header. This does not include
     * any blank cards used as padding after the END card.
     *
     * @return DOCUMENT ME!
     */
    public int cardCount() {
        return cards.size();
    }

    /**
     * returns the number of bytes in the data which would correspond
     * to this header. Note this is the actual number of bytes not including
     * padding.
     *
     * @return the data size in bytes excluding padding
     *
     * @throws FitsCardException DOCUMENT ME!
     */
    public int dataSize() throws FitsCardException {
        int naxis = card("NAXIS").intValue();

        int size;

        if (naxis == 0) {
            size = 0;
        } else {
            size = Math.abs(card("BITPIX").intValue()) / 8;
        }

        for (int i = 1; i <= naxis; ++i) {
            size *= card("NAXIS" + i).intValue();
        }

/**
         * now add a possible variable array heap
         */
        try {
            size += card("PCOUNT").intValue();
        } catch (NoSuchFitsCardException e) {
        }

        return size;
    } // end of DataSize method

    /**
     * returns the name of this extension. For most HDUs this will be
     * the value of the EXTNAME keyword. For a primary HDU, it will return
     * "PRIMARY". If there is no EXTNAME keyword, it will return null.
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        try {
            if (card(0).key().equals("SIMPLE")) {
                return "PRIMARY";
            }

            return card("EXTNAME").stringValue();
        } catch (FitsCardException e) {
            return "";
        }
    } // end of name method

    /**
     * returns the type of data accompanying this header. For most HDUs
     * this is the value of the XTENSION keyword. For the primary HDU this is
     * the string "IMAGE".
     *
     * @return DOCUMENT ME!
     *
     * @throws FitsException if there is no SIMPLE or XTENSION keyword in this
     *         header.
     */
    public String getType() throws FitsException {
        try {
            return card("XTENSION").stringValue();
        } catch (NoSuchFitsCardException e) {
/**
             * no XTENSION keyword, so it should be a primary HDU
             */
            if (hasCard("SIMPLE")) {
                return "IMAGE";
            } else {
                throw new FitsException("No SIMPLE or XTENSION keyword in " +
                    this);
            }
        }
    } // end of getType method
} // end of FitsHeader class
