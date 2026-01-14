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

package org.jscience.util.logging;

import org.jscience.util.Filter;


/**
 * filters LogEntry objects by their level using the natural order of Level
 * objects
 *
 * @author Holger Antelmann
 *
 * @see Level
 */
public class LevelFilter implements Filter<LogEntry> {
    /** accepts LogEntry objects that exclude the levels FINE, FINER, FINEST. */
    public static final Filter<LogEntry> NO_FINE_LEVEL_FILTER = new Filter<LogEntry>() {
            public boolean accept(LogEntry entry) {
                //if (entry.getLevel() == null) return true;
                if (entry.getLevel().equals(Level.FINE)) {
                    return false;
                }

                if (entry.getLevel().equals(Level.FINER)) {
                    return false;
                }

                if (entry.getLevel().equals(Level.FINEST)) {
                    return false;
                }

                return true;
            }
        };

    /** accepts LogEntry objects that compare to more than the FINE level. */
    public static final Filter<LogEntry> MORE_THAN_FINE_LEVEL_FILTER = new Filter<LogEntry>() {
            public boolean accept(LogEntry entry) {
                return (entry.getLevel().compareTo(Level.FINE) > 0);
            }
        };

    /** DOCUMENT ME! */
    Level minLevel;

/**
     * Creates a new LevelFilter object.
     *
     * @param minLevel DOCUMENT ME!
     */
    public LevelFilter(Level minLevel) {
        if (minLevel == null) {
            throw new NullPointerException("level must not be null");
        }

        this.minLevel = minLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Level getMinLevel() {
        return minLevel;
    }

    /**
     * returns true only if the Level of the entry is equal or comes
     * after the configured Level of this filter.
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(LogEntry entry) {
        return (minLevel.compareTo(entry.getLevel()) <= 0);
    }
}
