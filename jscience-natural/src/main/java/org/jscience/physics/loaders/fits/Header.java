/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.physics.loaders.fits;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class Header {

    private final List<HeaderCard> wCards = new ArrayList<>();
    // Map for fast lookup by keyword
    private final Map<String, HeaderCard> cardMap = new LinkedHashMap<>();

    public Header() {
    }

    public void addCard(HeaderCard card) {
        wCards.add(card);
        cardMap.put(card.getKeyword(), card);
    }

    public HeaderCard getCard(String keyword) {
        return cardMap.get(keyword);
    }

    public int getIntValue(String keyword, int defaultValue) {
        HeaderCard c = getCard(keyword);
        return (c != null) ? c.getValueAsInt(defaultValue) : defaultValue;
    }

    public long getLongValue(String keyword, long defaultValue) {
        HeaderCard c = getCard(keyword);
        return (c != null) ? c.getValueAsLong(defaultValue) : defaultValue;
    }

    public double getDoubleValue(String keyword, double defaultValue) {
        HeaderCard c = getCard(keyword);
        return (c != null) ? c.getValueAsDouble(defaultValue) : defaultValue;
    }

    public String getStringValue(String keyword) {
        HeaderCard c = getCard(keyword);
        return (c != null) ? c.getValueAsString() : null;
    }

    public List<HeaderCard> getCards() {
        return Collections.unmodifiableList(wCards);
    }

    /**
     * Reads a logical header block from the channel.
     * FITS headers are padded to 2880 bytes.
     * 
     * @param channel input channel
     * @return the parsed Header
     * @throws IOException
     */
    public static Header read(ReadableByteChannel channel) throws IOException {
        Header header = new Header();
        ByteBuffer buffer = ByteBuffer.allocate(FitsConstants.BLOCK_SIZE);
        boolean endFound = false;

        while (!endFound) {
            buffer.clear();
            while (buffer.hasRemaining()) {
                int r = channel.read(buffer);
                if (r < 0)
                    throw new IOException("Unexpected EOF in FITS header");
            }
            buffer.flip();

            // Parse 80-byte cards
            while (buffer.hasRemaining()) {
                byte[] cardBytes = new byte[80];
                buffer.get(cardBytes);
                String raw = new String(cardBytes, "US-ASCII"); // FITS is strictly ASCII

                HeaderCard card = new HeaderCard(raw);
                header.addCard(card);

                if (FitsConstants.KEY_END.equals(card.getKeyword())) {
                    endFound = true;
                    // Note: we continue consuming the rest of the block (it should be padded
                    // spaces)
                    // but we don't need to parse real cards anymore
                    break;
                }
            }
        }
        return header;
    }
}
