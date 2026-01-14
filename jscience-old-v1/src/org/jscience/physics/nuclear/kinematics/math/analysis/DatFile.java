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

package org.jscience.physics.nuclear.kinematics.math.analysis;

import java.io.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class DatFile {
    /**
     * DOCUMENT ME!
     */
    private double[] data;

    /**
     * Creates a new DatFile object.
     *
     * @param file DOCUMENT ME!
     */
    public DatFile(File file) {
        readInFile(file);
    }

    /**
     * Creates a new DatFile object.
     *
     * @param file DOCUMENT ME!
     * @param outData DOCUMENT ME!
     */
    public DatFile(File file, double[] outData) {
        data = outData;
        writeFile(file);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getData() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    private void readInFile(File file) {
        try {
            FileReader reader = new FileReader(file);
            StreamTokenizer st = new StreamTokenizer(reader);
            int maxCh = 0;
            st.nextToken(); //get channel

            do {
                int channel = (int) st.nval;

                if (channel > maxCh) {
                    maxCh = channel;
                }

                st.nextToken(); //skip counts
                st.nextToken(); //get channel
            } while (st.ttype != StreamTokenizer.TT_EOF);

            reader.close();
            data = new double[maxCh + 1];
            reader = new FileReader(file);
            st = new StreamTokenizer(reader);
            st.nextToken(); //get channel

            do {
                int channel = (int) st.nval;
                st.nextToken(); //get counts
                data[channel] = st.nval;
                st.nextToken(); //get channel
            } while (st.ttype != StreamTokenizer.TT_EOF);

            reader.close();
        } catch (FileNotFoundException fnf) {
            System.err.println("File not found: " + fnf);
        } catch (IOException ioe) {
            System.err.println("Problem reading file: " + ioe);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     */
    private void writeFile(File file) {
        try {
            FileWriter writer = new FileWriter(file);

            for (int channel = 0; channel < data.length; channel++) {
                writer.write(channel + "\t" + data[channel] + "\n");
            }

            writer.close();
        } catch (IOException ioe) {
            System.err.println("Problem writing file: " + ioe);
        }
    }
}
