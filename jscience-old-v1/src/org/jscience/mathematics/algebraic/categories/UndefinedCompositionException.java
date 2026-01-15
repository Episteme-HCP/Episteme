package org.jscience.mathematics.algebraic.categories;

/**
 * This exception occurs when trying to compose two morphisms whose
 * composition is undefined.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class UndefinedCompositionException extends RuntimeException {
/**
     * Constructs a UndefinedCompositionException with no detail message.
     */
    public UndefinedCompositionException() {
    }

/**
     * Constructs a UndefinedCompositionException with the specified detail
     * message.
     *
     * @param s DOCUMENT ME!
     */
    public UndefinedCompositionException(String s) {
        super(s);
    }
}
