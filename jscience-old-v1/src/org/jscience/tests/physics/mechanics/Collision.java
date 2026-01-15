package org.jscience.tests.physics.Mechanics;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * Two-body collision simulator.
 *
 * @author Mark Hale
 * @version 1.0
 */
public final class Collision extends Frame {
    /** DOCUMENT ME! */
    private ClassicalParticle2D A = new ClassicalParticle2D();

    /** DOCUMENT ME! */
    private ClassicalParticle2D B = new ClassicalParticle2D();

    /** DOCUMENT ME! */
    private TextField massA = new TextField("2.0");

    /** DOCUMENT ME! */
    private TextField massB = new TextField("2.0");

    /** DOCUMENT ME! */
    private TextField velXA = new TextField("3.0");

    /** DOCUMENT ME! */
    private TextField velXB = new TextField("3.0");

    /** DOCUMENT ME! */
    private TextField velYA = new TextField("3.0");

    /** DOCUMENT ME! */
    private TextField velYB = new TextField("-1.0");

    /** DOCUMENT ME! */
    private VectorDisplay display = new VectorDisplay(4);

    /** DOCUMENT ME! */
    private Label energyBefore = new Label();

    /** DOCUMENT ME! */
    private Label energyAfter = new Label();

    /** DOCUMENT ME! */
    private Label momentumXBefore = new Label();

    /** DOCUMENT ME! */
    private Label momentumXAfter = new Label();

    /** DOCUMENT ME! */
    private Label momentumYBefore = new Label();

    /** DOCUMENT ME! */
    private Label momentumYAfter = new Label();

/**
     * Creates a new Collision object.
     */
    public Collision() {
        super("Collision!");

        final Panel controls = new Panel();
        controls.setLayout(new GridLayout(4, 3));

        final Button button = new Button("Collide");
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    collide();
                }
            });
        controls.add(button);

        final Label labelA = new Label("Particle A");
        labelA.setForeground(Color.red);
        controls.add(labelA);

        final Label labelB = new Label("Particle B");
        labelB.setForeground(Color.blue);
        controls.add(labelB);
        controls.add(new Label("Mass:"));
        controls.add(massA);
        controls.add(massB);
        controls.add(new Label("X Velocity:"));
        controls.add(velXA);
        controls.add(velXB);
        controls.add(new Label("Y Velocity:"));
        controls.add(velYA);
        controls.add(velYB);

        final Panel info = new Panel();
        info.setLayout(new GridLayout(4, 3));
        info.add(new Panel());
        info.add(new Label("Before"));
        info.add(new Label("After"));
        info.add(new Label("Energy:"));
        info.add(energyBefore);
        info.add(energyAfter);
        info.add(new Label("X Momentum:"));
        info.add(momentumXBefore);
        info.add(momentumXAfter);
        info.add(new Label("Y Momentum:"));
        info.add(momentumYBefore);
        info.add(momentumYAfter);
        add(controls, "North");
        add(display, "Center");
        add(info, "South");
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent evt) {
                    dispose();
                    System.exit(0);
                }
            });
        collide();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     */
    public static void main(String[] arg) {
        Frame app = new Collision();
        app.setSize(500, 500);
        app.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     */
    private void collide() {
        A.setMass(parseDouble(massA.getText()));
        A.setVelocity(parseDouble(velXA.getText()), parseDouble(velYA.getText()));
        B.setMass(parseDouble(massB.getText()));
        B.setVelocity(parseDouble(velXB.getText()), parseDouble(velYB.getText()));
        display.setVector(0, -A.getXVelocity(), A.getYVelocity());
        display.setVector(1, -B.getXVelocity(), B.getYVelocity());
        energyBefore.setText(Double.toString(A.energy() + B.energy()));
        momentumXBefore.setText(Double.toString(A.getXMomentum() +
                B.getXMomentum()));
        momentumYBefore.setText(Double.toString(A.getYMomentum() +
                B.getYMomentum()));
        A.collide(B, 0.0);
        display.setVector(2, A.getXVelocity(), -A.getYVelocity());
        display.setVector(3, B.getXVelocity(), -B.getYVelocity());
        energyAfter.setText(Double.toString(A.energy() + B.energy()));
        momentumXAfter.setText(Double.toString(A.getXMomentum() +
                B.getXMomentum()));
        momentumYAfter.setText(Double.toString(A.getYMomentum() +
                B.getYMomentum()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static double parseDouble(String s) {
        return Double.valueOf(s).doubleValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @author $author$
     * @version $Revision: 1.3 $
     */
    private final class VectorDisplay extends Canvas {
        /** DOCUMENT ME! */
        private float[] vecX;

        /** DOCUMENT ME! */
        private float[] vecY;

        /** DOCUMENT ME! */
        private Color[] color;

/**
         * Creates a new VectorDisplay object.
         *
         * @param n DOCUMENT ME!
         */
        public VectorDisplay(int n) {
            vecX = new float[n];
            vecY = new float[n];
            color = new Color[n];
            color[0] = Color.red;
            color[1] = Color.blue;
            color[2] = Color.red;
            color[3] = Color.blue;
        }

        /**
         * DOCUMENT ME!
         *
         * @param i DOCUMENT ME!
         * @param dx DOCUMENT ME!
         * @param dy DOCUMENT ME!
         */
        public void setVector(int i, double dx, double dy) {
            final double norm = Math.sqrt((dx * dx) + (dy * dy));
            vecX[i] = (float) (dx / norm);
            vecY[i] = (float) (dy / norm);
            repaint();
        }

        /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         */
        public void paint(Graphics g) {
            final int ox = getSize().width / 2;
            final int oy = getSize().height / 2;
            int endX;
            int endY;

            for (int i = 0; i < vecX.length; i++) {
                endX = ox + (int) ((ox - 20) * vecX[i]);
                endY = oy + (int) ((oy - 20) * vecY[i]);
                g.setColor(color[i]);
                g.drawLine(ox, oy, endX, endY);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Dimension getPreferredSize() {
            return new Dimension(100, 100);
        }
    }
}
