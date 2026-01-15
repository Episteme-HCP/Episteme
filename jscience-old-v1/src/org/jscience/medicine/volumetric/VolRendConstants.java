/*
 *      @(#)VolRendConstants.java 1.2 01/09/04 15:51:07
 *
 * Copyright (c) 1996-1999 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 *
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 *
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */
package org.jscience.medicine.volumetric;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
abstract public interface VolRendConstants {
    /** DOCUMENT ME! */
    static final int X_AXIS = 0;

    /** DOCUMENT ME! */
    static final int Y_AXIS = 1;

    /** DOCUMENT ME! */
    static final int Z_AXIS = 2;

    /** DOCUMENT ME! */
    static final int FRONT = 0;

    /** DOCUMENT ME! */
    static final int BACK = 1;

    /** DOCUMENT ME! */
    static final int PLUS_X = 0;

    /** DOCUMENT ME! */
    static final int PLUS_Y = 1;

    /** DOCUMENT ME! */
    static final int PLUS_Z = 2;

    /** DOCUMENT ME! */
    static final int MINUS_X = 3;

    /** DOCUMENT ME! */
    static final int MINUS_Y = 4;

    /** DOCUMENT ME! */
    static final int MINUS_Z = 5;
}
