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

package org.jscience.mathematics.analysis.functions;

import org.jscience.mathematics.algebraic.matrices.DoubleVector;

import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


/**
 * The class SampledDataSet represents a set of measured or generated data,
 * consisting of a number of columns. The data in the SampledDataSet can be
 * retrieved as {@link
 * org.jscience.mathematics.algebraic.matrices.DoubleVector}s both row wise
 * and column wise. Note that the number of columns need not be identical, and
 * that the number of columns that can be retrieved is equal to the smallest
 * number of elements in all of the rows. <br>
 * The class provides a static method {@link #getDoubleVector(String)} that
 * returns a DoubleVector from a String like "1.2 3.44 : -2.1", where
 * whitespaces colons and semicolons are separators. This method may be useful
 * for other applications. <br>
 * Data are normally read from data files represented by a File object or a
 * file name in a String, but may also be loaded from a URL or an InputStream.
 * The format is like in the example below here.<pre>1.1 1.2   1.32.1,2.2; 2.3
 * 3.1,:3.2 : 3.3:3.4</pre>To load data like the above from a file name
 * "datafile" do like this:<pre>
 * SampledDataSet data = new SampledDataSet ( "datafile" );
 * int n = data.length(); // number of rows
 * DoubleVector firstRow = data.getRow( 1 ); // (1.1,1.2,1.3) dimension 3
 * DoubleVector lastRow = data.getRow( n ); // (3.1,3.2,3.3,3.4) dimension 4
 * DoubleVector firstColumn = data.getColumn( 1 ); // (1.1,2.1,3.1) dimension 3
 * </pre>
 *
 * @author Carsten Knudsen, Silvere Martin-Michiellot
 * @version 1.0
 */

//also look into org.jscience.io.TextReader and TextWriter for similar/complementary functionnality
public class SampledDoubleDataSet implements Serializable {
    /** DOCUMENT ME! */
    private List list;

/**
     * Constructs a SampledDoubleDataSet from the contents of the named file.
     *
     * @param fileName DOCUMENT ME!
     * @throws IOException           DOCUMENT ME!
     * @throws MalformedURLException DOCUMENT ME!
     */
    public SampledDoubleDataSet(String fileName)
        throws IOException, MalformedURLException {
        this(new File(fileName));
    } // constructor

/**
     * Constructs a SampledDoubleDataSet from the contents of the file
     * represented by the File object.
     *
     * @param file DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public SampledDoubleDataSet(File file) throws IOException {
        this(new FileInputStream(file));
    } // constructor

/**
     * Constructs a SampledDoubleDataSet from the contents located at the URL.
     *
     * @param url DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public SampledDoubleDataSet(URL url) throws IOException {
        this(url.openStream());
    } // constructor

/**
     * Constructs a SampledDoubleDataSet from the contents read through the
     * InputStream.
     *
     * @param is DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public SampledDoubleDataSet(InputStream is) throws IOException {
        list = new ArrayList();

        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr);
        String line;

        while ((line = reader.readLine()) != null) {
            list.add(getDoubleVector(line));
        } // while
    } // constructor

    /**
     * Return the data set as a read-only list where each entry is a
     * {@link org.jscience.mathematics.algebraic.matrices.DoubleVector}.
     *
     * @return DOCUMENT ME!
     */
    public List asList() {
        return Collections.unmodifiableList(list);
    } // asList

    /**
     * The method returns the length of the SampledDataSet which is the
     * number of rows of data.
     *
     * @return DOCUMENT ME!
     */
    public int length() {
        return list.size();
    } // length

    /**
     * The method returns a {@link
     * org.jscience.mathematics.algebraic.matrices.DoubleVector} with the data
     * from the <code>i</code>th row, starting at 0.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleVector getRow(int i) {
        return (DoubleVector) list.get(i);
    } // getRow

    /**
     * The method returns a {@link
     * org.jscience.mathematics.algebraic.matrices.DoubleVector} with the data
     * from the <code>i</code>th column.
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DoubleVector getColumn(int i) {
        int n = length();
        DoubleVector v = new DoubleVector(n);

        for (int j = 0; j < n; j++) {
            v.setElement(j, getRow(j).getPrimitiveElement(i));
        } // for

        return v;
    } // getColumn

    /**
     * The method returns a {@link
     * org.jscience.mathematics.algebraic.matrices.DoubleVector} from a String
     * with numbers separated by spaces, tabs, ",", ":" or ";".
     *
     * @param line DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DoubleVector getDoubleVector(String line) {
        StringTokenizer tokenizer = new StringTokenizer(line, " ,;:\t", false);
        int n = tokenizer.countTokens();
        DoubleVector v = new DoubleVector(n);

        for (int i = 0; i < n; i++) {
            v.setElement(i, Double.parseDouble(tokenizer.nextToken()));
        } // for

        return v;
    } // getDoubleVector

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void main(String[] args) throws IOException {
        SampledDoubleDataSet dataSet = new SampledDoubleDataSet("data");

        for (int i = 0; i < (dataSet.length() + 1); i++) {
            System.out.println(dataSet.getRow(i));
        }

        try {
            for (int i = 0; true; i++) {
                System.out.println(dataSet.getColumn(i));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    } // main
}
