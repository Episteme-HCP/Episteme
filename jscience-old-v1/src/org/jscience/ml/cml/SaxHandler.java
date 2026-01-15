package org.jscience.ml.cml;

import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

import java.util.Vector;


/**
 * manages callbacks from SAX2 handler
 */
public interface SaxHandler extends ContentHandler, DTDHandler, EntityResolver,
    ErrorHandler {
    /**
     * DOCUMENT ME!
     *
     * @param ignoreWhite DOCUMENT ME!
     */
    public void setIgnoreWhite(boolean ignoreWhite);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasErrors();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Vector getErrorVector();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getDebug();
}
