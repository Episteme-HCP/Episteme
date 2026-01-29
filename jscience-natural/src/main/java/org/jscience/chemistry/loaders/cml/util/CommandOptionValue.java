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

package org.jscience.chemistry.loaders.cml.util;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Represents a descriptor for an allowed value for a CommandOption.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class CommandOptionValue {
    /** Logger for this class. */
    static Logger logger = Logger.getLogger(CommandOptionValue.class.getName());

    /** Convenience constant for Level.FINE. */
    static Level MYFINE = Level.FINE;

    /** Convenience constant for Level.FINEST. */
    static Level MYFINEST = Level.FINEST;

    static {
        logger.setLevel(Level.INFO);
    }

    /** The name/string of this option value. */
    String name;

    /** A description of what this value signifies. */
    String desc;

    /**
     * Creates a new CommandOptionValue object.
     *
     * @param name the literal value name
     * @param desc the description of this value
     */
    public CommandOptionValue(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    /**
     * Returns a string representing the usage documentation for this value.
     *
     * @return the usage string
     */
    public String usage() {
        return CommandOptions.BLANK.substring(0, 20) +
        (name + CommandOptions.BLANK).substring(0, 15) + "//" + desc;
    }
}
