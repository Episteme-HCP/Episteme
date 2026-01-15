/*
 * ProtocoleEIS.java
 *
 * Created on 2 of noviembre of 2001, 19:15
 */
package org.jscience.physics.fluids.dynamics;

/**
 * This interface defines methods to be implemented by an EIS (Entorno
 * Integrado of Simulacion / Integrated Simulation Environment) to show
 * information of the ADFC 2.1.
 *
 * @author balrog
 */
public interface ProtocoleEIS {
    /**
     * DOCUMENT ME!
     */
    public void initiate();

    /**
     * DOCUMENT ME!
     *
     * @param texto DOCUMENT ME!
     */
    public void warning(String texto);

    /**
     * DOCUMENT ME!
     *
     * @param texto DOCUMENT ME!
     */
    public void error(String texto);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean continueSerializedSimulation();

    /**
     * DOCUMENT ME!
     *
     * @param html DOCUMENT ME!
     */
    public void outHTML(String html);

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setEndingTime(String str);

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     */
    public void setStartupTime(String str);

    /**
     * DOCUMENT ME!
     *
     * @param paso DOCUMENT ME!
     * @param iter DOCUMENT ME!
     */
    public void setActualStep(int paso, int iter);

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setCalculationProgress(int p);

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     */
    public void setLoadProgress(int p);
}
