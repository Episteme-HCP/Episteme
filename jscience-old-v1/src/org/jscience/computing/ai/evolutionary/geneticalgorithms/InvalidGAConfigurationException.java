package org.jscience.computing.ai.evolutionary.geneticalgorithms;

/**
 * Exception thrown when an invalid configuration of genetic algorithm is tried
 * to be used.
 * <p/>
 * For example if you extend a genetic-algorithm class like BinaryCodedGa and
 * you forgot to overwrite evaluateIndividual(...) function, this exception is
 * thrown from the BinaryCodedGa class.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class InvalidGAConfigurationException extends RuntimeException {
    /**
     * Constructs a new InvalidGAConfigurationException instance with the
     * given error message.
     *
     * @param message An error message describing the reason this exception is being thrown.
     */
    public InvalidGAConfigurationException(String message) {
        super(message);
    }
}
