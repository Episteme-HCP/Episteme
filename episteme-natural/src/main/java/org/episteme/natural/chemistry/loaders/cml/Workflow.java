/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.natural.chemistry.loaders.cml;

/**
 * represents workflow
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public interface Workflow {
    /**
     * Runs the workflow.
     *
     * @throws CMLException if the workflow fails to execute
     */
    void run() throws CMLException;

    /**
     * Gets the CMLDocument for object creation.
     *
     * @return the associated CML document
     */
    AbstractCMLDocument getCMLDocument();

/**
     * store object by name.
     */

    //    public void putObject(String name, Object object) throws CMLException;
/**
     * get object by name.
     */

    //    public Object getObject(String name);
/**
     * substitute variables in string. e.g. if bar==abc and plugh==qqq
     * foo${bar}zz${plugh}yy ==> fooabczzqqqyy not recursive
     */

    //    public String substituteVariables(String value) throws CMLException;
/**
     * process command
     */

    //    public void process(Node node) throws CMLException;
}

