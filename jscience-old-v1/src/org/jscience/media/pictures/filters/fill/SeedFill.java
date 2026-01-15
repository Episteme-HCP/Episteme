package org.jscience.media.pictures.filters.fill;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class SeedFill {
    /** DOCUMENT ME! */
    private final static int MAX = 1000;

    /** DOCUMENT ME! */
    private int[] stack = new int[4 * MAX];

    /** DOCUMENT ME! */
    private int sp = 0;

    /** DOCUMENT ME! */
    private int minX;

    /** DOCUMENT ME! */
    private int minY;

    /** DOCUMENT ME! */
    private int maxX;

    /** DOCUMENT ME! */
    private int maxY;

    /** DOCUMENT ME! */
    private PixelOp pixelOp;

    /** DOCUMENT ME! */
    private PixelCompareOp compareOp;

/**
     * Creates a new SeedFill object.
     */
    public SeedFill() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param pixelOp DOCUMENT ME!
     * @param compareOp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle fill(int w, int h, int x, int y, PixelOp pixelOp,
        PixelCompareOp compareOp) {
        Rectangle bounds = null;
        int l = 0;
        int x1;
        int x2;
        int dy;

        this.pixelOp = pixelOp;
        this.compareOp = compareOp;

        minX = minY = 0;
        maxX = w - 1;
        maxY = h - 1;

        if (!compareOp.thisPixel(x, y) || (x < minX) || (x > maxX) ||
                (y < minY) || (y > maxY)) {
            return null;
        }

        bounds = new Rectangle(x, y, 1, 1);
        push(y, x, x, 1);
        push(y + 1, x, x, -1);

        while (sp > 0) {
            boolean skip;

            // Pop stack
            dy = stack[--sp];
            x2 = stack[--sp];
            x1 = stack[--sp];
            y = stack[--sp] + dy;

            for (x = x1; (x >= minX) && compareOp.thisPixel(x, y); x--) {
                pixelOp.apply(x, y);
                bounds.add(x, y);
            }

            if (x >= x1) {
                skip = true;
            } else {
                skip = false;
                l = x + 1;

                if (l < x1) {
                    push(y, l, x1 - 1, -dy);
                }

                x = x1 + 1;
            }

            do {
                if (!skip) {
                    for (; (x <= maxX) && compareOp.thisPixel(x, y); x++) {
                        pixelOp.apply(x, y);
                        bounds.add(x, y);
                    }

                    push(y, l, x - 1, dy);

                    if (x > (x2 + 1)) {
                        push(y, x2 + 1, x - 1, -dy);
                    }
                }

                for (x++; (x <= x2) && !compareOp.thisPixel(x, y); x++)
                    ;

                l = x;
                skip = false;
            } while (x <= x2);
        }

        bounds.width++;
        bounds.height++;

        return bounds;
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     * @param xl DOCUMENT ME!
     * @param xr DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    private void push(int y, int xl, int xr, int dy) {
        if ((sp < MAX) && ((y + dy) >= minY) && ((y + dy) <= maxY)) {
            stack[sp++] = y;
            stack[sp++] = xl;
            stack[sp++] = xr;
            stack[sp++] = dy;
        }
    }
}
