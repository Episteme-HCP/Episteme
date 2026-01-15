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

import java.util.Iterator;
import java.util.Vector;


/**
 * A specialized stream that allows to provide custom actions upon the
 * occurence of a given byte pattern. The custom actions occur through
 * registered Handler objects; bytes can be skipped from the stream (to not
 * have the given pattern occur in the stream) and bytes can be inserted into
 * the stream through given methods. The class may be subclassed to provide
 * more sophisticated pattern matching, as this implementation only acts upon
 * exact matches of a search pattern found.
 *
 * @author Holger Antelmann
 *
 * @see ReplaceInputStream
 */
public class InterceptInputStream extends InputStream {
    /**
     * DOCUMENT ME!
     */
    InputStream in;

    /**
     * DOCUMENT ME!
     */
    byte[] searchPattern;

    /**
     * DOCUMENT ME!
     */
    byte[] buffer1;

    /**
     * DOCUMENT ME!
     */
    byte[] buffer2;

    /**
     * DOCUMENT ME!
     */
    byte[] insertion = null;

    /**
     * DOCUMENT ME!
     */
    int insertionCursor = -1;

    /**
     * DOCUMENT ME!
     */
    int cursor = -1;

    /**
     * DOCUMENT ME!
     */
    int length = -1;

    /**
     * DOCUMENT ME!
     */
    int length2 = -1;

    /**
     * DOCUMENT ME!
     */
    int alert = -1;

    /**
     * DOCUMENT ME!
     */
    Vector<Handler> listener = new Vector<Handler>(1, 1);

    /**
     * DOCUMENT ME!
     */
    long count = 0;

    /**
     * DOCUMENT ME!
     */
    boolean consume = false;

    /**
     * DOCUMENT ME!
     */
    boolean dirty = false;

    /**
     * Creates a new InterceptInputStream object.
     *
     * @param in DOCUMENT ME!
     * @param searchPattern DOCUMENT ME!
     */
    public InterceptInputStream(InputStream in, byte[] searchPattern) {
        if (searchPattern.length < 1) {
            throw new IllegalArgumentException(
                "length of searchPattern must be greater than 0");
        }

        this.in = in;
        this.searchPattern = searchPattern;

        int bsize = (searchPattern.length > IOUtils.BUFFER_SIZE)
            ? searchPattern.length : IOUtils.BUFFER_SIZE;
        buffer1 = new byte[bsize];
        buffer2 = new byte[bsize];
    }

    /**
     * DOCUMENT ME!
     *
     * @param handler DOCUMENT ME!
     */
    public void addHandler(Handler handler) {
        listener.add(handler);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void close() throws IOException {
        in.close();
    }

    /**
     * returns false
     *
     * @return DOCUMENT ME!
     */
    public boolean markSupported() {
        return false;
    }

    /**
     * throws IOException
     *
     * @throws IOException DOCUMENT ME!
     */
    public void reset() throws IOException {
        throw new IOException("mark not supported");
    }

    /**
     * inserts the given bytes into the stream, so that these bytes are
     * read next before all others that would normally follow at this point.
     * The inserted bytes are unchecked in regards to the search pattern.
     *
     * @param bytes DOCUMENT ME!
     */
    public synchronized void insertBytes(byte[] bytes) {
        dirty = true;

        if (insertion == null) {
            insertion = bytes;
            insertionCursor = 0;
        } else {
            byte[] temp = new byte[(insertion.length + bytes.length) -
                insertionCursor];
            System.arraycopy(insertion, insertionCursor, temp, 0,
                insertion.length - insertionCursor);
            System.arraycopy(bytes, 0, temp,
                insertion.length - insertionCursor, bytes.length);
            insertion = temp;
            insertionCursor = 0;
        }
    }

    /**
     * skips bytes only from the source input stream - ignoring
     * inserted bytes
     *
     * @param numberOfBytes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public synchronized long skip(long numberOfBytes) throws IOException {
        dirty = true;
        consume = true;

        long counter = -1;

        for (long i = 0; i < numberOfBytes; i++) {
            if (read() < 0) {
                break;
            }

            counter++;
        }

        consume = false;

        if (cursor >= 0) {
            alert = checkForMatch(buffer1, buffer2, searchPattern, cursor);
        }

        return counter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public synchronized int read() throws IOException {
        dirty = true;

        if (!consume && (insertion != null)) {
            if (insertionCursor < insertion.length) {
                return insertion[insertionCursor++];
            } else {
                insertion = null;
                insertionCursor = -1;
            }
        }

        if (cursor < 0) {
            length = in.read(buffer1);

            if (length < 0) {
                return -1;
            }

            length2 = in.read(buffer2);
            cursor = 0;

            if (!consume) {
                alert = checkForMatch(buffer1, buffer2, searchPattern, cursor);
            }
        } else if (cursor >= length) {
            byte[] temp = buffer1;
            buffer1 = buffer2;
            buffer2 = temp;
            length = length2;

            if (length < 0) {
                cursor = -1;

                return -1;
            }

            length2 = in.read(buffer2);
            cursor = 0;

            if (!consume) {
                alert = checkForMatch(buffer1, buffer2, searchPattern, cursor);
            }
        }

        if (!consume && (alert == cursor)) {
            dirty = false;

            Iterator<Handler> it = listener.iterator();

            while (it.hasNext()) {
                it.next().patternFound(this, searchPattern);
            }

            if (cursor < 0) {
                return -1;
            }

            alert = checkForMatch(buffer1, buffer2, searchPattern, cursor);

            if (dirty) {
                return read();
            }
        }

        count++;

        return buffer1[cursor++];
    }

    /**
     * returns the total number of bytes read from the original stream
     * so far (not considering inserted bytes but counting skipped bytes)
     *
     * @return DOCUMENT ME!
     */
    public long readSoFar() {
        return count;
    }

    /**
     * checks whether the exact searchPattern can be found in buffer1
     * or in the intersection between buffer1 and buffer2. May be overwritten
     * to do some more sophisticated pattern matching instead of just testing
     * for an exact match.
     *
     * @param buffer1 byte array containing the current stream buffer
     * @param buffer2 byte array containing the following buffer (to enable
     *        pattern search in intersections of two buffers, so that all
     *        occurences within the stream can be found)
     * @param searchPattern the pattern that is used for the pattern matching
     * @param cursor the current position of the stream within buffer1
     *
     * @return the index relative to buffer1 where the first matching pattern
     *         begins between the cursor  until the end of buffer1 (while
     *         considering buffer2 for overlaps) or -1 if no pattern match was
     *         found
     *
     * @see IOUtils#indexOf(byte[],byte[],int,int)
     * @see IOUtils#indexOf (byte[], byte[], byte[])
     */
    protected int checkForMatch(byte[] buffer1, byte[] buffer2,
        byte[] searchPattern, int cursor) {
        int index = IOUtils.indexOf(buffer1, searchPattern, cursor,
                buffer1.length);

        if (index < 0) {
            index = IOUtils.indexOf(buffer1, buffer2, searchPattern);
        }

        return index;
    }

/**
     * used to handle pattern occurences for an InterceptInputStream
     */
    public static interface Handler {
        /**
         * at the time this method is called, the given stream is
         * at the beginning of the given pattern that is to be read next
         *
         * @param stream DOCUMENT ME!
         * @param pattern DOCUMENT ME!
         */
        void patternFound(InterceptInputStream stream, byte[] pattern);
    }
}
