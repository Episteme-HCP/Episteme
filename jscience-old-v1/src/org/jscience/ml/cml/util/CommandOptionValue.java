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

package org.jscience.ml.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CommandOptionValue {
    /** DOCUMENT ME! */
    static Logger logger = Logger.getLogger(CommandOptionValue.class.getName());

    /** DOCUMENT ME! */
    static Level MYFINE = Level.FINE;

    /** DOCUMENT ME! */
    static Level MYFINEST = Level.FINEST;

    static {
        logger.setLevel(Level.INFO);
    }

    /** DOCUMENT ME! */
    String name;

    /** DOCUMENT ME! */
    String desc;

/**
     * Creates a new CommandOptionValue object.
     *
     * @param name DOCUMENT ME!
     * @param desc DOCUMENT ME!
     */
    public CommandOptionValue(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String usage() {
        return CommandOptions.BLANK.substring(0, 20) +
        (name + CommandOptions.BLANK).substring(0, 15) + "//" + desc;
    }
}
