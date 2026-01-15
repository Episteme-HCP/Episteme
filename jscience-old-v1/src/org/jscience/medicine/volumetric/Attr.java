/*
 *      @(#)Attr.java 1.5 00/09/20 15:47:50
 *
 * Copyright (c) 1996-2000 Sun Microsystems, Inc. All Rights Reserved.
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


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
//author: Doug Gehringer
//email:Doug.Gehringer@sun.com
import java.text.NumberFormat;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class Attr {
    /** DOCUMENT ME! */
    static protected NumberFormat numFormat;

    static {
        numFormat = NumberFormat.getInstance();
        numFormat.setMaximumFractionDigits(5);
    }

    /** DOCUMENT ME! */
    String label;

    /** DOCUMENT ME! */
    String name;

    /** DOCUMENT ME! */
    AttrComponent component = null;

/**
     * Creates a new Attr object.
     *
     * @param label DOCUMENT ME!
     */
    Attr(String label) {
        this.label = label;
        this.name = toName(label);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLabel() {
        return label;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     */
    void setComponent(AttrComponent component) {
        this.component = component;
    }

    /**
     * DOCUMENT ME!
     */
    void updateComponent() {
        if (component != null) {
            component.update();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param label DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String toName(String label) {
        return label.replace(' ', '_');
    }

    /**
     * DOCUMENT ME!
     */
    abstract void reset();

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    abstract void set(String value);
}
