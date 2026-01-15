package org.jscience.ml.cml;

/**
 * CML-Specific exceptions may be thrown by CML-DOM classes and interfaces
 * <p/>
 * There is no systematised set of error messages yet.
 */
public class CMLException extends Exception {

    public final static String NYI = "Not yet Implemented";

    public CMLException() {
        super();
    }

    public CMLException(String s) {
        super(s);
    }
}
