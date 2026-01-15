/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer
* ---------------------------------------------------------
*/
package org.jscience.util.logging;

import org.jscience.io.IOUtils;

import java.io.IOException;
import java.io.Serializable;


/**
 * formats the LogEntry as a serialized byte array.
 *
 * @author Holger Antelmann
 *
 * @see org.jscience.io.IOUtils#deserialize(byte[])
 */
public class BinaryLogFormatter implements LogEntryFormatter {
    /**
     * DOCUMENT ME!
     *
     * @param entry DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws LogException DOCUMENT ME!
     */
    public Object format(LogEntry entry) {
        try {
            return IOUtils.serialize(entry);
        } catch (IOException ex) {
            Object[] param = entry.getParameters();

            for (int i = 0; i < param.length; i++) {
                if (!(param[i] instanceof Serializable)) {
                    param[i] = param[i].toString();
                }
            }

            try {
                return IOUtils.serialize(entry);
            } catch (IOException ioex) {
                throw new LogException("cannot serialize LogEntry", ioex);
            }
        }
    }
}
