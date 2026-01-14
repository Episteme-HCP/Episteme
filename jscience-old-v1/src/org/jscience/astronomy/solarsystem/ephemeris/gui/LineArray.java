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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import java.io.DataInputStream;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class LineArray {
    /** DOCUMENT ME! */
    private DataInputStream instream;

    /** DOCUMENT ME! */
    private int count;

    /** DOCUMENT ME! */
    private ShortLine[] lines;

    /** DOCUMENT ME! */
    private boolean loaded;

    /** DOCUMENT ME! */
    private boolean suicide;

/**
     * Creates a new LineArray object.
     */
    LineArray() {
        loaded = false;
        suicide = false;
    }

/**
     * Creates a new LineArray object.
     *
     * @param datainputstream DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    LineArray(DataInputStream datainputstream) throws IOException {
        loaded = false;
        suicide = false;
        count = datainputstream.readInt() / 2;
        System.out.println("LineArray gets a count of: " + count);
        lines = new ShortLine[count];

        for (int i = 0; i < count; i++) {
            lines[i] = new ShortLine();

            short word0 = datainputstream.readShort();
            short word1 = datainputstream.readShort();
            short word2 = datainputstream.readShort();
            lines[i].start = new ShortPoint((short) (-word2), word0, word1);
            word0 = datainputstream.readShort();
            word1 = datainputstream.readShort();
            word2 = datainputstream.readShort();
            lines[i].end = new ShortPoint((short) (-word2), word0, word1);

            if (suicide) {
                break;
            }
        }

        loaded = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void kill() {
        suicide = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ShortLine getLine(int i) {
        return lines[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     */
    private void error(String s) {
        System.out.println(s);
        System.exit(100);
    }
}
