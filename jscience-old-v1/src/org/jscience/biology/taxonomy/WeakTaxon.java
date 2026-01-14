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

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Set;


/**
 * <p>An implementation of Taxon that keeps only weak references to
 * children, but full references to parents.</p>
 * <p/>
 * <p>This may be suitable for deriving memory-savy implementations
 * of TaxonFactory.</p>
 * <p/>
 * <p>To manipulate the children set, use the getChildrenRaw and
 * setChildrenRaw methods. These 'box' the actual weak reference, but
 * recognize null to mean that there are no children currently
 * known. A code-fragment may wish to do something like this:</p>
 * <p/>
 * <pre><code>
 * Set children = weakTaxon.getChildrenRaw();
 * if(children == null) {
 *   children = new HashSet();
 *   weakTaxon.setChildrenRaw(children);
 * }
 * // do stuff to update child set e.g. add children
 * </code></pre>
 * </p>
 *
 * @author Matthew Pocock
 */

//strongly inspired and enhanced from Biojava, http://www.biojava.org, original code under Lesser GPL
public class WeakTaxon extends AbstractTaxon {
    protected Taxon parent;
    private WeakReference /*Set*/ children;

    /**
     * Creates a new WeakTaxon object.
     *
     * @param scientificName DOCUMENT ME!
     * @param commonName     DOCUMENT ME!
     */
    public WeakTaxon(String scientificName, String commonName) {
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
        Set c;

        c = getChildrenRaw();

        if (c != null) {
            return c;
        } else {
            return Collections.EMPTY_SET;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getChildrenRaw() {
        Set c;

        if (children != null) {
            c = (Set) children.get();

            if (c != null) {
                return c;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     */
    public void setChildrenRaw(Set children) {
        this.children = new WeakReference(children);
    }
}
