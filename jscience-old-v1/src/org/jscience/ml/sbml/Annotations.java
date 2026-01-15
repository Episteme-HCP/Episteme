package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores the annotation metadata for an SBML node.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public final class Annotations {
    private List annotations;

    private static String wrapItem(String item) {
        return SBase.isItemOfType("annotation", item) ? item : "<annotation>" + item + "</annotation>";
    }

    Annotations() {
    }

    public void add(String annotation) {
        if (annotations == null)
            annotations = new ArrayList();
        annotations.add(wrapItem(annotation));
    }

    public void clear() {
        annotations = null;
    }

    public String get(int index) {
        if (annotations == null)
            throw new ArrayIndexOutOfBoundsException(index);
        return (String) annotations.get(index);
    }

    public int size() {
        return annotations == null ? 0 : annotations.size();
    }

    public Iterator iterator() {
        if (annotations == null)
            annotations = new ArrayList();
        return annotations.iterator();
    }

    public void remove(int index) {
        if (annotations == null)
            throw new ArrayIndexOutOfBoundsException(index);
        annotations.remove(index);
    }

    public void set(int index, String annotation) {
        if (annotations == null)
            annotations = new ArrayList();
        annotations.set(index, wrapItem(annotation));
    }

    /**
     * The SBML for this element.
     */

    public String toString() {
        if (annotations == null)
            return "";
        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = annotations.iterator(); iterator.hasNext();)
            buffer.append((String) iterator.next() + "\n");
        return buffer.toString();
    }
}
