package org.jscience.ml.cml;

import java.io.IOException;
import java.io.Writer;

/**
 * Adds i/o control
 *
 * @author P.Murray-Rust, 2003
 */
public interface CMLNode {

    /**
     * output CML variants
     */
    public final static String ARRAY = "ARRAY";
    public final static String CML1 = "CML1";
    public final static String CML2 = "CML2";

    /**
     * set CML version
     * <p/>
     * 1 or 2 at present (default 2)
     *
     * @param v version
     * @throws unsupported version
     */
    void setVersion(String v) throws CMLException;

    /**
     * set array syntax
     * <p/>
     * set CML array syntax (default false)
     *
     * @param syntax
     */
    void setArraySyntax(boolean syntax);

    /**
     * write XML (allows for syntactic variants)
     *
     * @param control applications-specific, for example "CML1+array" or "CML2"
     * @param w       output
     * @throws IOException
     * @throws org.jscience.ml.cml.CMLException
     *
     */

    void writeXML(Writer w, String control) throws org.jscience.ml.cml.CMLException, IOException;

    /**
     * write XML (uses default control)
     *
     * @param w output
     */
    void writeXML(Writer w) throws org.jscience.ml.cml.CMLException, IOException;

}
