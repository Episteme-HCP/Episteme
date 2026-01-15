/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.io;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Writer;


/**
 * A class that implmements XMLSerializable can be written to an external
 * target and later reinitialized using that target as a source. As the data
 * is written in XML format, the exported data can be embedded in XML files
 * that also contain other data.
 *
 * @author Holger Antelmann
 */
public interface XMLSerializable {
    /**
     * exports the enire object as XML and writes it to the given
     * Writer, so that the object can be reconstructed with
     * <code>importXML(InputSource)</code>. The given Writer is neither
     * flushed nor closed, so that other data may be effectively written to
     * the InputSource after the method returned.
     *
     * @see #importXML(InputSource)
     */
    void exportXML(Writer out) throws IOException;

    /**
     * The object is fully reinitialized with the XML data contained in
     * the given ImputSource, so that the object has the same state as it had
     * during export.
     *
     * @see #exportXML(Writer)
     */
    void importXML(InputSource source) throws SAXException, IOException;
}
