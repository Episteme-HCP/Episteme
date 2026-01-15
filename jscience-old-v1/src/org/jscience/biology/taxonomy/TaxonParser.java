package org.jscience.biology.taxonomy;

import org.jscience.util.CircularReferenceException;


/**
 * Encapsulate the mapping between Taxon and stringified representations of
 * taxa.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */
public interface TaxonParser {
    /**
     * Convert a stringified Taxon into a Taxon instance.
     *
     * @param taxonFactory the TaxonFactory used to instantiate taxa instances
     * @param taxonString the String to parse
     *
     * @return a Taxon instance created by the TaxonFactory from the
     *         taxonString
     *
     * @throws CircularReferenceException DOCUMENT ME!
     */
    public Taxon parse(TaxonFactory taxonFactory, String taxonString)
        throws CircularReferenceException;

    /**
     * Convert a Taxon into a stringified representation.
     *
     * @param taxon the Taxon to serialize
     *
     * @return the stringified version of Taxon
     */
    public String serialize(Taxon taxon);
}
