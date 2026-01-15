package org.jscience.biology.taxonomy;

import java.util.Collections;
import java.util.Set;

/**
 * A no-frills implementation of Taxon.
 * @version 1.0
 * @author Silvere Martin-Michiellot
 */

/**
 * A no-frills implementatation of Taxon.
 * <p/>
 * <p>A TaxonFactory implementation will probably wish to sub-class
 * this and add package-private accessors for the parent and children
 * fields.</p>
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
//a enhanced version of this class could be build out of org.jscience.util.Tree
public class SimpleTaxon extends AbstractTaxon {
    protected Taxon parent;
    protected Set children;

    /**
     * Create a new instance with no parent, no children and given
     * scientific and common names.
     */
    public SimpleTaxon(String scientificName, String commonName) {
        super(scientificName, commonName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Taxon getParent() {
        return parent;
    }

    void setParent(Taxon parent) {
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRoot() {
        return parent != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLeaf() {
        return children == null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildren() {
        if (children != null) {
            return children;
        } else {
            return Collections.EMPTY_SET;
        }
    }
}
