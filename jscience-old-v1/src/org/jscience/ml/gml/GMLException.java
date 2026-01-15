/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml;

import org.jscience.ml.gml.xml.XMLException;


/**
 * Exception thrown in GML-related work.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public class GMLException extends XMLException {
/**
     * Creates a new GMLException object.
     *
     * @param msg DOCUMENT ME!
     */
    public GMLException(String msg) {
        super(msg);
    }
}
