package org.jscience.util.mapper;

/**
 * This class is a simple container for an offset and an {@link
 * ArraySliceMappable} object.
 *
 * @author L. Maisonobe
 * @version $Id: ArrayMapperEntry.java,v 1.3 2007-10-23 18:24:37 virtualcall Exp $
 */
class ArrayMapperEntry {
    /** Mappable object. */
    public final ArraySliceMappable object;

    /** Offset from start of array. */
    public final int offset;

/**
     * Simple constructor.
     *
     * @param object mappable object
     * @param offset offset from start of array
     */
    public ArrayMapperEntry(ArraySliceMappable object, int offset) {
        this.object = object;
        this.offset = offset;
    }
}
