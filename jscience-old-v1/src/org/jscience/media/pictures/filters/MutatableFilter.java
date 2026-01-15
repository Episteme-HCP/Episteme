package org.jscience.media.pictures.filters;

import java.awt.image.ImageFilter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public interface MutatableFilter {
    /**
     * DOCUMENT ME!
     *
     * @param amount DOCUMENT ME!
     * @param dst DOCUMENT ME!
     * @param keepShape DOCUMENT ME!
     * @param keepColors DOCUMENT ME!
     */
    public void mutate(int amount, ImageFilter dst, boolean keepShape,
        boolean keepColors);
}
