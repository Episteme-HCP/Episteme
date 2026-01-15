/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import java.io.IOException;
import java.io.InputStream;


/**
 * This specialized stream replaces all occurences of a given byte pattern
 * with another throughout the stream. Also, this class can easily be
 * overwritten to e.g. only replace only the first or every second occurrence
 * or the alike.
 *
 * @author Holger Antelmann
 */
public class ReplaceInputStream extends InterceptInputStream implements InterceptInputStream.Handler {
    /** DOCUMENT ME! */
    byte[] replacePattern;

    /** DOCUMENT ME! */
    byte[] buffer;

    /** DOCUMENT ME! */
    byte[] buffer2;

    /** DOCUMENT ME! */
    int cursor;

    /** DOCUMENT ME! */
    int length;

    /** DOCUMENT ME! */
    int counter = 0;

/**
     * Creates a new ReplaceInputStream object.
     *
     * @param in             DOCUMENT ME!
     * @param searchPattern  DOCUMENT ME!
     * @param replacePattern DOCUMENT ME!
     */
    public ReplaceInputStream(InputStream in, byte[] searchPattern,
        byte[] replacePattern) {
        super(in, searchPattern);

        if (replacePattern == null) {
            throw new IllegalArgumentException(
                "replacePattern may be empty but not null");
        }

        this.replacePattern = replacePattern;
        addHandler(this);
    }

    /**
     * returns the number of replacements so far
     *
     * @return DOCUMENT ME!
     */
    public int getReplacementCount() {
        return counter;
    }

    /**
     * only used internally (implementing
     * InterceptInputStream.Handler); skips the pattern length from the stream
     * and inserts the replacementPattern
     *
     * @param stream DOCUMENT ME!
     * @param pattern DOCUMENT ME!
     *
     * @throws Error DOCUMENT ME!
     */
    public void patternFound(InterceptInputStream stream, byte[] pattern) {
        counter++;

        try {
            stream.skip(pattern.length);
        } catch (IOException shouldntHappenBecauseLengthIsCached) {
            throw new Error();
        }

        stream.insertBytes(replacePattern);
    }
}
