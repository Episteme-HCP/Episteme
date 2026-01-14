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

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * A no-frills implementation of TaxonFactory that builds an in-memory
 * Taxon tree.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
public class SimpleTaxonFactory implements TaxonFactory {
    /**
     * The TaxonFactory that the system should use for storing the
     * taxonomy used by swissprot and embl as in-memory objects.
     */
    public static final SimpleTaxonFactory GLOBAL = new SimpleTaxonFactory(
            "GLOBAL");

    /** DOCUMENT ME! */
    private Taxon root;

    /** DOCUMENT ME! */
    private String name;

    /** DOCUMENT ME! */
    private Map taxonBySciName = new HashMap();

/**
     * Creates a new SimpleTaxonFactory object.
     *
     * @param name DOCUMENT ME!
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public SimpleTaxonFactory(String name) {
        if ((name != null) && (name.length() > 0)) {
            this.name = name;
            this.root = createTaxon("ROOT", "");
        } else {
            throw new IllegalArgumentException(
                "The SimpleTaxonFactory constructor can't have null or empty arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Taxon getRoot() {
        return root;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param taxon DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Taxon importTaxon(Taxon taxon) {
        SimpleTaxon can;
        Iterator i;
        Taxon child;

        if (taxon != null) {
            can = canonicalize(taxon);

            if (can == null) {
                can = new SimpleTaxon(taxon.getScientificName(),
                        taxon.getCommonName());

                for (i = taxon.getChildren().iterator(); i.hasNext();) {
                    child = (Taxon) i.next();
                    addChild(can, child);
                }

                return can;
            } else {
                return can;
            }
        } else {
            throw new IllegalArgumentException("You can't import null Taxon.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param scientificName DOCUMENT ME!
     * @param commonName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Taxon createTaxon(String scientificName, String commonName) {
        Taxon taxon;

        taxon = new SimpleTaxon(scientificName, commonName);
        taxonBySciName.put(scientificName, taxon);

        return taxon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Taxon addChild(Taxon parent, Taxon child) {
        SimpleTaxon sparent;
        SimpleTaxon schild;

        if ((parent != null) && (child != null)) {
            if (canonicalize(parent) == null) {
                throw new IllegalArgumentException(
                    "Parent taxon not owned by this TaxonFactory");
            }

            sparent = (SimpleTaxon) parent;
            schild = (SimpleTaxon) importTaxon(child);

            if (sparent.children == null) {
                sparent.children = Collections.EMPTY_SET;
            }

            sparent.children.add(schild);
            schild.setParent(sparent);

            return schild;
        } else {
            throw new IllegalArgumentException(
                "You can't add with null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IllegalArgumentException DOCUMENT ME!
     */
    public Taxon removeChild(Taxon parent, Taxon child) {
        SimpleTaxon sparent;
        SimpleTaxon schild;

        if ((parent != null) && (child != null)) {
            sparent = canonicalize(parent);
            schild = canonicalize(child);

            if (sparent == null) {
                throw new IllegalArgumentException(
                    "Don't know about parent Taxon");
            }

            if ((schild != null) && (sparent.children != null) &&
                    (sparent.children.remove(schild))) {
                return schild;
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException(
                "You can't remove with null arguments.");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Taxon search(Object id) {
        return (Taxon) taxonBySciName.get(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param taxon DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SimpleTaxon canonicalize(Taxon taxon) {
        return (SimpleTaxon) search(taxon.getScientificName());
    }
}
