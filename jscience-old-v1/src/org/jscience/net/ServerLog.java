/*
* ---------------------------------------------------------
* Antelmann.com Java Framework by Holger Antelmann
* Copyright (c) 2005 Holger Antelmann <info@antelmann.com>
* For details, see also http://www.antelmann.com/developer/
* ---------------------------------------------------------
*/
package org.jscience.net;

import org.jscience.util.Filter;

import java.util.Enumeration;

/**
 * ServerLog represents a log from a web-server and provides access
 * to its content
 *
 * @author Holger Antelmann
 */
public interface ServerLog {
    /**
     * returns an Enumeration of all ServerLogEntry objects that are accepted
     * by the given filter or all ServerLogEntries if the filter is null.
     *
     * @return an Enumeration of ServerLogEntry objects;
     *         note that there may be null values in the Enumeration,
     *         if the entry couldn't be properly parsed.
     * @see ServerLogEntry
     */
    Enumeration<ServerLogEntry> getServerLogEntryEnumerator(Filter<ServerLogEntry> filter);
}
