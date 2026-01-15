// Interface for RotatePanel to allow it interact with the scene
// Did it as an interface incase we want to rotate other things
// later rather than just Sky3d.
package org.jscience.tests.astronomy.milkyway.hipparcos;


//this package is rebundled from Hipparcos Java package from
//William O'Mullane
//http://astro.estec.esa.nl/Hipparcos/hipparcos_java.html
//mailto:hipparcos@astro.estec.esa.nl
import javax.media.j3d.RotationInterpolator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
interface RotateAble {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    RotationInterpolator getRotationInterpolator();
}
;
