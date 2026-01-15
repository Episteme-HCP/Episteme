/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;


//import org.jscience.util.*;
/**
 * allows for several LogWriters to share the same formatting
 *
 * @author Holger Antelmann
 */
public interface LogEntryFormatter {
    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws LogException DOCUMENT ME!
     */
    Object format(LogEntry entry) throws LogException;
}
