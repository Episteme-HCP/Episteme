/*
 * $Id: OMAttributionBeanInfo.java,v 1.3 2007-10-23 18:21:20 virtualcall Exp $
 *
 * Copyright (c) 2001-2004, RIACA, Technische Universiteit Eindhoven (TU/e).
 * All Rights Reserved.
 *
 * ---------------------------------------------------------------------------
 *
 *  The contents of this file are subject to the RIACA Public License
 *  Version 1.0 (the "License"). You may not use this file except in
 *  compliance with the License. A copy of the License is available at
 *  http://www.riaca.win.tue.nl
 *
 *  Alternatively, the contents of this file may be used under the terms
 *  of the GNU Lesser General Public license (the "LGPL license"), in which
 *  case the provisions of the LGPL license are applicable instead of those
 *  above. A copy of the LGPL license is available at http://www.gnu.org
 *
 *  The Original Code is ROML -- the RIACA OpenMath Library. The Initial
 *  Developer of the Original Code is Manfred N. Riem.  Portions created
 *  by Manfred N. Riem are Copyright (c) 2001. All Rights Reserved.
 *
 *  Contributor(s):
 *
 *      Ernesto Reinaldo Barreiro, Arjeh M. Cohen, Hans Cuypers, Hans Sterk,
 *      Olga Caprotti
 *
 * ---------------------------------------------------------------------------
 */
package org.jscience.ml.openmath;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;


/**
 * Bean info class for the OMAttribution class.<p></p>
 *
 * @author Manfred N. Riem (mriem@manorrock.org)
 * @version $Revision: 1.3 $
 */
public class OMAttributionBeanInfo extends SimpleBeanInfo {
    /**
     * Get the property descriptors.<p></p>
     *
     * @return the array of property descriptors.
     *
     * @throws Error DOCUMENT ME!
     */
    public PropertyDescriptor[] getPropertyDescriptors() {
        try {
            PropertyDescriptor constructor = new PropertyDescriptor("constructor",
                    OMAttribution.class);
            PropertyDescriptor attributions = new PropertyDescriptor("attributions",
                    OMAttribution.class);

            PropertyDescriptor[] array = { constructor, attributions };

            return array;
        } catch (IntrospectionException exception) {
            throw new Error(exception.toString());
        }
    }
}
