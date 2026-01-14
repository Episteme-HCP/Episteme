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

package org.jscience.physics.loaders.fits;

/**
 * Represents a single card in a FITS header.
 * <p>
 * A card consists of a keyword (8 chars), a value indicator ("= "), a value,
 * and an optional comment.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class HeaderCard {

    private final String keyword;
    private final String value;
    private final String comment;
    private final String raw;

    public HeaderCard(String raw) {
        if (raw.length() < 80) {
            // Pad to 80 if short (though standard says fixed 80)
            raw = String.format("%-80s", raw);
        }
        this.raw = raw;
        this.keyword = raw.substring(0, 8).trim();

        // Basic parsing logic (simplified for now)
        if (raw.length() > 10 && raw.charAt(8) == '=' && raw.charAt(9) == ' ') {
            // Value present
            int slashIndex = raw.indexOf('/', 10);
            if (slashIndex > 0) {
                this.value = raw.substring(10, slashIndex).trim();
                this.comment = raw.substring(slashIndex + 1).trim();
            } else {
                this.value = raw.substring(10).trim();
                this.comment = "";
            }
        } else {
            this.value = "";
            this.comment = raw.substring(8).trim();
        }
    }

    public String getKeyword() {
        return keyword;
    }

    public String getValue() {
        return value;
    }

    public String getComment() {
        return comment;
    }

    public int getValueAsInt(int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public long getValueAsLong(long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public double getValueAsDouble(double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public String getValueAsString() {
        // Remove quotes if string
        if (value.startsWith("'") && value.endsWith("'")) {
            return value.substring(1, value.length() - 1).trim();
        }
        return value;
    }

    @Override
    public String toString() {
        return raw;
    }
}


