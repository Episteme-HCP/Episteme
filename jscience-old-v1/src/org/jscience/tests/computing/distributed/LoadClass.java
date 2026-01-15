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
