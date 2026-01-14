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

package org.jscience.biology.taxonomy;

import java.util.Vector;


/**
 * A taxonomic entity, represented by a scientific name and corresponding
 * to a particular taxonomic serial numer (tsn) from ITIS. See:
 * "http://sis.agr.gc.ca/itis" for details.
 */

//code from http://knb.ecoinformatics.org/software/
public class ItisTaxon extends SimpleTaxon {
    /** Identifier for this taxon, the taxonomic serial number */
    private long tsn;

    /** Taxonomic rank of the taxon */
    private String taxonRank;

    /** Author of the taxon */
    private String taxonAuthor;

    /** Parent of this taxon */
    private long parentTsn;

    /** List of children of this taxon (Vector contains Long tsn numbers) */
    private Vector childTsnList;

    /** List of vernacular names for this taxon */
    private Vector vernacularNames;

    /** List of synonyms of this taxon (Vector contains Long tsn numbers) */
    private Vector synonymTsnList;

    /** flag indicating if the full taxon record has been retrieved from ITIS */
    private boolean dataComplete;

    /**
     * flag indicating if the full child tsn list has been retrieved
     * from ITIS
     */
    private boolean hasChildList;

/**
     * construct an instance of the Taxon class, manually setting the
     * attributes instead of getting them from an XML stream
     */
    public ItisTaxon() {
        super(null, null);
        // Initialize the members
        childTsnList = new Vector();
        vernacularNames = new Vector();
        synonymTsnList = new Vector();
        dataComplete = false;
        hasChildList = false;
    }

/**
     * construct an instance of the Taxon class, manually setting the
     * attributes instead of getting them from an XML stream
     */
    public ItisTaxon(String scientificName, String commonName) {
        super(scientificName, commonName);
        // Initialize the members
        childTsnList = new Vector();
        vernacularNames = new Vector();
        synonymTsnList = new Vector();
        dataComplete = false;
        hasChildList = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //overridden
    public String getCommonName() {
        return (String) vernacularNames.get(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param commonName DOCUMENT ME!
     *
     * @throws UnsupportedOperationException DOCUMENT ME!
     */

    //overridden
    public void setCommonName(String commonName) {
        throw new UnsupportedOperationException("Please use addVernacularName.");
    }

    /**
     * Accessor method to return the tsn
     *
     * @return DOCUMENT ME!
     */
    public long getTsn() {
        return tsn;
    }

    /**
     * method to set the tsn of this taxon
     *
     * @param tsn DOCUMENT ME!
     */
    public void setTsn(long tsn) {
        this.tsn = tsn;
    }

    /**
     * Accessor method to return the taxon rank
     *
     * @return DOCUMENT ME!
     */
    public String getTaxonRank() {
        return taxonRank;
    }

    /**
     * method to set the taxon rank
     *
     * @param taxonRank DOCUMENT ME!
     */
    public void setTaxonRank(String taxonRank) {
        this.taxonRank = taxonRank;
    }

    /**
     * Accessor method to return the parent taxon
     *
     * @return DOCUMENT ME!
     */
    public long getParentTsn() {
        return parentTsn;
    }

    /**
     * method to set the parent taxon
     *
     * @param parentTsn DOCUMENT ME!
     */
    public void setParentTsn(long parentTsn) {
        this.parentTsn = parentTsn;
    }

    /**
     * Accessor method to return Vector of child TSNs
     *
     * @return DOCUMENT ME!
     */
    public Vector getChildTsn() {
        return childTsnList;
    }

    /**
     * method to add to the list of child TSNs
     *
     * @param childTsn DOCUMENT ME!
     */
    public void addChildTsn(long childTsn) {
        this.childTsnList.add(new Long(childTsn));
    }

    /**
     * Accessor method to return Vector of synonym TSNs
     *
     * @return DOCUMENT ME!
     */
    public Vector getSynonymTsn() {
        return synonymTsnList;
    }

    /**
     * method to add to the list of synonym TSNs
     *
     * @param synonymTsn DOCUMENT ME!
     */
    public void addSynonymTsn(long synonymTsn) {
        this.synonymTsnList.add(new Long(synonymTsn));
    }

    /**
     * Accessor method to return dataComplete flag
     *
     * @return DOCUMENT ME!
     */
    public boolean isDataComplete() {
        return dataComplete;
    }

    /**
     * method to set the dataComplete flag
     *
     * @param isComplete DOCUMENT ME!
     */
    public void setDataComplete(boolean isComplete) {
        this.dataComplete = isComplete;
    }

    /**
     * Accessor method to return hasChildList flag
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChildList() {
        return hasChildList;
    }

    /**
     * method to set the hasChildList flag
     *
     * @param hasChildList DOCUMENT ME!
     */
    public void setHasChildList(boolean hasChildList) {
        this.hasChildList = hasChildList;
    }

    /**
     * Accessor method to return Vector of vernacular names
     *
     * @return DOCUMENT ME!
     */
    public Vector getVernacularNames() {
        return vernacularNames;
    }

    /**
     * method to add to the list of vernacular names
     *
     * @param name DOCUMENT ME!
     */
    public void addVernacularName(String name) {
        this.vernacularNames.add(name);
    }

    /**
     * Accessor method to return the author name
     *
     * @return DOCUMENT ME!
     */
    public String getTaxonAuthor() {
        return taxonAuthor;
    }

    /**
     * method to set the author name
     *
     * @param taxonAuthor DOCUMENT ME!
     */
    public void setTaxonAuthor(String taxonAuthor) {
        this.taxonAuthor = taxonAuthor;
    }
}
