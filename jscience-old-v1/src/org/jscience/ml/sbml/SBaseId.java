package org.jscience.ml.sbml;

/**
 * Name and id for an sbml node. This code is licensed under the DARPA
 * BioCOMP Open Source License.  See LICENSE for more details.
 *
 * @author Nicholas Allen
 */
public abstract class SBaseId extends SBase {
    /** DOCUMENT ME! */
    protected String id;

    /** DOCUMENT ME! */
    protected String name;

/**
     * Creates a new SBaseId object.
     *
     * @param id   DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public SBaseId(String id, String name) {
        if (id != null) {
            setId(id);
        }

        if (name != null) {
            setName(name);
        }
    }

/**
     * Creates a new SBaseId object.
     */
    public SBaseId() {
        this(null, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return (name == null) ? id : name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }
}
