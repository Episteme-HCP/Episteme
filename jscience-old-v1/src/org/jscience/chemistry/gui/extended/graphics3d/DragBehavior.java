package org.jscience.chemistry.gui.extended.graphics3d;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;

import java.util.Enumeration;

import javax.media.j3d.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class DragBehavior extends Behavior {
    /** DOCUMENT ME! */
    WakeupCriterion[] mouseEvents;

    /** DOCUMENT ME! */
    WakeupOr mouseCriterion;

    /** DOCUMENT ME! */
    int x;

    /** DOCUMENT ME! */
    int y;

    /** DOCUMENT ME! */
    int x_last;

    /** DOCUMENT ME! */
    int y_last;

    /** DOCUMENT ME! */
    double x_angle;

    /** DOCUMENT ME! */
    double y_angle;

    /** DOCUMENT ME! */
    double x_factor;

    /** DOCUMENT ME! */
    double y_factor;

    /** DOCUMENT ME! */
    TransformGroup transformGroup;

    /** DOCUMENT ME! */
    Transform3D modelTrans;

    /** DOCUMENT ME! */
    Transform3D transformX;

    /** DOCUMENT ME! */
    Transform3D transformY;

/**
     * Creates a new DragBehavior object.
     *
     * @param transformGroup DOCUMENT ME!
     */
    protected DragBehavior(TransformGroup transformGroup) {
        super();
        this.transformGroup = transformGroup;
        modelTrans = new Transform3D();
        transformX = new Transform3D();
        transformY = new Transform3D();
    }

    /**
     * DOCUMENT ME!
     */
    public void initialize() {
        x = 0;
        y = 0;
        x_last = 0;
        y_last = 0;
        x_angle = 0;
        y_angle = 0;
        x_factor = .03;
        y_factor = .03;

        mouseEvents = new WakeupCriterion[2];
        mouseEvents[0] = new WakeupOnAWTEvent(MouseEvent.MOUSE_DRAGGED);
        mouseEvents[1] = new WakeupOnAWTEvent(MouseEvent.MOUSE_PRESSED);
        mouseCriterion = new WakeupOr(mouseEvents);
        wakeupOn(mouseCriterion);
    }

    /**
     * DOCUMENT ME!
     *
     * @param test DOCUMENT ME!
     * @param modifier DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    final boolean isModifier(int test, int modifier) {
        return ((test & modifier) == modifier);
    }

    /**
     * DOCUMENT ME!
     *
     * @param criteria DOCUMENT ME!
     */
    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] event;
        int id;
        int dx;
        int dy;

        while (criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion) criteria.nextElement();

            if (wakeup instanceof WakeupOnAWTEvent) {
                event = ((WakeupOnAWTEvent) wakeup).getAWTEvent();

                for (int i = 0; i < event.length; i++) {
                    id = event[i].getID();

                    if (id == MouseEvent.MOUSE_DRAGGED) {
                        int mod = ((MouseEvent) event[i]).getModifiers();
                        System.out.println(mod + " " + InputEvent.BUTTON1_MASK);

                        if (isModifier(mod, InputEvent.BUTTON1_MASK)) {
                            System.out.println("button1");
                        }

                        if (isModifier(mod, InputEvent.BUTTON3_MASK)) {
                            System.out.println("button3");
                        }

                        x = ((MouseEvent) event[i]).getX();
                        y = ((MouseEvent) event[i]).getY();

                        dx = x - x_last;
                        dy = y - y_last;

                        x_angle = dy * y_factor;
                        y_angle = dx * x_factor;

                        transformX.rotX(x_angle);
                        transformY.rotY(y_angle);
                        modelTrans.mul(transformX, modelTrans);
                        modelTrans.mul(transformY, modelTrans);

                        transformGroup.setTransform(modelTrans);

                        x_last = x;
                        y_last = y;
                    } else if (id == MouseEvent.MOUSE_PRESSED) {
                        x_last = ((MouseEvent) event[i]).getX();
                        y_last = ((MouseEvent) event[i]).getY();
                    }
                }
            }
        }

        wakeupOn(mouseCriterion);
    }
}
