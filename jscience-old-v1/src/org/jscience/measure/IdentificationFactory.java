package org.jscience.measure;

/**
 * A class representing a system to generate identifications
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface IdentificationFactory {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification generateIdentification();

    //public Identification generateIdentification(String identification);
    /**
     * DOCUMENT ME!
     *
     * @param identification DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Identification convertIdentification(Identification identification);
}
