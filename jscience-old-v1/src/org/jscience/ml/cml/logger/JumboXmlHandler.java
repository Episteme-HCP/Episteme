package org.jscience.ml.cml.logger;

import org.jscience.ml.cml.AbstractBase;
import org.jscience.ml.cml.AbstractCMLDocument;
import org.jscience.ml.cml.cmlimpl.DocumentFactoryImpl;
import org.jscience.ml.cml.util.PMRDOMUtils;

import org.w3c.dom.Element;

import java.io.FileWriter;
import java.io.IOException;

import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


/**
 * allows logging of JUMBO/CML-based XML DOMs as well as messages.
 */
public class JumboXmlHandler extends Handler {
    /** DOCUMENT ME! */
    AbstractCMLDocument doc;

    /** DOCUMENT ME! */
    Element root;

    /** DOCUMENT ME! */
    String filename = "dummy.xml";

    /** DOCUMENT ME! */
    ErrorManager errorManager;

/**
     * Creates a new JumboXmlHandler object.
     *
     * @param filename DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public JumboXmlHandler(String filename) throws IOException {
        super();
        init();

        try {
            doc = DocumentFactoryImpl.createNewDocument();
        } catch (Exception e) {
            System.err.println("Cannot initialize JUMBOXMLHandler: " + e);
            errorManager.error("Parser bug: ", e, ErrorManager.OPEN_FAILURE);
        }

        root = doc.createElement("log");
        doc.appendChild(root);
        this.filename = filename;
    }

/**
     * Creates a new JumboXmlHandler object.
     */
    public JumboXmlHandler() {
        super();
        init();
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        errorManager = new ErrorManager();
        this.setErrorManager(errorManager);
    }

    /**
     * DOCUMENT ME!
     *
     * @param record DOCUMENT ME!
     */
    public void publish(LogRecord record) {
        Element elem = (Element) root.appendChild(doc.createElement("record"));
        elem.setAttribute("level", record.getLevel().getName());
        elem.setAttribute("msg", record.getMessage());

        Object[] params = record.getParameters();

        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof AbstractBase) {
                    AbstractBase clone = (AbstractBase) ((AbstractBase) params[i]).deepCopy(doc);
                    elem.appendChild(clone);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void flush() {
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        try {
            System.out.println("WRITING TO: " + filename);

            FileWriter fw = new FileWriter(filename);
            PMRDOMUtils.output(doc, fw);
            fw.close();
        } catch (IOException ioe) {
            errorManager.error("Close bug: ", ioe, ErrorManager.CLOSE_FAILURE);
        }
    }
}
