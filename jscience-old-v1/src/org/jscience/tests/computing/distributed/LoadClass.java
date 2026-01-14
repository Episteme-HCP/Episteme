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
 * LoadClass.java
 *
 * Created on 09 March 2004, 18:58
 */
package org.jscience.tests.distributed;

import java.io.*;

import java.lang.reflect.Constructor;


/**
 * DOCUMENT ME!
 *
 * @author mmg20
 */
public class LoadClass {
    /** DOCUMENT ME! */
    static String fileName = "/home/mmg20/temp/biro.obj";

/**
     * Creates a new instance of LoadClass
     */
    public LoadClass() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            //writeBiro();
            //readBiro();
            loadClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    static void loadClass() throws Exception {
        ClassLoader cld = ClassLoader.getSystemClassLoader();
        System.out.println("" + cld);

        Class bC = cld.loadClass("islandev.testing.Biro");
        Class[] constrParams = { new Double(0).getClass() };
        Constructor bCr = bC.getConstructor(constrParams);
        Object[] params = { new Double(0.5) };
        PrintingThing b = (PrintingThing) bCr.newInstance(params);
        b.print();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    static void writeBiro() throws IOException {
        Biro b = new Biro(new Double(0));
        FileOutputStream fos = new FileOutputStream(fileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);

        oos.writeObject(b);
        oos.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    static void readBiro() throws Exception {
        FileInputStream fis = new FileInputStream(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);

        Object b = ois.readObject();
        ois.close();
        System.out.println("b is " + b);
    }
}
