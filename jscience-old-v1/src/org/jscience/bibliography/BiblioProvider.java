// BiblioProvider.java
//
//    senger@ebi.ac.uk
//    March 2001
//
package org.jscience.bibliography;

import embl.ebi.utils.ReflectUtils;

import java.util.Hashtable;


/**
 * Represents a provider. The provider is an active participant of the process
 * of creation and dissemination of the bibliographic resources. The most
 * obvious examples are authors, but it includes also publishers and other
 * contributors. The participants can be people, organisations, or even
 * software services.
 * <p/>
 * <p/>
 * It is a CORBA-independent container of members, without any methods. Some
 * attributes can be <tt>null</tt> if they are not supported by the
 * repository.
 * </p>
 * <p/>
 * <P></p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: BiblioProvider.java,v 1.2 2007-10-21 17:37:42 virtualcall Exp $
 */
public class BiblioProvider {
    /**
     * General provider properties. Use sub-classes to get type-specific
     * properties.
     */
    public Hashtable properties = new Hashtable();

    /**
     * A bit of "pretty" formatting. It even tries to make some indentations
     * :-)
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ReflectUtils.formatPublicFields(this);
    }
}
