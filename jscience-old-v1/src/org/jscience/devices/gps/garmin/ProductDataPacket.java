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

package org.jscience.devices.gps.garmin;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class ProductDataPacket extends GarminPacket {
    /** Product-ID of GPS. */
    protected int productID;

    /** Software version in GPS. */
    protected int SWversion;

    /** Description of GPS as given by GPS. */
    protected String productDesc;

/**
     * Treats the packet p as a packet containing product-data. Throws
     * PacketNotRecognizedException if p is not a product-data-packet.
     *
     * @param p DOCUMENT ME!
     */
    public ProductDataPacket(int[] p) {
        super(p);

        if (getID() != Pid_Product_Data) {
            throw (new PacketNotRecognizedException(Pid_Product_Data, getID()));
        }

        productID = readWord(3);
        SWversion = readWord(5);
        productDesc = readNullTerminatedString(7);
    }

/**
     * This method is a copy-constructor allowing to "upgrade" a GarminPacket
     * to a ProductDataPacket. Throws PacketNotRecognizedException if p is not
     * a product-data-packet.
     *
     * @param p DOCUMENT ME!
     */
    public ProductDataPacket(GarminPacket p) {
        this(p.packet);
    }

    /**
     * Returns the product ID of the GPS.
     *
     * @return DOCUMENT ME!
     */
    public int getProductID() {
        return productID;
    }

    /**
     * Returns the version of the software in the GPS.
     *
     * @return DOCUMENT ME!
     */
    public int getSWVersion() {
        return SWversion;
    }

    /**
     * Returns the supplied description of the GPS.
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return productDesc;
    }

    /**
     * Returns a human-readable version of this packet.
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer res = new StringBuffer();
        res.append(productDesc + '\n');
        res.append("Product ID: " + productID);
        res.append("\nSoftware version: " + SWversion);

        return res.toString();
    }
}
