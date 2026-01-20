package org.jscience.economics.money;

import org.jscience.util.UnavailableDataException;


//this code is based on Java Financial Library:
//http://www.neuro-tech.net/
//Luke Reeves <lreeves@member.fsf.org>
//and was originally released under the LGPL
/**
 * Interface for classes that will retrieve quotes from data sources.
 */
public interface QuoteSource {
    /**
     * Fetches a quote from this source.
     *
     * @param quote DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UnavailableDataException DOCUMENT ME!
     */
    public boolean fetch(Quote quote) throws UnavailableDataException;
}
