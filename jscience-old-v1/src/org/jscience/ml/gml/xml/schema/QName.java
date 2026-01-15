/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   First version.
 */
package org.jscience.ml.gml.xml.schema;

import java.util.Hashtable;


/**
 * Represents a qualified name, pair of (namespace, name).
 *
 * @version 1.0
 */
public class QName {
    /** DOCUMENT ME! */
    private static Hashtable qnames_ = new Hashtable();

    /** DOCUMENT ME! */
    private String nsUri_;

    /** DOCUMENT ME! */
    private String localName_;

/**
     * Creates a new QName object.
     *
     * @param nsUri     DOCUMENT ME!
     * @param localName DOCUMENT ME!
     */
    private QName(String nsUri, String localName) {
        nsUri_ = nsUri;
        localName_ = localName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNsUri() {
        return nsUri_;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLocalName() {
        return localName_;
    }

    /**
     * DOCUMENT ME!
     *
     * @param other DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object other) {
        boolean result = false;

        if (other instanceof QName) {
            QName otherQName = (QName) other;
            result = getLocalName().equals(otherQName.getLocalName()) &&
                getNsUri().equals(otherQName.getNsUri());
        }

        return result;
    }

    /**
     * Returns an existing or creates a new QName object. This method
     * ensures that QNames can be used in Hashtables.
     *
     * @param nsUri DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static QName getQName(String nsUri, String localName) {
        Hashtable allQNamesInNs = (Hashtable) qnames_.get(nsUri);

        if (allQNamesInNs == null) {
            allQNamesInNs = new Hashtable();
            qnames_.put(nsUri, allQNamesInNs);
        }

        QName qname = (QName) allQNamesInNs.get(localName);

        if (qname != null) {
            // IMPORTANT: Returning an immutable QName object
            // This object can be used in Hashtables!!!
            return qname;
        }

        // new QName
        qname = new QName(nsUri, localName);
        allQNamesInNs.put(localName, qname);

        return qname;
    }
}
