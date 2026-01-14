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

package org.jscience.geography.coordinates;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
abstract class GcsGtrsDataSet {
    /** DOCUMENT ME! */
    public int _minimum_cell_id;

    /** DOCUMENT ME! */
    public double _row_lat;

    /** DOCUMENT ME! */
    public double _starting_lon;

    /** DOCUMENT ME! */
    public double _cell_width;

    /** DOCUMENT ME! */
    public double _cell_width_inv;

    /** DOCUMENT ME! */
    public int _maximum_cell_id;

    /** DOCUMENT ME! */
    public double _irreg_cell_lon;

    /** DOCUMENT ME! */
    public double _irreg_cell_width;

/**
     * Creates a new GcsGtrsDataSet object.
     *
     * @param minimum_cell_id  DOCUMENT ME!
     * @param row_lat          DOCUMENT ME!
     * @param starting_lon     DOCUMENT ME!
     * @param cell_width       DOCUMENT ME!
     * @param cell_width_inv   DOCUMENT ME!
     * @param maximum_cell_id  DOCUMENT ME!
     * @param irreg_cell_lon   DOCUMENT ME!
     * @param irreg_cell_width DOCUMENT ME!
     */
    protected GcsGtrsDataSet(int minimum_cell_id, double row_lat,
        double starting_lon, double cell_width, double cell_width_inv,
        int maximum_cell_id, double irreg_cell_lon, double irreg_cell_width) {
        _minimum_cell_id = minimum_cell_id;
        _row_lat = row_lat;
        _starting_lon = starting_lon;
        _cell_width = cell_width;
        _cell_width_inv = cell_width_inv;
        _maximum_cell_id = maximum_cell_id;
        _irreg_cell_lon = irreg_cell_lon;
        _irreg_cell_width = irreg_cell_width;
    }
}
