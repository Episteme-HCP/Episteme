package org.jscience.tests.physics.Mechanics;

import java.awt.*;
import java.awt.event.*;


/**
 * Angular momentum simulator.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class Rotation extends Frame implements Runnable {
    /** DOCUMENT ME! */
    private RigidBody2D body = new RigidBody2D();

    /** DOCUMENT ME! */
    private Display display = new Display();

/**
     * Creates a new Rotation object.
     */
    public Rotation() {
        super("Rotation!");
        add(display, "Center");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        body.setMass(5.0);
        body.setMomentOfInertia(5.0);

        Thread thr = new Thread(this);
        thr.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        Frame app = new Rotation();
        app.setSize(250, 250);
        app.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        double width;
        double height;
        double x;
        double y;

        while (true) {
            body.move(0.01);
            width = getSize().width;
            height = getSize().height;
            x = body.getXPosition();
            y = body.getYPosition();

            if (x > (width / 2.0)) {
                body.setXPosition(x - width);
            } else if (x < (-width / 2.0)) {
                body.setXPosition(x + width);
            }

            if (y > (height / 2.0)) {
                body.setYPosition(y - height);
            } else if (y < (-height / 2.0)) {
                body.setYPosition(y + height);
            }

            display.repaint();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private final class Display extends Canvas {
        /** DOCUMENT ME! */
        private Point start;

        /** DOCUMENT ME! */
        private Point end;

        /** DOCUMENT ME! */
        private boolean firstDrag = false;

/**
         * Creates a new Display object.
         */
        public Display() {
            addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        firstDrag = true;
                        start = end = null;
                    }

                    public void mouseReleased(MouseEvent e) {
                        if ((start != null) && (end != null)) {
                            final double Fx = end.x - start.x;
                            final double Fy = -(end.y - start.y);
                            final int cx = getSize().width / 2;
                            final int cy = getSize().height / 2;
                            final double x = (end.x - cx) -
                                body.getXPosition();
                            final double y = -(end.y - cy) -
                                body.getYPosition();

                            for (int i = 0; i < 4; i++) {
                                body.applyForce(Fx, Fy, x, y, 0.05);
                                body.move(0.05);
                                repaint();
                            }

                            System.out.println("Force (" + Fx + ',' + Fy +
                                ") applied at (" + x + ',' + y + ')');
                        }
                    }
                });
            addMouseMotionListener(new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        if (firstDrag) {
                            start = e.getPoint();
                            firstDrag = false;
                        } else if (start != null) {
                            end = e.getPoint();
                        }
                    }
                });
        }

        /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         */
        public void paint(Graphics g) {
            final Graphics2D g2 = (Graphics2D) g;
            final int cx = getSize().width / 2;
            final int cy = getSize().height / 2;
            final double x = body.getXPosition();
            final double y = body.getYPosition();
            g2.translate(x, -y);
            g2.rotate(-body.getAngle(), cx, cy);
            g2.setColor(Color.red);
            g2.fillRect(cx - 50, cy - 10, 100, 20);
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }
    }
}
