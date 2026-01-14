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

package org.jscience.ml.cml.dom.pmr;

import org.w3c.dom.Node;
import org.w3c.dom.UserDataHandler;

/**
 * When associating an object to a key on a node using Node.setUserData()
 * the application can provide a handler that gets called when the node the object
 * is associated to is being cloned, imported, or renamed.
 * This can be used by the application to implement various behaviors regarding
 * the data it associates to the DOM nodes. This interface defines that handler.
 */
public class PMRUserDataHandlerImpl implements UserDataHandler {

    /**
     * This method is called whenever the node for which this handler is registered is imported or cloned.
     * <p/>
     * This method is called whenever the node for which this handler is registered is imported or cloned.
     * DOM applications must not raise exceptions in a UserDataHandler. The effect of throwing exceptions from the handler is DOM implementation dependent.
     *
     * @param operation Specifies the type of operation that is being performed on the node.
     * @param key       Specifies the key for which this handler is being called.
     * @param data      Specifies the data for which this handler is being called.
     * @param src       Specifies the node being cloned, adopted, imported, or renamed. This is null when the node is being deleted.
     * @param dst       Specifies the node newly created if any, or null.
     */
    public void handle(short operation, String key, Object data, Node src, Node dst) {
        dst = dst.cloneNode(true);
    }

}
