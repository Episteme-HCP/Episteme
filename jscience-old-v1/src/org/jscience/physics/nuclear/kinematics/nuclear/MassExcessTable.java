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

package org.jscience.physics.nuclear.kinematics.nuclear;

import org.jscience.physics.nuclear.kinematics.math.UncertainNumber;

import java.io.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;


/**
 * Class for storing binding energies. Has a hashtable storing binding
 * energies (as <code>UncertainNumber</code> objects) with the <code>
 * Nucleus</code> objects as keys. It also has a table mapping element symbols
 * to element numbers.
 */
public class MassExcessTable extends Hashtable {
    /** name of file to load stored binding energies as and object */
    private static final String BET_FILENAME = "MassExcesses.obj";

    /**
     * DOCUMENT ME!
     */
    String tableUsed = TableText.TABLE_1995.getName(); //default value

    /** array of element symbols keyed to element numbers */
    private String[] symbolTable = new String[120];

/**
     * Default constructor, necessary for loading from <code>ObjectInputStream</code>.
     */
    public MassExcessTable() {
    }

    /**
     * Get the binding energy in MeV of the specified nucleus.
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UncertainNumber getMassExcess(Nucleus n) {
        return (UncertainNumber) get(n);
    }

    /**
     * Get the element symbol for the specified element number.
     *
     * @param Z DOCUMENT ME!
     *
     * @return element symbol if Z is valid, empty string otherwise
     */
    public String getSymbol(int Z) {
        String rval = "";

        if ((Z >= 0) && (Z < symbolTable.length)) {
            rval = symbolTable[Z];
        }

        return rval;
    }

    /**
     * Get the element number for the specified element symbol,
     * ignoring case. Because of the ambiguity between "n" for neutron and "N"
     * for nitrogen, this only returns the 7, the element number of Nitrogen,
     * for "n" and "N".
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws NuclearException DOCUMENT ME!
     */
    public int getElementNumber(String s) throws NuclearException {
        for (int i = 1; i < symbolTable.length; i++) {
            if (s.equalsIgnoreCase(symbolTable[i])) {
                return i;
            }
        }

        throw new NuclearException("Couldn't find element number for \"" + s +
            "\".");
    }

    /**
     * Return a <code>Vector</code> of <code>Nucleus</code> objects
     * representing the isotopes of the given element.
     *
     * @param Z DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List getIsotopes(int Z) {
        Nucleus temp;
        List rval = new ArrayList();

        for (Enumeration e = keys(); e.hasMoreElements();) {
            temp = (Nucleus) e.nextElement();

            if (temp.Z == Z) {
                rval.add(temp);
            }
        }

        return rval;
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean massExists(Nucleus n) {
        return (get(n) != null);
    }

    /**
     * Associate the given element number and symbol.
     *
     * @param Z DOCUMENT ME!
     * @param symbol DOCUMENT ME!
     */
    private void storeSymbol(int Z, String symbol) {
        symbolTable[Z] = symbol;
    }

    /**
     * DOCUMENT ME!
     *
     * @param which DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static public MassExcessTable load(TableText which) {
        MassExcessTable bet;

        try {
            FileInputStream fis = new FileInputStream(BET_FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            bet = (MassExcessTable) ois.readObject();
            ois.close();

            if (bet.tableUsed == null) {
                throw new NuclearException(
                    "I could not determine which mass table was used to create this table.");
            } else if (bet.tableUsed != which.getName()) {
                throw new NuclearException("Program asked for " +
                    which.getYear() +
                    " mass table. The file has the data from " + bet.tableUsed +
                    ".");
            }
        } catch (Exception e) {
            System.err.println(
                "MassExcessTable.load(): I was unable to load the binding energy table. Here's why:");
            System.err.println(
                "----------------------------------------------------------");
            System.err.println(e);
            System.err.println(
                "----------------------------------------------------------");
            System.err.println("I will attempt to build the table using \"" +
                which.getName() + "\".");
            bet = build(which);
        }

        return bet;
    }

    /**
     * We end up here if loading the table was unsuccessful. The masses
     * are read in from the ASCII mass table, and a new table is constructed
     * and saved.
     *
     * @param which DOCUMENT ME!
     *
     * @return the table
     */
    static private MassExcessTable build(TableText which) {
        String betFilePath = "???";

        try {
            betFilePath = new File(BET_FILENAME).getCanonicalPath();
        } catch (Exception ioe) {
            System.err.println(ioe);
        }

        MassExcessTable rval = doIt(which);

        if (rval != null) {
            System.out.println(rval.getClass().getName() +
                ".build(): I successfully read in the mass table from \"" +
                which.getName() + "\"");
            System.out.println("and saved the table in usable form in \"" +
                betFilePath + "\".");
        }

        return rval;
    }

    /**
     * The workhorse for build(). Attempts to construct a table from
     * the ASCII file <code> mass_rmd.mas95</code> residing in the
     * <code>org.jscience.physics.nuclear.kinematics.nuclear</code> package.
     *
     * @param which DOCUMENT ME!
     *
     * @return the table
     */
    static private MassExcessTable doIt(TableText which) {
        String s = "";
        StringReader sr;
        int Z;
        int A;
        MassExcessTable bet = new MassExcessTable();
        bet.setTableText(which);

        try {
            InputStreamReader isr = new InputStreamReader(bet.getClass()
                                                             .getResourceAsStream(which.getName()));
            LineNumberReader lnr = new LineNumberReader(isr);

            for (int i = 0; i < 40; i++) {
                s = lnr.readLine();
            }

            do {
                sr = new StringReader(s);
                sr.skip(9); // 1st col. & (N-Z) +& N
                Z = readInt(5, sr);
                A = readInt(5, sr);
                sr.skip(1);

                String symbol = readString(3, sr).trim();
                sr.skip(which.getColsToSkip());

                /* Text file in keV, I want MeV. */
                UncertainNumber m_excess = new UncertainNumber(readDouble(
                            which.getColsMassExcess(), sr) / 1000.0,
                        readDouble(which.getColsUncertainty(), sr) / 1000.0);
                bet.put(new Nucleus(Z, A, true), m_excess);
                bet.storeSymbol(Z, symbol);
                sr.close();
                s = lnr.readLine();
            } while (s != null);

            lnr.close();
            s = null;
            sr = null;
            lnr = null;
            isr = null;

            FileOutputStream fos = new FileOutputStream(MassExcessTable.BET_FILENAME);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(bet);
            oos.close();
            oos = null;
            fos = null;
        } catch (Exception e) {
            System.err.println(bet.getClass().getName() +
                ".doIt(): I was unable to load the mass table from \"" + which +
                "\". Here's why:");
            System.err.println(
                "----------------------------------------------------------");
            System.err.println(e);
            System.err.println(
                "----------------------------------------------------------");

            return null;
        }

        return bet;
    }

    /**
     * DOCUMENT ME!
     *
     * @param which DOCUMENT ME!
     */
    void setTableText(TableText which) {
        tableUsed = which.getName();
    }

    /**
     * private worker methods for reading in strings, ints, and doubles
     * from a Reader.
     *
     * @param len DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static private String readString(int len, Reader r)
        throws IOException {
        char[] temp;
        temp = new char[len];
        r.read(temp);

        String s = new String(temp);

        return s.replace('#', ' ');
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static private int readInt(int len, Reader r) throws IOException {
        return Integer.parseInt(readString(len, r).trim());
    }

    /**
     * DOCUMENT ME!
     *
     * @param len DOCUMENT ME!
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static private double readDouble(int len, Reader r)
        throws IOException {
        return Double.parseDouble(readString(len, r).trim());
    }

    /**
     * for testing purposes only
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        MassExcessTable.load(TableText.TABLE_1995);
    }
}
