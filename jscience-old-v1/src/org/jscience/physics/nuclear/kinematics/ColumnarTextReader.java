/***************************************************************
 * Nuclear Simulation Java Class Libraries
 * Copyright (C) 2003 Yale University
 *
 * Original Developer
 *     Dale Visser (dale@visser.name)
 *
 * OSI Certified Open Source Software
 *
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the University of Illinois/NCSA 
 * Open Source License.
 *
 * This program is distributed in the hope that it will be 
 * useful, but without any warranty; without even the implied 
 * warranty of merchantability or fitness for a particular 
 * purpose. See the University of Illinois/NCSA Open Source 
 * License for more details.
 *
 * You should have received a copy of the University of 
 * Illinois/NCSA Open Source License along with this program; if 
 * not, see http://www.opensource.org/
 **************************************************************/
package org.jscience.physics.nuclear.kinematics;

import java.io.*;


/**
 * Class for reading in spreadsheet-style text files.
 */
public class ColumnarTextReader extends InputStreamReader {
    /**
     * DOCUMENT ME!
     */
    private StringReader sr;

    /**
     * DOCUMENT ME!
     */
    private LineNumberReader lnr;

    /**
     * Creates a new ColumnarTextReader object.
     *
     * @param is DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public ColumnarTextReader(InputStream is)
        throws FileNotFoundException, IOException {
        super(is);
        lnr = new LineNumberReader(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void nextLine() throws IOException {
        sr = new StringReader(lnr.readLine());
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public String readString(int len) throws IOException {
        char[] temp;
        temp = new char[len];
        sr.read(temp);

        return new String(temp).trim();
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public int readInt(int len) throws IOException {
        return Integer.parseInt(readString(len));
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public double readDouble(int len) throws IOException {
        return Double.parseDouble(readString(len));
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void skipChars(int len) throws IOException {
        readString(len);
    }
}
