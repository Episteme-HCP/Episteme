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

package org.jscience.util;

/**
 * Useful base-class for objects implementing Changeable
 *
 * @author Matthew Pocock
 * @author Thomas Down
 */
public abstract class AbstractChangeable implements Changeable {
    private transient ChangeSupport changeSupport = null;

    /**
     * Discover if we have any listeners registered.
     *
     * @return true if there is at least one listener
     * @deprecated use hasListeners(ChangeType) if at all possible
     */
    protected boolean hasListeners() {
        return (changeSupport != null) && changeSupport.hasListeners();
    }

    /**
     * Discover if we have listeners registered for a particular change type.
     *
     * @param ct the ChangeType we are interested in
     * @return true if there is at least one listener
     */
    protected boolean hasListeners(ChangeType ct) {
        return (changeSupport != null) && changeSupport.hasListeners(ct);
    }

    /**
     * Called the first time a ChangeSupport object is needed.  Override this if
     * you want to set the Unchanging set on the ChangeSupport, or if you want to
     * install listeners on other objects when the change system is initialized.
     *
     * @since 1.3
     */
    protected ChangeSupport generateChangeSupport() {
        return new ChangeSupport();
    }

    /**
     * Called to retrieve the ChangeSupport for this object.
     * <p/>
     * <p/>
     * Your implementation of this method should have the following structure:
     * <code><pre>
     * ChangeSupport cs = super.getChangeSupport(ct);
     * <p/>
     * if(someForwarder == null && ct.isMatching(SomeInterface.SomeChangeType)) {
     *   someForwarder = new ChangeForwarder(...
     * <p/>
     *   this.stateVariable.addChangeListener(someForwarder, VariableInterface.AChange);
     * }
     * <p/>
     * return cs;
     * </pre></code>
     * <p/>
     * It is usual for the forwarding listeners (someForwarder in this example) to
     * be transient and lazily instantiated. Be sure to register & unregister the
     * forwarder in the code that does the ChangeEvent handling in setter methods.
     */
    protected ChangeSupport getChangeSupport(ChangeType ct) {
        synchronized (this) {
            if (changeSupport == null) {
                changeSupport = generateChangeSupport();
            }
        }

        return changeSupport;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     */
    public final void addChangeListener(ChangeListener cl) {
        addChangeListener(cl, ChangeType.UNKNOWN);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public final void addChangeListener(ChangeListener cl, ChangeType ct) {
        ChangeSupport cs = getChangeSupport(ct);
        cs.addChangeListener(cl, ct);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     */
    public final void removeChangeListener(ChangeListener cl) {
        removeChangeListener(cl, ChangeType.UNKNOWN);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     * @param ct DOCUMENT ME!
     */
    public final void removeChangeListener(ChangeListener cl, ChangeType ct) {
        if (hasListeners()) {
            ChangeSupport cs = getChangeSupport(ct);
            cs.removeChangeListener(cl, ct);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ct DOCUMENT ME!
     * @return DOCUMENT ME!
     */
    public final boolean isUnchanging(ChangeType ct) {
        ChangeSupport cs = getChangeSupport(ct);

        return cs.isUnchanging(ct);
    }
}
