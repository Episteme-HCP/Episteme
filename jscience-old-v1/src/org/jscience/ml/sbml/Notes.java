package org.jscience.ml.sbml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores the note metadata for an SBML node.
 * <p/>
 * <p/>
 * This code is licensed under the DARPA BioCOMP Open Source License.  See LICENSE for more details.
 * </p>
 *
 * @author Nicholas Allen
 */

public final class Notes {
    private List notes;

    private static String wrapItem(String item) {
        return SBase.isItemOfType("notes", item) ? item : "<notes>" + item + "</notes>";
    }

    Notes() {
    }

    public void add(String note) {
        if (notes == null)
            notes = new ArrayList();
        notes.add(wrapItem(note));
    }

    public void clear() {
        notes = null;
    }

    public String get(int index) {
        if (notes == null)
            throw new ArrayIndexOutOfBoundsException(index);
        return (String) notes.get(index);
    }

    public int size() {
        return notes == null ? 0 : notes.size();
    }

    public Iterator iterator() {
        if (notes == null)
            notes = new ArrayList();
        return notes.iterator();
    }

    public void remove(int index) {
        if (notes == null)
            throw new ArrayIndexOutOfBoundsException(index);
        notes.remove(index);
    }

    public void set(int index, String note) {
        if (notes == null)
            notes = new ArrayList();
        notes.set(index, wrapItem(note));
    }

    /**
     * The SBML for this element.
     */

    public String toString() {
        if (notes == null)
            return "";
        StringBuffer buffer = new StringBuffer();
        for (Iterator iterator = notes.iterator(); iterator.hasNext();) {
            String note = (String) iterator.next();
            if (note.split("\\s*</??notes>\\s*").length != 0)
                buffer.append(note + "\n");
        }
        return buffer.toString();
    }
}
