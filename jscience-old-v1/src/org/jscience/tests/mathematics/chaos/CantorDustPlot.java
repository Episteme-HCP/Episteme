import org.jscience.mathematics.chaos.*;

import java.applet.Applet;

import java.awt.*;


/**
 * Plot of Cantor dust.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class CantorDustPlot extends Applet {
    /**
     * DOCUMENT ME!
     */
    private final int N = 5;

    /**
     * DOCUMENT ME!
     */
    private Image img;

    /**
     * DOCUMENT ME!
     */
    private CantorDustGraphic dust;

    /**
     * DOCUMENT ME!
     */
    private int width;

    /**
     * DOCUMENT ME!
     */
    private int height;

    /**
     * DOCUMENT ME!
     */
    public void init() {
        width = getSize().width;
        height = getSize().height;
        img = createImage(width, height);
        dust = new CantorDustGraphic(img.getGraphics());
        dust.draw(0, width, N);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics g) {
        g.drawImage(img, 0, 0, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
      */
    class CantorDustGraphic extends CantorDust {
        /**
         * DOCUMENT ME!
         */
        private final Graphics g;

        /**
         * Creates a new CantorDustGraphic object.
         *
         * @param grafixs DOCUMENT ME!
         */
        public CantorDustGraphic(Graphics grafixs) {
            g = grafixs;
        }

        /**
         * DOCUMENT ME!
         *
         * @param start DOCUMENT ME!
         * @param end DOCUMENT ME!
         * @param n DOCUMENT ME!
         */
        public void draw(int start, int end, int n) {
            g.setColor(Color.black);
            g.drawLine(start, height / 2, end, height / 2);
            g.setColor(getBackground());
            recurse(start, end, n);
        }

        /**
         * DOCUMENT ME!
         *
         * @param start DOCUMENT ME!
         * @param end DOCUMENT ME!
         */
        protected void eraseLine(double start, double end) {
            g.drawLine((int) Math.round(start), height / 2,
                (int) Math.round(end), height / 2);
        }
    }
}
