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

package org.jscience.linguistics.braille;

/**
 * The interface defining Java text/Braille translation classes that implement
 * the UMIST, Manchester, UK translation algorithm.
 * <P>Classes implementing <CODE>BrailleLanguage</CODE> should use the same global
 * constants for the language information.
 * <p/>
 * <p><small>Copyright 1999, 2004 Alasdair King. This program is free software
 * under the terms of the GNU General Public License. </small>
 *
 * @author Alasdair King, alasdairking@yahoo.co.uk
 * @version 1.0 09/01/2001
 * @see "<strong>Blenkhorn, P.</strong> A System for Converting Braille into Print, <em>IEEE Transactions on Rehabilitation Engineering</em>, Vol 3, No 2, June 1995."
 * @see <A HREF="http://personalpages.umist.ac.uk/staff/A.King/papers/KingEvansBlenkhorn_JavaBraille_2004.htm">A platform-independent Braille translator</A>
 */
public interface BrailleLanguage {
    /**
     * Defines the value of the bit that must be set to indicate a
     * <EM>wildcard</EM> character in any language.
     */
    static final int WILDCARD_FLAG = 64;

    /**
     * Defines the value of the bit that must be set to indicate a
     * whitespace character.
     */
    static final int SPACE_FLAG = 8;

    /**
     * Information for a wildcard definition - wildcards of type
     * WILDCARD_NONE must must match zero or more of its type in the input
     * text.
     */
    static final int WILDCARD_NONE = 1;

    /**
     * Information for a wildcard definition - wildcards of type
     * WILDCARD_NONE must must match exactly one its type in the input text.
     */
    static final int WILDCARD_ONE = 2;

    /**
     * Information for a wildcard definition - wildcards of type
     * WILDCARD_NONE must may match one or more of its type in the input text.
     */
    static final int WILDCARD_SEVERAL = 3;

    /** The maximum size of a single translation rule in characters. */
    static final int RULE_BUFFER = 128;

    /**
     * The ASCII/Unicode character used to denote the end of the left
     * context and the start of the focus in a translation rule.
     */
    static final char LEFT_FOCUS_DELIMITER = '[';

    /**
     * The ASCII/Unicode character used to denote the end of the focus
     * and the start of the right context in a translation rule.
     */
    static final char RIGHT_FOCUS_DELIMITER = ']';

    /**
     * The ASCII/Unicode character used to denote the end of the right
     * context and the start of the rule output in a translation rule.
     */
    static final char RULE_OUTPUT_DELIMITER = '=';

    /**
     * The value used to denote the end of the output in a translation
     * rule - only for legacy 256-character-set languages.
     */
    static final char RULE_CONTENT_DELIMITER = 0;

    /**
     * Character denoting the end of the list of translation rules in a
     * language rules table in a 256-character BrailleLanguage system.
     */
    static final char TABLE_DELIMITER = '#';

    /**
     * The filename extension that indicates the BrailleLanguage data
     * file for this BrailleLanguage, imported when the BrailleLanguage is
     * instantiated.
     */
    static String FILENAME_EXTENSION = "";

    /** Used to delimit the filename extension from the main filename body. */
    static final char FILE_EXTENSION_DELIMITER = '.';

    /**
     * The filename extension, including the dot, that indicates the
     * BrailleLanguage definition tables in their editable human-readable
     * plain text form for this language.
     */
    static String DATAFILE_EXTENSION = "";

    /**
     * Sets the state of the finite state machine performing the
     * translation, and therefore controls the type of translation performed,
     * depending on the translation language selected.
     *
     * @param state An <CODE>int</CODE> containing the new state for the
     *        machine to take.
     *
     * @return A <CODE>boolean</CODE> indicating whether setting the state was
     *         successful.  It will fail if the new state requested is outside
     *         the limits of states for the language being used.
     */
    boolean setState(int state);

    /**
     * Returns the states permitted in this language.  This will vary
     * from one language to the next.
     *
     * @return An <CODE>int</CODE>, the number of states defined for the
     *         language and hence the number of permitted states.  The state
     *         can be any integer from 1 to <CODE>numberStates</CODE>.
     */
    int getPermittedStates();

    /**
     * Returns the current state of the virtual machine.
     *
     * @return The current <CODE>int</CODE> state of the virtual machine, in
     *         the range 1 to <CODE>numberStates</CODE>.
     */
    int getState();

    /**
     * Performs translation from text to Braille or from Braille to
     * text, depending on language and state.
     *
     * @param toConvert <CODE>String</CODE> holding characters to translate.
     *
     * @return A <CODE>String</CODE> holding the output text, characters now
     *         translated.
     */
    String translate(String toConvert);

    /**
     * Performs translation from text to Braille or from Braille to
     * text, depending on language and state.
     *
     * @param toConvert <CODE>int[]</CODE> holding characters to translate.
     *
     * @return <CODE>int[]</CODE> holding the output text, characters now
     *         translated.
     */
    int[] translate(int[] toConvert);
}
