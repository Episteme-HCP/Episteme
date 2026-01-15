package org.jscience.ml.cml.util;

/**
 * MANAGES ERRORS
 */
public class PMRError {
    /**
     * writes error message to System.err and calls System.exit(1)
     *
     * @param message to be output
     */
    public static void fatalError(String message) {
        System.err.println("FATAL: " + message);

        System.exit(1);
    }
}
