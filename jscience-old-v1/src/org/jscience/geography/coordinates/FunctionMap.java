package org.jscience.geography.coordinates;

import java.util.HashMap;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class FunctionMap extends HashMap {
    /** DOCUMENT ME! */
    private static FunctionMap _functionMap = new FunctionMap();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static FunctionMap instance() {
        return _functionMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conv DOCUMENT ME!
     */
    public synchronized void register(Conversions conv) {
        _functionMap.put(conv.getSrc().toString(), conv);
    }

    // returns a clone copy of the conversion object
    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Conversions get(SRM_SRFT_Code src) {
        Conversions conv;

        if ((conv = (Conversions) _functionMap.get(src.toString())) != null) {
            return conv.makeClone();
        } else {
            return null;
        }
    }
}
