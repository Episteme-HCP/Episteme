package org.jscience.util;

/**
 * Interface for cloneable classes. A cloneable class implements the standard
 * {@link java.lang.Cloneable} interface from J2SE and additionnaly overrides
 * the {@link Object#clone()} method with public access. For some reason lost
 * in the mists of time, the J2SE's {@link java.lang.Cloneable} interface
 * doesn't declare the <code>clone()</code> method, which make it hard to use.
 * This <code>Cloneable</code> interface add this missing method, which avoid
 * the need to cast an interface to its implementation in order to clone it.
 *
 * @see java.lang.Cloneable
 * @see <A
 *      HREF="http://developer.java.sun.com/developer/bugParade/bugs/4098033.html">Cloneable
 *      doesn't define <code>clone()</code></A> on Sun's bug parade
 */

//copied after Geotools (under LGPL)
public interface Cloneable extends java.lang.Cloneable {
    /**
     * Creates and returns a copy of this object. The precise meaning
     * of "copy" may depend on the class of the object.
     *
     * @return A clone of this instance.
     *
     * @see Object#clone
     */
    public Object clone();
}
