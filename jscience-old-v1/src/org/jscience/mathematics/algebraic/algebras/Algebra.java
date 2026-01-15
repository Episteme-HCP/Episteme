package org.jscience.mathematics.algebraic.algebras;

import org.jscience.mathematics.algebraic.fields.Ring;
import org.jscience.mathematics.algebraic.modules.VectorSpace;


/**
 * This interface defines an algebra.
 *
 * @author Mark Hale
 * @version 1.0
 */

//this is only linear algebra (or field algebra) here.
//we should have a parent interface with empty contents
//see http://en.wikipedia.org/wiki/Algebra_over_a_field
//http://en.wikipedia.org/wiki/Abstract_algebra
//http://en.wikipedia.org/wiki/Algebra
//http://en.wikipedia.org/wiki/Linear_algebra
public interface Algebra {
/**
     * This interface defines a member of an algebra.
     */
    interface Member extends VectorSpace.Member, Ring.Member {
    }
}
