package org.jscience.mathematics.analysis;

/**
 * This is the common interface to define a map or function. The Java language
 * doesn't provide us with a way to have only one interface for primitive
 * types and Objects as the same time. May be with Java 1.5 although I don't
 * know how... This empty interface is the mean by which we provide a way to
 * group all mappings.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//we provide support for ComplexMapping but we could also provide support for RationalMapping...
//we could also provide support for mapping between (finite) set elements, as ObjectMapping, etc.
public interface AbstractMapping {
}
