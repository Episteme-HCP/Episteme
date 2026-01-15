package org.jscience.biology.taxonomy;

import org.jscience.util.CircularReferenceException;

import java.util.Iterator;
import java.util.StringTokenizer;


/**
 * Encapsulate the 'EBI' species format used in Embl, Genbank and Swissprot
 * files.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
public class EbiFormat implements TaxonParser {
    /** DOCUMENT ME! */
    public static final String PROPERTY_NCBI_TAXON = EbiFormat.class +
        ":NCBI_TAXON";

    /** DOCUMENT ME! */
    private static EbiFormat INSTANCE = new EbiFormat();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static final EbiFormat getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EbiFormat();
        }

        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param taxonFactory DOCUMENT ME!
     * @param taxonString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CircularReferenceException DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Taxon parse(TaxonFactory taxonFactory, String taxonString)
        throws CircularReferenceException {
        String name;
        Taxon taxon;
        StringTokenizer sTok;
        String tok;
        Iterator i;
        Taxon child;

        if ((taxonFactory != null) && (taxonString != null) &&
                (taxonString.length() > 0)) {
            name = taxonString.trim();

            if (name.endsWith(".")) {
                name = name.substring(0, name.length() - 1);
            }

            taxon = taxonFactory.getRoot();
            sTok = new StringTokenizer(name, ";");

            if (sTok.countTokens() == 1) {
                return taxonFactory.addChild(taxon,
                    taxonFactory.createTaxon(name, null));
            }

            tok = null;
CLIMB_TREE: 
            while (sTok.hasMoreTokens()) {
                tok = sTok.nextToken().trim();

                for (i = taxon.getChildren().iterator(); i.hasNext();) {
                    child = (Taxon) i.next();

                    if (child.getScientificName().equals(tok)) {
                        taxon = child;

                        continue CLIMB_TREE; // found child by name - go through loop again
                    }
                }

                break; // couldn't find a child by than name - stop this and move on
            }

            for (; sTok.hasMoreTokens(); tok = sTok.nextToken().trim()) {
                taxon = taxonFactory.addChild(taxon,
                        taxonFactory.createTaxon(tok, null));
            }

            return taxon;
        } else {
            throw new IllegalArgumentException(
                "You can't parse null arguments (or empty String).");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param taxon DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String serialize(Taxon taxon) {
        String name;
        String sci;

        name = null;

        if (taxon != null) {
            do {
                sci = taxon.getScientificName();

                if (name == null) {
                    name = sci + ".";
                } else {
                    name = sci + "; " + name;
                }

                taxon = taxon.getParent();
            } while ((taxon != null) && (taxon.getParent() != null));
        }

        return name;
    }
}
