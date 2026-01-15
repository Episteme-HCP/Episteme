package org.jscience.computing.ai.evolutionary.geneticalgorithms;

/**
 * Exception thrown when an invalid fitness value is tried to be used
 * <p/>
 * Default implementation throws this exception when the fitness value has
 * a negative value in these cases. You should correct your fitness function
 * for returning positive values.
 * <p/>
 * Custom implementations of genetic algorithms may use other (problem spceific)
 * criterias to throw this exception.
 * <p/>
 * If fitness function returns negative values, then selectChromosome()
 * method may return a negative index for individual. To prevent
 * ArrayIndexOutOfBounds runtime exception, this exception is implemented.
 * Other solutions are welcome, if you have.
 *
 * @author Levent Bayindir
 * @version 0.1
 */
public class InvalidFitnessValueException extends Exception {
    /**
     * Constructs a new InvalidFitnessValueException instance with the
     * given error message.
     *
     * @param message an error message describing the reason of this exception
     */
    public InvalidFitnessValueException(String message) {
        super(message);
    }
}
