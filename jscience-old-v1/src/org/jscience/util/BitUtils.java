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

/*
 * Bits.java, 10.11.2005
 *
 * Author:
 *     Franz Wilhelmstötter (franz.wilhelmstoetter@gmx.at)
 */
package org.jscience.util;

/**
 * 
DOCUMENT ME!
 *
 * @author Franz Wilhelmstötter
 * @version $Revision: 1.3 $, $Date: 2007-10-23 18:24:15 $
 */
final class BitUtils {
    /**
     * Creates a new BitUtils object.
     */
    private BitUtils() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param bits DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] shiftRight(byte[] data, int bits) {
        //checkNull(data, "Data");
        if (bits <= 0) {
            return data;
        }

        int d = 0;

        if (data.length == 1) {
            if (bits <= 8) {
                d = data[0] & 0xFF;
                d >>>= bits;
                data[0] = (byte) d;
            } else {
                data[0] = 0;
            }
        } else if (data.length > 1) {
            int carry = 0;

            if (bits < 8) {
                for (int i = data.length - 1; i > 0; --i) {
                    carry = data[i - 1] & (1 << (bits - 1));
                    carry = carry << (8 - bits);

                    d = data[i] & 0xFF;
                    d >>>= bits;
                    d |= carry;

                    data[i] = (byte) d;
                }

                d = data[0] & 0xFF;
                d >>>= bits;

                data[0] = (byte) d;
            } else {
                for (int i = data.length - 1; i > 0; --i) {
                    data[i] = data[i - 1];
                }

                data[0] = 0;
                shiftRight(data, bits - 8);
            }
        }

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param bits DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] shiftLeft(byte[] data, int bits) {
        //checkNull(data, "Data");
        if (bits <= 0) {
            return data;
        }

        int d = 0;

        if (data.length == 1) {
            if (bits <= 8) {
                d = data[0] & 0xFF;
                d <<= bits;
                data[0] = (byte) d;
            } else {
                data[0] = 0;
            }
        } else if (data.length > 1) {
            int carry = 0;

            if (bits < 8) {
                for (int i = 0; i < (data.length - 1); ++i) {
                    carry = data[i + 1] & (1 >>> (8 - bits));

                    d = data[i] & 0xFF;
                    d <<= bits;
                    d |= carry;

                    data[i] = (byte) d;
                }

                d = data[data.length - 1] & 0xFF;
                d <<= bits;

                data[data.length - 1] = (byte) d;
            } else {
                for (int i = 0; i < (data.length - 1); ++i) {
                    data[i] = data[i + 1];
                }

                data[data.length - 1] = 0;
                shiftLeft(data, bits - 8);
            }
        }

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] increment(byte[] data) {
        //checkNull(data, "Data");
        if (data.length == 0) {
            return data;
        }

        int d = 0;
        int pos = data.length - 1;

        do {
            d = data[pos] & 0xFF;
            ++d;
            data[pos] = (byte) d;
            --pos;
        } while ((pos >= 0) && (data[pos + 1] == 0));

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] invert(byte[] data) {
        //checkNull(data, "Data");
        int d = 0;

        for (int i = 0; i < data.length; ++i) {
            d = data[i] & 0xFF;
            d = ~d;
            data[i] = (byte) d;
        }

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] complement(byte[] data) {
        return increment(invert(data));
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param index DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static byte[] setBit(byte[] data, int index, boolean value) {
        //checkNull(data, "Data");
        if (data.length == 0) {
            return data;
        }

        final int MAX = data.length * 8;

        if ((index >= MAX) || (index < 0)) {
            throw new IndexOutOfBoundsException("Index out of bounds: " +
                index);
        }

        int pos = data.length - (index / 8) - 1;
        int bitPos = index % 8;

        int d = data[pos] & 0xFF;

        if (value) {
            d = d | (1 << bitPos);
        } else {
            d = d & ~(1 << bitPos);
        }

        data[pos] = (byte) d;

        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean getBit(byte[] data, int index) {
        //checkNull(data, "Data");
        if (data.length == 0) {
            return false;
        }

        final int MAX = data.length * 8;

        if ((index >= MAX) || (index < 0)) {
            throw new IndexOutOfBoundsException("Index out of bounds: " +
                index);
        }

        int pos = data.length - (index / 8) - 1;
        int bitPos = index % 8;
        int d = data[pos] & 0xFF;

        return (d & (1 << bitPos)) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(long n) {
        StringBuilder out = new StringBuilder();

        for (int i = 63; i >= 0; --i) {
            out.append((n >>> i) & 1);
        }

        return out.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(byte... data) {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < data.length; ++i) {
            for (int j = 7; j >= 0; --j) {
                out.append((data[i] >>> j) & 1);
            }

            out.append('|');
        }

        return out.toString();
    }
}
