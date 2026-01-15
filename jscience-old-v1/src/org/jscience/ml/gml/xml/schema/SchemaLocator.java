/**
 * Name               Date          Change --------------     ----------
 * ---------------- amilanovic         29-Mar-2002   Updated for the new
 * package name.
 */
package org.jscience.ml.gml.xml.schema;

import java.io.InputStream;


/**
 * Locates and retrieves a schema from the specified location.
 *
 * @author Aleksandar Milanovic
 * @version 1.0
 */
public interface SchemaLocator {
    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLSchema retrieveSchemaFromURL(String url);

    /**
     * DOCUMENT ME!
     *
     * @param fileName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLSchema retrieveSchemaFromFile(String fileName);

    /**
     * DOCUMENT ME!
     *
     * @param schemaStream DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XMLSchema retrieveSchemaFromStream(InputStream schemaStream);
}
