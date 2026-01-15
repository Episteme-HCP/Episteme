/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util;

/**
 * An empty tagging interface to be implemented by all classes that use native
 * function calls (yikes).
 * <p/>
 * <p/>
 * To run these implementing classes, you need to ensure that the platform
 * dependent library specified in the implementing class is available in the
 * <dfn>java.library.path</dfn> or (as this is inherently system dependent) in
 * the windows class path or something.
 * </p>
 *
 * @author Holger Antelmann
 * @since 5/14/2002
 */
public interface NativeCode {
}
