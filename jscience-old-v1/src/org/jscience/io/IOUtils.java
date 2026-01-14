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

package org.jscience.io;

import java.io.*;
import java.util.Arrays;

/**
 * The class IOUtils contains some useful static functions to manipulate
 * data.
 *
 * @author Holger Antelmann
 */
public final class IOUtils {
    /**
     * implements an outputstream that writes nowhere
     */
    public static final OutputStream devNullStream = new OutputStream() {
        public void write(int b) { /*nothing*/
        }
    };

    /**
     * standard buffer size used (2048)
     */
    public static final int BUFFER_SIZE = 2048;

    private IOUtils() {
    }

    /**
     * reads a line from the console and returns the keyboard input as a string
     * after 'Enter' was pressed
     */
    public static String getConsoleInput() {
        String answer = new String();
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        try {
            answer = input.readLine();
        } catch (java.io.IOException e) {
            return null;
        }
        return answer;
    }

    /**
     * prints the given question and returns getConsoleInput()
     */
    public static String getConsoleInput(String question) {
        System.out.print(question);
        return getConsoleInput();
    }

    /**
     * de-serializes the given serialized bytes back to an object
     */
    public static Object deserialize(byte[] bytes)
            throws ClassNotFoundException, IOException {
        ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bin);
        return in.readObject();
    }

    /**
     * serializes the given object and returnes the serialized data as byte array
     *
     * @param obj must be Serializable
     */
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);
        out.writeObject(obj);
        out.flush();
        return bout.toByteArray();
    }

    /**
     * returns <code>indexOf(source, pattern, 0, source.length)</code>
     *
     * @see #indexOf(byte[],byte[],int,int)
     */
    public static int indexOf(byte[] source, byte[] pattern) {
        return indexOf(source, pattern, 0, source.length);
    }

    /**
     * searches for the first occurrence of the given byte pattern within the
     * given byte source.
     *
     * @param source     a byte array containing the data that is searched for the
     *                   occurrence of a pattern
     * @param pattern    a (non-empty) byte array containing the search pattern
     * @param beginIndex the index to begin the pattern search within the source array
     *                   (between 0 and source pattern length and smaller than endIndex)
     * @param endIndex   the index to end the pattern search within the source array
     *                   (between 0 and source pattern length and greater than beginIndex)
     * @return the starting position of the first occurrence of the given pattern
     *         or -1 if the pattern is not present
     * @throws ArrayIndexOutOfBoundsException if the parameter pattern is an
     *                                        empty array
     * @throws NullPointerException           if any of the given parameters are null
     */
    public static int indexOf(byte[] source, byte[] pattern, int beginIndex, int endIndex) {
        int limit = endIndex - pattern.length;
        loop:
        for (int i = beginIndex; i <= limit; i++) {
            if (source[i] != pattern[0]) continue;
            for (int j = 1; j < pattern.length; j++) {
                if (source[j + i] != pattern[j]) continue loop;
            }
            return i;
        }
        return -1;
    }

    /**
     * searches for an occurrence of the given byte pattern only within the
     * 'intersection' of the given two byte sources.
     *
     * @return the starting position of the first occurrence of the given pattern
     *         as a position of the first source or -1 if the pattern is not present
     * @throws ArrayIndexOutOfBoundsException if the parameter pattern is an
     *                                        empty array
     * @throws NullPointerException           if any of the given parameters are null
     * @see #indexOf(byte[],byte[])
     */
    public static int indexOf(byte[] source1, byte[] source2, byte[] pattern) {
        int l1 = (source1.length < pattern.length) ? source1.length + 1 : pattern.length;
        if (l1 < 1) return -1;
        int l2 = (source2.length < pattern.length) ? source2.length + 1 : pattern.length;
        if (l2 < 1) return -1;
        byte[] inter = new byte[l1 + l2 - 2];
        System.arraycopy(source1, source1.length + 1 - l1, inter, 0, l1 - 1);
        System.arraycopy(source2, 0, inter, l1 - 1, l2 - 1);
        int r = indexOf(inter, pattern, 0, inter.length);
        return (r == -1) ? -1 : r + (source1.length - l1 + 1);
    }

    /**
     * returns <code>indexOf(source, pattern, 0, source.length)</code>
     *
     * @see #indexOf(char[],char[],int,int)
     */
    public static int indexOf(char[] source, char[] pattern) {
        return indexOf(source, pattern, 0, source.length);
    }

    /**
     * searches for the first occurrence of the given character pattern within the
     * given byte source.
     *
     * @param source     a byte array containing the data that is searched for the
     *                   occurrence of a pattern
     * @param pattern    a (non-empty) character array containing the search pattern
     * @param beginIndex the index to begin the pattern search within the source array
     *                   (between 0 and source pattern length and smaller than endIndex)
     * @param endIndex   the index to end the pattern search within the source array
     *                   (between 0 and source pattern length and greater than beginIndex)
     * @return the starting position of the first occurrence of the given pattern
     *         or -1 if the pattern is not present
     * @throws ArrayIndexOutOfBoundsException if the parameter pattern is an
     *                                        empty array
     * @throws NullPointerException           if any of the given parameters are null
     */
    public static int indexOf(char[] source, char[] pattern, int beginIndex, int endIndex) {
        int limit = endIndex - pattern.length;
        loop:
        for (int i = beginIndex; i <= limit; i++) {
            if (source[i] != pattern[0]) continue;
            for (int j = 1; j < pattern.length; j++) {
                if (source[j + i] != pattern[j]) continue loop;
            }
            return i;
        }
        return -1;
    }

    /**
     * searches for an occurrence of the given byte pattern only within the
     * 'intersection' of the given two byte sources.
     *
     * @return the starting position of the first occurrence of the given pattern
     *         as a position of the first source or -1 if the pattern is not present
     * @throws ArrayIndexOutOfBoundsException if the parameter pattern is an
     *                                        empty array
     * @throws NullPointerException           if any of the given parameters are null
     * @see #indexOf(byte[],byte[])
     */
    public static int indexOf(char[] source1, char[] source2, char[] pattern) {
        int l1 = (source1.length < pattern.length) ? source1.length + 1 : pattern.length;
        if (l1 < 1) return -1;
        int l2 = (source2.length < pattern.length) ? source2.length + 1 : pattern.length;
        if (l2 < 1) return -1;
        char[] inter = new char[l1 + l2 - 2];
        System.arraycopy(source1, source1.length + 1 - l1, inter, 0, l1 - 1);
        System.arraycopy(source2, 0, inter, l1 - 1, l2 - 1);
        int r = indexOf(inter, pattern, 0, inter.length);
        return (r == -1) ? -1 : r + (source1.length - l1 + 1);
    }

    public static long indexOf(InputStream in, byte[] pattern) throws IOException {
        class Check {
            boolean test = false;
        }
        ;
        final Check found = new Check();
        InterceptInputStream.Handler handler = new InterceptInputStream.Handler() {
            public void patternFound(InterceptInputStream stream, byte[] pattern) {
                found.test = true;
            }
        };
        InterceptInputStream stream = new InterceptInputStream(in, pattern);
        stream.addHandler(handler);
        //stream.skip(beginIndex);
        int n = stream.read();
        while (!found.test && (n >= 0)) n = stream.read();
        return found.test ? stream.readSoFar() - 1 : -1;
    }

    /**
     * returns number of bytes transfered; closes both streams and flushes
     */
    public static long transfer(InputStream from, OutputStream to) throws IOException {
        return transfer(from, to, true);
    }

    /**
     * transfers the inputstream to the outputstream
     */
    public static long transfer(InputStream from, OutputStream to, boolean flushAndClose) throws IOException {
        return transfer(from, to, new byte[BUFFER_SIZE], flushAndClose);
    }

    /**
     * transfers the inputstream to the outputstream
     */
    public static long transfer(InputStream from, OutputStream to, byte[] buffer, boolean flushAndClose)
            throws IOException {
        return transfer(from, to, 0, buffer, flushAndClose);
    }

    /**
     * transfers the given number of bytes from one stream to the other
     */
    public static long transfer(InputStream from, OutputStream to, long maxBytes) throws IOException {
        return transfer(from, to, maxBytes, new byte[BUFFER_SIZE], false);
    }

    public static long transfer(InputStream from, OutputStream to, long maxBytes, boolean flushAndClose)
            throws IOException {
        return transfer(from, to, maxBytes, new byte[BUFFER_SIZE], flushAndClose);
    }

    /**
     * transfers the bytes from the given inputstream to the outputstream.
     *
     * @param from          the InputStream from which to read the data
     * @param to            the OutputStream to which to write the data
     * @param buffer        the byte buffer to be used; must have a length greater than zero
     * @param flushAndClose if true, the method also flushes and closes both streams
     * @param maxBytes      maximum number of bytes to be transfered;
     *                      if less than 1, the entire inputstream is transfered
     * @return the number of bytes actually transfered
     */
    public static long transfer(InputStream from, OutputStream to, long maxBytes, byte[] buffer, boolean flushAndClose)
            throws IOException {
        if (buffer.length < 1)
            throw new IllegalArgumentException("buffer length must be greater than 0");
        long counter = 0;
        if ((maxBytes > 0) && (buffer.length > maxBytes)) {
            buffer = new byte[(int) maxBytes];
        }
        int length = from.read(buffer);
        while (length > 0) {
            to.write(buffer, 0, length);
            counter += length;
            if ((maxBytes > 0) && (counter >= maxBytes)) break;
            if ((maxBytes > 0) && (buffer.length > (maxBytes - counter))) {
                buffer = new byte[(int) (maxBytes - counter)];
            }
            length = from.read(buffer);
        }
        if (flushAndClose) {
            to.flush();
            from.close();
            to.close();
        }
        return counter;
    }

    /**
     * transfers the reader to the writer.
     * Flushes writer and closes reader and writer.
     *
     * @return number of characters transfered;
     */
    public static long transfer(Reader from, Writer to) throws IOException {
        return transfer(from, to, 0, new char[BUFFER_SIZE], true);
    }

    /**
     * transfers the characters from the given reader to the writer.
     *
     * @param from          the Reader from which to read the data
     * @param to            the Writer to which to write the data
     * @param buffer        the char buffer to be used; must have a length greater than zero
     * @param flushAndClose if true, the method also flushes and closes both: reader and writer
     * @param maxChars      maximum number of characters to be transfered;
     *                      if less than 1, the entire inputstream is transfered
     * @return the number of characters actually transfered
     */
    public static long transfer(Reader from, Writer to, long maxChars, char[] buffer, boolean flushAndClose)
            throws IOException {
        if (buffer.length < 1)
            throw new IllegalArgumentException("buffer length must be greater than 0");
        long counter = 0;
        if ((maxChars > 0) && (buffer.length > maxChars)) {
            buffer = new char[(int) maxChars];
        }
        int length = from.read(buffer);
        while (length > 0) {
            to.write(buffer, 0, length);
            counter += length;
            if ((maxChars > 0) && (counter >= maxChars)) break;
            if ((maxChars > 0) && (buffer.length > (maxChars - counter))) {
                buffer = new char[(int) (maxChars - counter)];
            }
            length = from.read(buffer);
        }
        if (flushAndClose) {
            to.flush();
            from.close();
            to.close();
        }
        return counter;
    }

    public static byte[] asByteArray(InputStream stream) throws IOException {
        IOException exception = null;
        ByteArrayOutputStream baus = new ByteArrayOutputStream();
        transfer(stream, baus);
        return baus.toByteArray();
    }

    /**
     * compares the content of the two streams and returns true only if both
     * streams have equal content
     */
    public static boolean equals(InputStream stream1, InputStream stream2) throws IOException {
        return equals(stream1, stream2, 0);
    }

    /**
     * compares the content of the two streams and returns true only if both
     * streams have equal content for maxByte number of bytes.
     * If maxBytes is equal or less than 0, its value is ignored and the stream
     * is compared to its end.
     */
    public static boolean equals(InputStream stream1, InputStream stream2, long maxBytes) throws IOException {
        byte[] buffer1 = new byte[BUFFER_SIZE];
        byte[] buffer2 = new byte[BUFFER_SIZE];
        int n1 = stream1.read(buffer1);
        int n2 = stream2.read(buffer2);
        long count = 0;
        while ((n1 == buffer1.length) && (n2 == buffer2.length)) {
            if (maxBytes > 0) {
                count += n1;
                if (count > maxBytes) {
                    int rest = n1 - (int) (count - maxBytes);
                    byte[] tBuffer1 = new byte[rest];
                    byte[] tBuffer2 = new byte[rest];
                    System.arraycopy(buffer1, 0, tBuffer1, 0, rest);
                    System.arraycopy(buffer2, 0, tBuffer2, 0, rest);
                    return Arrays.equals(tBuffer1, tBuffer2);
                }
            }
            if (!Arrays.equals(buffer1, buffer2)) return false;
            n1 = stream1.read(buffer1);
            n2 = stream2.read(buffer2);
        }
        int minRead = ((n1 < n2) ? n1 : n2);
        if (minRead < 0) {
            return (n1 == n2);
        }
        if (maxBytes > 0) {
            count += minRead;
            if (count > maxBytes) {
                int rest = minRead - (int) (count - maxBytes);
                byte[] tBuffer1 = new byte[rest];
                byte[] tBuffer2 = new byte[rest];
                System.arraycopy(buffer1, 0, tBuffer1, 0, rest);
                System.arraycopy(buffer2, 0, tBuffer2, 0, rest);
                return Arrays.equals(tBuffer1, tBuffer2);
            }
        }
        if (n1 != n2) return false;
        byte[] endBuffer1 = new byte[n1];
        byte[] endBuffer2 = new byte[n2];
        System.arraycopy(buffer1, 0, endBuffer1, 0, n1);
        System.arraycopy(buffer2, 0, endBuffer2, 0, n2);
        return Arrays.equals(endBuffer1, endBuffer2);
    }

    /**
     * returns how many times the given byte pattern occurs within the file
     */
    public static int count(InputStream stream, byte[] pattern) throws IOException {
        ReplaceInputStream rs = new ReplaceInputStream(stream, pattern, new byte[0]);
        int n = rs.read();
        while (n > -1) n = rs.read();
        return rs.getReplacementCount();
    }

    /**
     * returns the number of bytes that are equal in both streams;
     * -1 is returned if both streams are equal until both ends.
     * If the <code>markSupported()</code> method returns true on both streams,
     * the method uses the <code>mark(int)</code> method to allow to reset
     * both streams to the point where both streams were still equal;
     * otherwise, both streams have already read the first unequal byte when
     * the method returns - or they are at the end of one or both streams.
     * If both streams have equal content but one stream is longer than the other,
     * the length of the shorter stream is returned.
     */
    public static long countEqualBytes(InputStream stream1, InputStream stream2) throws IOException {
        return countEqualBytes(stream1, stream2, 0);
    }

    /**
     * if maxBytes <=0, the streams are tested to their ends
     */
    public static long countEqualBytes(InputStream stream1, InputStream stream2, long maxBytes) throws IOException {
        boolean mark = stream1.markSupported() && stream2.markSupported();
        long count = 0;
        if (mark) {
            stream1.mark(1);
            stream2.mark(1);
        }
        int byte1 = stream1.read();
        int byte2 = stream2.read();
        while (byte1 == byte2) {
            if (byte1 < 0) return -1;
            if (mark) {
                stream1.mark(1);
                stream2.mark(1);
            }
            byte1 = stream1.read();
            byte2 = stream2.read();
            if ((maxBytes > 0) && (++count > maxBytes)) return -1;
        }
        return count;
    }

    /**
     * returns the number of characters that are equal in both readers;
     * -1 is returned if both readers are equal until both ends.
     * If the <code>markSupported()</code> method returns true on both readers,
     * the method uses the <code>mark(int)</code> method to allow to reset
     * both readers to the point where both readers were still equal;
     * otherwise, both readers have already read the first unequal character when
     * the method returns - or they are at the end of one or both readers.
     * If both readers have equal content but one reader is longer than the other,
     * the length of the shorter reader is returned.
     */
    public static long countEqualChars(Reader reader1, Reader reader2) throws IOException {
        return countEqualChars(reader1, reader2, 0);
    }

    /**
     * if maxChars <=0, the readers are tested to their ends
     */
    public static long countEqualChars(Reader reader1, Reader reader2, long maxChars) throws IOException {
        boolean mark = reader1.markSupported() && reader2.markSupported();
        long count = 0;
        if (mark) {
            reader1.mark(1);
            reader2.mark(1);
        }
        int char1 = reader1.read();
        int char2 = reader2.read();
        while (char1 == char2) {
            if (char1 < 0) return -1;
            if (mark) {
                reader1.mark(1);
                reader2.mark(1);
            }
            char1 = reader1.read();
            char2 = reader2.read();
            if ((maxChars > 0) && (++count > maxChars)) return -1;
        }
        return count;
    }

    public static long getSerialVersionUID(Object obj) {
        return getSerialVersionUID(obj.getClass());
    }

    public static long getSerialVersionUID(Class c) {
        return ObjectStreamClass.lookup(c).getSerialVersionUID();
    }
}
