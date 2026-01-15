/*
 * CopiaRespaldo.java
 *
 * Created on 30 of noviembre of 2001, 19:57
 */
package org.jscience.physics.fluids.dynamics.navierstokes;

import org.jscience.physics.fluids.dynamics.KernelADFC;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;


/**
 * DOCUMENT ME!
 *
 * @author balrog
 */
public class SaveBackupCopy {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private ObjectOutputStream os;

    /** DOCUMENT ME! */
    private String name;

/**
     * Creates new SaveBackupCopy
     *
     * @param kadfc DOCUMENT ME!
     */
    public SaveBackupCopy(KernelADFC kadfc) {
        kernel = kadfc;

        name = kernel.getConfiguration().meshName + ".backup";
    }

    /**
     * DOCUMENT ME!
     *
     * @param u0 DOCUMENT ME!
     * @param u00 DOCUMENT ME!
     * @param u000 DOCUMENT ME!
     * @param p DOCUMENT ME!
     * @param ink DOCUMENT ME!
     */
    public void salvaBackup(double[][] u0, double[][] u00, double[][] u000,
        double[] p, double[] ink) {
        try {
            os = new ObjectOutputStream(new FileOutputStream(name));
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>SaveBackupCopy:</B> exception open " +
                name + "</FONT>");
        }

        try {
            os.writeObject(u0);
            os.writeObject(u00);
            os.writeObject(u000);
            os.writeObject(p);
            os.writeObject(ink);
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>SaveBackupCopy:</B> exception serialize data</FONT>");
        }

        try {
            os.close();
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>SaveBackupCopy:</B> exception closing " +
                name + "</FONT>");
        }

        // kernel.out("<FONT COLOR=#008000><B>BackUp done in "+name+"<B></FONT>");
    }
}
