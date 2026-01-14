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

package org.jscience.astronomy;


//perhaps we should provide a system which allows to add elements after creation time
public class Catalog {

    /**
     * The name of the catalog
     */
    private String name;

    /**
     * The names.
     * <p/>
     * These is the corresponding name to each position entry of the catalog
     */
    private String[] names;

    /**
     * The positions.
     * <p/>
     * <p>These are itsNpos triplets of numbers, each triplet being x,y,z in Gm.
     * The coordinate system is mean equinox of J2000.0.
     */
    private double[] itsR;

    /**
     * Initialise the object.
     * <p/>
     * <p>This creates the array itsR[], but does not initialise the array
     * values.
     *
     * @param aNpos Obtain space for so many xyz triplets of coordinates.
     */

    public void Catalog(String name, int aNpos) {

        this.name = name;
        names = new String[aNpos];
        itsR = new double[3 * aNpos];

    }

    /**
     * The name of the catalogue.
     */
    public String getName() {

        return name;

    }

    /**
     * The number of positions in the catalogue.
     */
    public int getNumEntries() {

        return names.length;

    }

    public String getEntryName(int pos) {

        return names[pos];

    }

    public double[] getEntry(int pos) {

        double[] triplet;

        triplet = new double[3];
        triplet[0] = itsR[3 * pos];
        triplet[0] = itsR[3 * pos + 1];
        triplet[0] = itsR[3 * pos + 2];

        return triplet;

    }

    public void setEntry(int pos, String name, double x, double y, double z) {

        names[pos] = name;
        itsR[3 * pos] = x;
        itsR[3 * pos + 1] = y;
        itsR[3 * pos + 2] = z;

    }

}
