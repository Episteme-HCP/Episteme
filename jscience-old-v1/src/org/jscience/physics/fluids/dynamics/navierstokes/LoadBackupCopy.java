/*
 * CopiaRespaldo.java
 *
 * Created on 30 of noviembre of 2001, 19:57
 */
package org.jscience.physics.fluids.dynamics.navierstokes;

import org.jscience.physics.fluids.dynamics.KernelADFC;

import java.io.FileInputStream;
import java.io.ObjectInputStream;


/**
 * DOCUMENT ME!
 *
 * @author balrog
 */
public class LoadBackupCopy {
    /** DOCUMENT ME! */
    private KernelADFC kernel;

    /** DOCUMENT ME! */
    private ObjectInputStream is;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private double[][] u0;

    /** DOCUMENT ME! */
    private double[][] u00;

    /** DOCUMENT ME! */
    private double[][] u000;

    /** DOCUMENT ME! */
    private double[] p;

    /** DOCUMENT ME! */
    private double[] ink;

    /** DOCUMENT ME! */
    private boolean ok;

/**
     * Creates new CopiaRespaldo
     *
     * @param kadfc DOCUMENT ME!
     */
    public LoadBackupCopy(KernelADFC kadfc) {
        kernel = kadfc;

        name = kernel.getConfiguration().meshName + ".backup";
        ok = false;
    }

    /**
     * DOCUMENT ME!
     */
    public void loadBackup() {
        try {
            is = new ObjectInputStream(new FileInputStream(name));
        } catch (Exception ex) {
            kernel.out(
                "</FONT COLOR=#FF0000><B>LoadBackupCopy:</B> exception al open " +
                name + "</FONT>");

            return;
        }

        if (kernel.continueSerializedSimulation()) {
            try {
                u0 = (double[][]) is.readObject();
                u00 = (double[][]) is.readObject();
                u000 = (double[][]) is.readObject();
                p = (double[]) is.readObject();
                ink = (double[]) is.readObject();
            } catch (Exception ex) {
                kernel.out(
                    "</FONT COLOR=#FF0000><B>LoadBackupCopy:</B> exception al deserialize data</FONT>");

                return;
            }

            try {
                is.close();
            } catch (Exception ex) {
                kernel.out(
                    "</FONT COLOR=#FF0000><B>LoadBackupCopy:</B> exception al cerrar " +
                    name + "</FONT>");

                return;
            }

            kernel.out("<FONT COLOR=#008000><B>Continuing BackUp done in " +
                name + "<B></FONT>");
            ok = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getU0() {
        return u0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getU00() {
        return u00;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[][] getU000() {
        return u000;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getP() {
        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public double[] getInk() {
        return ink;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOk() {
        return ok;
    }
}
