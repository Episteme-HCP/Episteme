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

package org.jscience.physics.waves.optics.materials;

import org.jscience.util.IllegalDimensionException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class MaterialFromFile {
/**
     * Creates a new MaterialFromFile object.
     */
    public MaterialFromFile() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    static public void readFile(File f) {
        BufferedReader bf;
        Material m = null;

        if (f.exists() && f.canRead()) {
            try {
                bf = new BufferedReader(new FileReader(f));

                //String s = null;
                do {
                    m = readMaterial(bf);

                    if (m != null) {
                        System.out.println(m.getName());
                    }
                } while (m != null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param bf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static private Material readMaterial(BufferedReader bf)
        throws IOException {
        String s = bf.readLine();

        if (s != null) {
            Material mat = new Material();
            mat.setName(s);

            s = bf.readLine();

            while ((s != null) && !s.equals("")) {
                try {
                    mat.addParameterSet(s);
                } catch (IllegalDimensionException e) {
                    System.out.println(mat.getName() + ": " + e.toString());
                } catch (InvalidMethodTypeException e) {
                    System.out.println("Unknown method: " + e.name);
                } catch (NumberFormatException e) {
                    System.out.println(mat.getName() + ": " +
                        "NumberFormatException");
                }

                s = bf.readLine();
            }

            return mat;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    static void main(String[] args) {
        File f = new File((new File("Materials.txt")).getAbsolutePath());

        readFile(f);
    }
}
