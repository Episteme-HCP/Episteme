/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

// CandyFinder.java
//
//    senger@ebi.ac.uk
//    February 2001
//
package org.jscience.bibliography.candy;

import org.jscience.util.NestedException;

import java.beans.PropertyChangeListener;

/**
 * <p/>
 * This interface is a main entry point to a set of controlled
 * vocabularies.
 * </p>
 * <p/>
 * <p/>
 * The implementation is supposed to behave as a Java bean (usually an
 * invisible bean unless it implements some additional GUI methods
 * which are not defined in this interface).
 * </p>
 *
 * @author <A HREF="mailto:senger@ebi.ac.uk">Martin Senger</A>
 * @version $Id: CandyFinder.java,v 1.2 2007-10-21 17:37:41 virtualcall Exp $
 */
public interface CandyFinder extends PropertyChangeListener {
    /*************************************************************************
     *
     *    P r o p e r t i e s
     *
     *************************************************************************/
    /**
     * A default name of this (and any) finder.
     * It is used when no other name was given by the finder
     * implementation.
     */
    static final String DEFAULT_FINDER_NAME = "Default vocabulary finder";

    //
    // Property names
    //

    /**
     * <p/>
     * A property name.
     * </p>
     * <p/>
     * <p/>
     * Its value is of type {@link CandyVocabulary}.
     * It this property is set a given vocabulary becomes part of
     * this finder.
     * </p>
     */
    static final String PROP_VOCABULARY = CandyVocabulary.PROP_VOCABULARY;

    /**
     * ***********************************************************************
     * <p/>
     * It creates a connection to an object representing a vocabulary
     * finder, or/and it makes all necessary initialization steps
     * needed for further communication.
     * </p>
     * <p/>
     * <p/>
     * However, there should be no need to call this method
     * explicitly, the other methods should do it automatically before
     * they need to use the finder.
     * </p>
     *
     * @throws NestedException if the connection/initialization cannot
     *                         be established
     *                         ***********************************************************************
     */
    void connect() throws NestedException;

    /**
     * ***********************************************************************
     * It checks if a vocabulary finder object is available. The semantic of
     * <em>available</em>depends on the implementation.
     * ***********************************************************************
     */
    boolean isReady();

    /**
     * ***********************************************************************
     * It closes connection with the finder object. Implementations may
     * choose to use this method for freeing resources.
     * ***********************************************************************
     */
    void disconnect();

    /**
     * **********************************************************************
     * <p/>
     * It returns names of all vocabularies known to this vocabulary
     * finder. Any of the returned names can be later used in the method
     * {@link #getVocabularyByName getVocabularyByName}.
     * </p>
     *
     * @return a list of available vocabulary names
     * @throws NestedException if the finder fails to communicate
     *                         with its vocabularies
     *                         ***********************************************************************
     */
    String[] getAllVocabularyNames() throws NestedException;

    /**
     * **********************************************************************
     * It returns a selected vocabulary.
     *
     * @param name a name of a vocabulary to be returned
     * @return a selected vocabulary
     * @throws NestedException when the vocabulary cannot be found (likely the
     *                         given name is wrong)
     *                         ***********************************************************************
     * @see #getAllVocabularyNames
     */
    CandyVocabulary getVocabularyByName(String name) throws NestedException;

    /**
     * **********************************************************************
     * It returns all available vocabularies.
     *
     * @return all available vocabularies
     * @throws NestedException if the finder fails to communicate
     *                         with its vocabularies
     *                         ***********************************************************************
     */
    CandyVocabulary[] getAllVocabularies() throws NestedException;

    /**
     * **********************************************************************
     * It returns the number of available vocabularies.
     *
     * @throws NestedException if the finder fails to communicate
     *                         with its vocabularies
     *                         ***********************************************************************
     */
    int getNumCount() throws NestedException;

    //
    // Property access methods
    //

    /**
     * **********************************************************************
     * It returns a name of this vocabulary finder.
     *
     * @throws NestedException if the finder fails to return its name
     *                         ***********************************************************************
     */
    String getFinderName() throws NestedException;
}
