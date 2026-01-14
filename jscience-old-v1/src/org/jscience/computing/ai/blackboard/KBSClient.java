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

/* KBSClient.java */
package org.jscience.computing.ai.blackboard;

import org.jscience.computing.ai.blackboard.util.SymbolTable;
import org.jscience.computing.ai.blackboard.util.ValuePair;

import java.util.Hashtable;


/**
 * KBSClient interface.  To communicate with a KBS, an application must
 * implement the methods defined by this interface.  The methods provide a
 * standard communication interface between a client application and KBS.
 *
 * @version:  1.3, 04/26/96 
 * @author:   Paul Brown
 */

// The author allowed us to redistribute this program under GPL:
// name: Alun Preece
// email: A.D.Preece@cs.cardiff.ac.uk
// website: http://www.csd.abdn.ac.uk/~apreece/pbf/ or http://users.cs.cf.ac.uk/A.D.Preece/
public interface KBSClient {
    /**
     * Reads a value from the client.  The value may be any object
     * subclass.
     *
     * @param args valid return value specification
     *
     * @return the read item
     */
    public Object read(ValuePair[] args);

    /**
     * Writes output to the client (may contain variable references).
     *
     * @param args output specification
     * @param bindings a collection of variable bindings
     * @param symbols the system symbol table, used for output translation
     */
    public void write(ValuePair[] args, Hashtable bindings, SymbolTable symbols);

    /**
     * Writes output to the client and requests a return value,
     * basically a combination of the read and write methods.
     *
     * @param args output and valid return value specifications
     * @param bindings a collection of variable bindings
     * @param symbols the system symbol table, used for input/output
     *        translation
     *
     * @return the read item
     */
    public Object query(ValuePair[] args, Hashtable bindings,
        SymbolTable symbols);
}
