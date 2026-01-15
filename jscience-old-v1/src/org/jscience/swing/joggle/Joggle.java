// JTEM - Java Tools for Experimental Mathematics
// Copyright (C) 2001 JTEM-Group
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

package org.jscience.swing.joggle;

import org.jscience.swing.spinner.SharableTimer;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * A Component drawing a joggle (or wheel) you can drag to move it (scroll)
 * and change a value to the corresponding {@link JoggleModel}.
 * The following image shows a joggle UI with {@link #HORIZONTAL} orientation.
 * <p/>
 * <img src="doc-files/joggle.gif" alt="joggle screenshot">
 * </p>
 * <p/>
 * Any joggle has a certain number of grooves. The visible groove count relies
 * on the apex angle. The apex angle must be a value between <i>pi/9</i> (20
 * degrees) and <i>pi</i> (180 degrees). For example, if the apex angle is <i>pi</i>
 * then approximately the half of all grooves will be visible. You can set the
 * number of grooves with the method {@link #setGrooveCount(int)}
 * and the apex angle with {@link #setApexAngle(double)}.
 * </p><p>
 * The rotation angle describes the current position of the joggle. You can
 * get it by the method {@link #getRotationAngle} but you can also explicitly
 * set it by {@link #setRotationAngle}. Note that the rotation angles of
 * <i>0</i> (0 degrees), <i>2*pi</i> (360 degrees),
 * <i>4*pi</i> (720 degrees), <i>6*pi</i> (1080 degrees), etc., corresponds to
 * the same position. You can get these position by the method
 * {@link #getPositionAngle()}.
 * </p><p>
 * If you are not using the constructor expecting the arguments for
 * <i>grooveCount</i>, <i>initApexAngle</i> and <i>initRotationAngle</i>,
 * the following default values will be set:
 * <ul>
 * <li>grooveCount = 30</li>
 * <li>apexAngle = Math.PI (180 degrees)</li>
 * <li>rotationAngle = 0.0</li>
 * </ul>
 * </p>
 * <h3>"Jog the Joggle"</h3>
 * <p/>
 * You can "jog" (drag) this <code>Joggle</code> {@link #BACKWARD},
 * {@link #FORWARD} or to {@link #BIDIRECTIONAL both directions}.
 * <code>FORWARD</code> means that you can drag this <code>Joggle</code>
 * to the right ({@link #HORIZONTAL} orientation) or to the bottom
 * ({@link #VERTICAL} orientation). <code>BACKWARD</code> means
 * that you can drag it to the left (<code>HORIZONTAL</code> orientation) or to
 * the top (<code>VERTICAL</code> orientation).
 * The default jogDirection is <code>BIDIRECTIONAL</code>, that means
 * dragging this <code>Joggle</code> into both directions is possible. You
 * can specify the jogDirection with {@link #setJogDirection(int)} or with a
 * certain constructor.
 * </p>
 * <p/>
 * If you want the joggle to move constantly and permanently, you only have to
 * "jog" it. This meens, you have to release the mouse button while you are
 * still dragging. The speed of the joggle's rotation depends on the speed
 * you drag it in the moment of mouse releasing.
 * </p>
 * <h3>"Color the Joggle"</h3>
 * <p/>
 * There are three colors to set for a joggle.
 * <ul>
 * <li>First is the <i>light color</i>: visible
 * at the joggle's {@link #setHighlightAngle(double) highlight angle}.
 * The default highlight angle is 0 - in this
 * case the light color is the color on the very top of the joggle. The light
 * color can be set by {@link #setJoggleLightColor(Color)}.</li>
 * <li>Second color is the <i>shadow color</i>: it defines the destinition
 * color of the color gradient from the highlight angle (light color) to the
 * joggle edges. The shadow color is visible at 90 degrees (or more) from the
 * highlight angle. It can be set by {@link #setJoggleShadowColor(Color)}.</li>
 * <li>Third color is the color for the <i>marked groove</i>. The marked groove
 * eases following any rotation of the joggle. The marked groove color
 * changes towards the joggle border like the light color. That's why
 * the marked groove is invisible, if the marked groove color is equal
 * to the light color. The marked groove color can be set by
 * {@link #setMarkedGrooveColor(Color)}.</li>
 * </ul>
 * The default light and shadow colors are the colors of a scrollbar
 * defined by the current LookAndFeel. To get these colors also after
 * the LookAndFeel changed, {@link #updateUI()} will call
 * {@link UIManager#getColor(Object)
 * UIManager.getColor}<code>("ScrollBar.thumbShadow")</code> and
 * <code>UIManager.getColor("ScrollBar.thumbHighlight")</code>.<br>
 * The default marked groove color is {@link Color#RED red}.<br>
 * The following image shows a joggle with <i>white</i> as the light color
 * and <i>black</i> as the shadow and marked groove color.</p>
 * <p/>
 * <img src="doc-files/joggle_bw.gif" alt="joggle black and white screenshot"></p>
 *
 * @author marcel
 */
public class Joggle extends JComponent {
    ChangeEvent changeEvent = null;
    /**
     * For horizontal orientation of this <code>Joggle</code>.
     */
    static final public int HORIZONTAL = 0;
    /**
     * For vertical orientation of this <code>Joggle</code>.
     */
    static final public int VERTICAL = 1;
    private int jogDirection;
    /**
     * Direction a <code>Joggle</code> is able to be dragged.
     */
    static final public int BIDIRECTIONAL = 0;
    /**
     * Direction a <code>Joggle</code> is able to be dragged.
     */
    static final public int FORWARD = 1;
    /**
     * Direction a <code>Joggle</code> is able to be dragged.
     */
    static final public int BACKWARD = -1;
    private boolean antialiasing;
    private boolean isHorizontal;
    private double rotationAngle;
    private double apexAngle;
    private int grooveCount;
    private int height;
    private int width;
    private Insets insets;
    private double highlightAngle;
    private Color joggleLightColor;
    private Color joggleShadowColor;
    private Color markedGrooveColor;
    /*
    * Private field to keep the first point the mouse will be clicked before dragging.
    * The MouseListener writes to this field and the MouseMotionListener reads
    * from this field.
    */
    private Point clickPoint;
    private Point dragPoint = new Point();
    private Point oldDragPoint = new Point();
    private long dragTime;
    private long oldDragTime;
    private long oldTimerEventTime;
    private double jogTimerSpeed;
    private SharableTimer jogTimer;
    private ActionListener jogTimerListener;
    public static final int TIMER_DELAY = 20;
    /*
    * Flag to know the reason of a changeEvent coming from JoggleModel.
    */
    //private boolean changeByJogTimer=false;
    /*
    * After a mouse click before dragging the rotationAngle will be stored into this
    * field and every drag will calculate the new rotationAngle by adding the
    * "dragged" angle to the rotationAngleOld.
    */
    private double rotationAngleOld;
    private JoggleModel model;
    private JogglePainter painter;

    /**
     * Creates a new <code>Joggle</code> with a new {@link DefaultJoggleModel}.
     * Other initial values are:<br>
     * <br>
     * <ul type="none">
     * <li>orientation = {@link #HORIZONTAL}</li>
     * <li>grooveCount = 30</li>
     * <li>apexAngle = Math.PI</li>
     * <li>rotationAngle = 0.0</li>
     * <li>jogDirection = {@link #BIDIRECTIONAL}</li>
     * </ul>
     */
    public Joggle() {
        this(new DefaultJoggleModel(), HORIZONTAL, 30, Math.PI, BIDIRECTIONAL);
    }

    /**
     * Creates a new <code>Joggle</code> with a new {@link DefaultJoggleModel}
     * and the specified orientation. The orientation can be {@link #HORIZONTAL} or
     * {@link #VERTICAL}. Other initial values are:<br>
     * <br>
     * <ul type="none">
     * <li>grooveCount = 30</li>
     * <li>apexAngle = Math.PI</li>
     * <li>rotationAngle = 0.0</li>
     * <li>jogDirection = {@link #BIDIRECTIONAL}</li>
     * </ul>
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public Joggle(int orientation) {
        this(new DefaultJoggleModel(), orientation, 30, Math.PI, BIDIRECTIONAL);
    }

    /**
     * Creates a new <code>Joggle</code> with the specified {@link JoggleModel}.
     * Other initial values are:<br>
     * <br>
     * <ul type="none">
     * <li>orientation = {@link #HORIZONTAL}</li>
     * <li>grooveCount = 30</li>
     * <li>apexAngle = Math.PI</li>
     * <li>rotationAngle = <code>joggleModel.</code>{@link
     * JoggleModel#getValue() getValue()}<code> /
     * joggleModel.</code>{@link JoggleModel#getTransmissionRatio()
     * getTransmissionRatio()}</li>
     * <li>jogDirection = {@link #BIDIRECTIONAL}</li>
     * </ul>
     *
     * @param joggleModel A data model for this <code>Joggle</code> .
     */
    public Joggle(JoggleModel joggleModel) {
        this(joggleModel, HORIZONTAL, 30, Math.PI, BIDIRECTIONAL);
    }

    /**
     * Creates a new <code>Joggle</code> with the specified {@link JoggleModel}
     * and orientation. The orientation can be {@link #HORIZONTAL} or
     * {@link #VERTICAL}. Other initial values are:<br>
     * <br>
     * <ul type="none">
     * <li>grooveCount = 30</li>
     * <li>apexAngle = Math.PI</li>
     * <li>rotationAngle = <code>joggleModel.</code>{@link
     * JoggleModel#getValue() getValue()}<code> /
     * joggleModel.</code>{@link JoggleModel#getTransmissionRatio()
     * getTransmissionRatio()}</li>
     * <li>jogDirection = {@link #BIDIRECTIONAL}</li>
     * </ul>
     *
     * @param joggleModel A data model for this <code>Joggle</code> .
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public Joggle(JoggleModel joggleModel, int orientation) {
        this(joggleModel, orientation, 30, Math.PI, BIDIRECTIONAL);
    }

    /**
     * Creates a new <code>Joggle</code> with the specified {@link JoggleModel}
     * and orientation. The orientation can be {@link #HORIZONTAL} or
     * {@link #VERTICAL}. Further more, init values for grooveCount, apexAngle
     * and jogDirection have to be specified. See class
     * documentation for details.
     * <p/>
     * The current position of this
     * <code>Joggle</code> - its rotationAngle - will be calculated by<br>
     * <br>
     * <code>joggleModel.</code>{@link JoggleModel#getValue() getValue()}<code> /
     * joggleModel.</code>{@link JoggleModel#getTransmissionRatio()
     * getTransmissionRatio()}.
     * </p>
     *
     * @param joggleModel      A data model for this <code>Joggle</code> .
     * @param orientation      {@link #HORIZONTAL} or {@link #VERTICAL}.
     * @param grooveCount      total number of grooves this <code>Joggle</code> has.
     * @param initApexAngle    the angle describing the visible part of this
     *                         <code>Joggle</code>.
     * @param initJogDirection {@link #FORWARD}, {@link #BACKWARD} or
     *                         {@link #BIDIRECTIONAL}.
     * @throws IllegalArgumentException if the initApexAngle is no value
     *                                  between pi and pi/9.
     * @throws IllegalArgumentException if the orientation is not
     *                                  {@link #HORIZONTAL} or {@link #VERTICAL}.
     * @throws IllegalArgumentException if the jogDirection is not
     *                                  {@link #FORWARD}, {@link #BACKWARD} or
     *                                  {@link #BIDIRECTIONAL}.
     */
    public Joggle(JoggleModel joggleModel, int orientation, int grooveCount,
                  double initApexAngle, int initJogDirection) {
        if (initApexAngle > Math.PI || initApexAngle < Math.PI / 9)
            throw new IllegalArgumentException("apex angle must be a value between pi/9 and pi");
        if (orientation != HORIZONTAL && orientation != VERTICAL)
            throw new IllegalArgumentException("unexpected orientation");
        isHorizontal = orientation == HORIZONTAL;
        if (isHorizontal)
            painter = new HorizontalPainter();
        else
            painter = new VerticalPainter();
        if (initJogDirection != BIDIRECTIONAL && initJogDirection != FORWARD &&
                initJogDirection != BACKWARD)
            throw new IllegalArgumentException("unexpected jogDirection");
        jogDirection = initJogDirection;
        model = joggleModel;
        model.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent ev) {
                /*if(!changeByJogTimer)
                if(jogTimer!=null)
                {
                  jogTimer.stop();
                }*/
                rotationAngle = model.getValue() / model.getTransmissionRatio();
                fireStateChanged();
                repaint();
            }
        });
        rotationAngle = model.getValue() / model.getTransmissionRatio();
        this.grooveCount = grooveCount;
        apexAngle = initApexAngle / 2;
        this.highlightAngle = 0;
        antialiasing = true;
        //initialize look and feel spicific colors for light and shadow.
        updateUI();
        markedGrooveColor = Color.RED;
        Border innerBorder = new BevelBorder(BevelBorder.LOWERED,
                Color.BLACK, Color.BLACK);
        Border outerBorder = new BevelBorder(BevelBorder.LOWERED);
        setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5),
                new CompoundBorder(outerBorder, innerBorder)));
        painter.setValidateSize();

        /*
        * will be added to the jogTimer before it will be started.
        */
        jogTimerListener = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                long timerEventTime = System.currentTimeMillis();
                //System.out.println("-- timerEventTime= "+timerEventTime);
                int deltaT = (int) (timerEventTime - oldTimerEventTime);
                //System.out.println("-- deltaT= "+deltaT);
                oldTimerEventTime = timerEventTime;
                double changeValue = jogTimerSpeed * deltaT;
                //System.out.println("-- changeValue= "+v);
                double nextValue = model.getValue() +
                        changeValue * model.getTransmissionRatio();
                //changeByJogTimer=true;
                setValue(nextValue);
                //System.out.println("-- value= " + getValue());
                //changeByJogTimer=false;
                if (nextValue < model.getMinimum() ||
                        nextValue > model.getMaximum()) {
                    jogTimer.stop();
                }
            }
        };
        jogTimer = new SharableTimer();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent ev) {
                jogTimer.stop();
                clickPoint = ev.getPoint();
                dragPoint.x = clickPoint.x;
                dragPoint.y = clickPoint.y;
                dragTime = ev.getWhen();
                rotationAngleOld = rotationAngle;
            }

            public void mouseReleased(MouseEvent ev) {
                long releaseTime = ev.getWhen();
                long currTime = System.currentTimeMillis();
//        if(currTime<releaseTime)
//          new Exception("Joggle.Joggle(): time "
//            +(releaseTime-currTime)+" ms").printStackTrace();
                //jog only if the user releases the mouse button during dragging
                boolean jogIntention = releaseTime - dragTime == 0;
                //System.out.println("releaseTime-dragTime: "+(releaseTime-dragTime));
                if (jogIntention) {
                    int deltaDragTime = (int) (dragTime - oldDragTime);
                    double dragAngle = painter.getDraggedAngle(oldDragPoint, dragPoint);
                    jogTimerSpeed = dragAngle / deltaDragTime;
                    //System.out.println("-- v= "+v);
                    oldTimerEventTime = System.currentTimeMillis();//releaseTime;
                    //System.out.println("-- oldTimerEventTime= "+oldTimerEventTime);
                    //System.out.println("-- timer start...");
                    jogTimer.start(TIMER_DELAY, jogTimerListener, true);
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            double draggedAngle;

            /*
            * The "dragged" angle is the approximation at ...
            */
            public void mouseDragged(MouseEvent ev) {
                if (!isEnabled())
                    return;
                if (jogDirection != BIDIRECTIONAL) {
                    int draggedDirection = painter.getDraggedDirection(dragPoint, ev.getPoint());
                    if (draggedDirection != jogDirection) {
                        clickPoint = ev.getPoint(); //e.g.: jogDirection is FORWARD, you drag backward ->
                        dragPoint.x = clickPoint.x; //joggle doesn't move. drag forward again and the joggle
                        dragPoint.y = clickPoint.y; //moves at once
                        rotationAngleOld = rotationAngle;
                        return;
                    }
                }
                oldDragPoint.x = dragPoint.x;
                oldDragPoint.y = dragPoint.y;
                dragPoint = ev.getPoint();
                oldDragTime = dragTime;
                dragTime = ev.getWhen();
                draggedAngle = painter.getDraggedAngle(clickPoint, dragPoint);
                setValue((rotationAngleOld + draggedAngle) * model.getTransmissionRatio());
                //System.out.println("-- value=" + getValue());
            }
        });
    }

    public void paintComponent(Graphics g) {
        //super.paintComponent(g);
        g = getComponentGraphics(g);
        Graphics2D g2d = (Graphics2D) g;
        insets = getInsets(insets);
        if (isEnabled()) {
            g2d.translate(insets.left, insets.top);
            paintJoggle(g2d);
            g2d.translate(-insets.left, -insets.top);
        } else {
            BufferedImage img = new BufferedImage(painter.getJoggleWidth(),
                    painter.getJoggleHeight(), BufferedImage.TYPE_INT_RGB);
            paintJoggle(img.createGraphics());
            Image grayImg = GrayFilter.createDisabledImage(img);
            g2d.drawImage(grayImg, insets.left, insets.top, null);
        }
    }

    private void paintJoggle(Graphics2D g2d) {
        RenderingHints renderingHintsBackup = null;
        if (antialiasing) {
            renderingHintsBackup = g2d.getRenderingHints();
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        }
        double positionAngle = getPositionAngle();
        height = painter.getJoggleHeight();//getHeight()-getInsets().bottom-getInsets().top;
        width = painter.getJoggleWidth();//getWidth()-getInsets().left-getInsets().right;
        double lowerLimit = (-apexAngle - positionAngle) * grooveCount / (2 * Math.PI);
        double upperLimit = (apexAngle - positionAngle) * grooveCount / (2 * Math.PI);
        int kmin = (int) Math.floor(lowerLimit + 1);
        int kmax = (int) Math.floor(upperLimit);
        //System.out.println("- kmin: "+kmin);
        //System.out.println("- kmax: "+kmax);

        //for color gradient:
        float widthKOld = 0.f;
        double rotationAngleKLowLimit =
                positionAngle + 2 * Math.PI * lowerLimit / grooveCount;
        double rotationAngleKUpLimit =
                positionAngle + 2 * Math.PI * upperLimit / grooveCount;
        double rotationAngleKOld = rotationAngleKLowLimit;
        //---------------------
        for (int k = kmin; k <= kmax; k++) {
            double rotationAngleK = positionAngle + 2 * Math.PI * k / grooveCount;
            //System.out.println("k="+i+", rotationAngleK="+rotationAngleK);
            float widthK = (float) (
                    (1 + Math.sin(rotationAngleK) / Math.sin(apexAngle)) * width / 2);
            //g2d.setColor(getColor((widthKOld+widthK)/2));
            g2d.setColor(getColorByAngle((rotationAngleKOld + rotationAngleK) / 2,
                    joggleLightColor, joggleShadowColor, 0));
            painter.fillRect(g2d, widthKOld, 0, widthK - widthKOld, height);
            float highlightOffset = (rotationAngleKOld < highlightAngle) ? -1.f : 1.f;
            g2d.setColor(getColorByAngle(rotationAngleKOld,
                    joggleLightColor, joggleShadowColor, 20));
            painter.drawLine(g2d, widthKOld + highlightOffset, 0, widthKOld + highlightOffset, height - 1);
            g2d.setColor(getColorByAngle(rotationAngleKOld,
                    joggleLightColor, joggleShadowColor, -20));
            painter.drawLine(g2d, widthKOld, 0, widthKOld, height - 1);
            //drawing red groove:
            if (k > kmin)
                drawMarkedGroove(g2d, k - 1, rotationAngleKOld, widthKOld, highlightOffset);
            //end of drawing red groove
            widthKOld = widthK;
            rotationAngleKOld = rotationAngleK;
        }
        g2d.setColor(getColorByAngle((rotationAngleKOld + rotationAngleKUpLimit) / 2,
                joggleLightColor, joggleShadowColor, 0));
        painter.fillRect(g2d, widthKOld, 0, width + 1 - widthKOld, height);
        g2d.setColor(getColorByAngle(rotationAngleKOld,
                joggleLightColor, joggleShadowColor, 20));
        painter.drawLine(g2d, widthKOld + 1, 0, widthKOld + 1, height - 1);
        g2d.setColor(getColorByAngle(rotationAngleKOld,
                joggleLightColor, joggleShadowColor, -20));
        painter.drawLine(g2d, widthKOld, 0, widthKOld, height - 1);
        drawMarkedGroove(g2d, kmax, rotationAngleKOld, widthKOld, 1);
        if (antialiasing)
            g2d.setRenderingHints(renderingHintsBackup);
    }

    private void drawMarkedGroove(Graphics2D g2d, int k, double angle, float width,
                                  float highlightOffset) {
        int lastGrooveNumber = k < 0 ?
                k + (((-k + grooveCount - 1) / grooveCount) - 1) * grooveCount + grooveCount + 1 :
                (k + 1) - (k / grooveCount) * grooveCount;
        if (lastGrooveNumber == 1) {
            //System.out.println("k="+k);
            //System.out.println("lastGroove="+lastGrooveNumber);
            g2d.setColor(getColorByAngle(angle, markedGrooveColor,
                    joggleShadowColor, 20));
            painter.drawLine(g2d, width + highlightOffset, 0,
                    width + highlightOffset, height - 1);
            g2d.setColor(getColorByAngle(angle, markedGrooveColor,
                    joggleShadowColor, -20));
            painter.drawLine(g2d, width, 0, width, height - 1);
        }
    }

    /*
    * Returns the average color of lightColor and shadowColor depending
    * on the specified angle.
    *
    * @param lighter a percentage specification that makes the color brighter.
    */
    private Color getColorByAngle(double angle, Color lightColor,
                                  Color shadowColor, int lighter) {
        //intensity between 0 and 1 depending on the specified angle,
        //describes the percentage of the lightColor (the top of the joggle
        //0->0% of the compared color value (r, g, b values)
        //1->100% of the compared color value
        double lightColorIntensity = Math.abs(Math.cos(angle - highlightAngle));
        double shadowColorIntensity = 1 - lightColorIntensity;
        int r = (int) ((lightColor.getRed() * lightColorIntensity +
                shadowColor.getRed() * shadowColorIntensity) * (1 + lighter / 100.));
        int g = (int) ((lightColor.getGreen() * lightColorIntensity +
                shadowColor.getGreen() * shadowColorIntensity) * (1 + lighter / 100.));
        int b = (int) ((lightColor.getBlue() * lightColorIntensity +
                shadowColor.getBlue() * shadowColorIntensity) * (1 + lighter / 100.));
        if (r > 255) r = 255;
        if (g > 255) g = 255;
        if (b > 255) b = 255;
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        return new Color(r, g, b);
    }

    /**
     * Returns the current apex angle describing the visible part of this
     * <code>Joggle</code>.
     *
     * @return the current apex angle describing the visible part of this
     *         <code>Joggle</code>.
     * @see #setApexAngle(double)
     */
    public double getApexAngle() {
        return apexAngle * 2;
    }

    /**
     * Sets a new apex angle (in radian) for this <code>Joggle</code>.
     * The angle describes the visible part of this <code>Joggle</code>.
     *
     * @param d the new apex angle.
     * @throws IllegalArgumentException if d&gt;pi or d&lt;pi/9
     */
    public void setApexAngle(double d) {
        if (d > Math.PI || d < Math.PI / 9)
            throw new IllegalArgumentException("apex angle must be a value between pi/9 and pi");
        apexAngle = d / 2;
        repaint();
    }

    /**
     * Returns the current count of grooves of this <code>Joggle</code>.
     * Note that the really visible groove count depends on the apex angle
     * and is the half of the set groove count at the maximum apex angle of
     * pi (180 degrees).
     *
     * @return the currently set groove count.
     * @see #getApexAngle()
     */
    public int getGrooveCount() {
        return grooveCount;
    }

    /**
     * Sets the total (visible and invisible) count of grooves of this
     * <code>Joggle</code>.
     * Note that the really visible number of grooves depends on the
     * currently set apex angle.
     *
     * @param i the new count of grooves.
     * @see #setApexAngle(double)
     */
    public void setGrooveCount(int i) {
        grooveCount = i;
        repaint();
    }

    /**
     * Returns the current rotation angle (in radian). This angle describes the
     * absolut position of this <code>Joggle</code> depending on the count of
     * rotations.
     * If you want the angle in relation to one rotation, you have to
     * call {@link #getPositionAngle()}.
     *
     * @return the current rotation angle.
     * @see #getPositionAngle()
     */
    public double getRotationAngle() {
        return rotationAngle;
    }

    /**
     * Sets the current rotation angle (in radian) of this <code>Joggle</code>.
     * The rotation angle describes its absolut position depending on the count
     * of rotations the <code>Joggle</code> has already complete.
     *
     * @param d the new rotation angle.
     */
    public void setRotationAngle(double d) {
        //rotationAngle=d;
        setValue(d * model.getTransmissionRatio());
        //repaint();
    }

    /**
     * Returns the angle (in radian) in relation to the current rotation angle
     * but ignoring the count of rotations. That's why the postion angle is
     * a value between 0 (inclusively) and 2*pi (exclusively). For example:
     * If the rotationAngle is 2*pi (4*pi, 6*pi, 8*pi, ...), the position angle
     * is 0.
     *
     * @return the relativ position angle in relation to one rotation.
     */
    public double getPositionAngle() {
        return rotationAngle % (2 * Math.PI);
    }

    /**
     * Returns the angle (in radian) to the lightpoint shining on this
     * <code>Joggle</code>.
     *
     * @return the angle (in radian) to the lightpoint shining on this
     *         <code>Joggle</code>.
     */
    public double getHighlightAngle() {
        return highlightAngle;
    }

    /**
     * Sets the angle (in radian) to the lightpoint shining on this
     * <code>Joggle</code>.
     * If d=0 then the lightpoint stands in the zenith to this
     * <code>Joggle</code>.
     * Other values around zero move
     * the lightpoint to the left and right ({@link #HORIZONTAL} orientation)
     * or top and bottom ({@link #VERTICAL} orientation). d should be a radian
     * value between -pi and pi.
     *
     * @param d a radian value describing the highlight angle.
     */
    public void setHighlightAngle(double d) {
        highlightAngle = d;
        repaint();
    }

    /**
     * Returns true if the Java2D rendering mode is set to antialias this
     * <code>Joggle</code>, false otherwise.
     *
     * @return true if the Java2D rendering mode is set to antialias this
     *         <code>Joggle</code>, false otherwise.
     */
    public boolean getAntialiasing() {
        return antialiasing;
    }

    /**
     * Specifies the Java2D rendering mode. If this property is set to true,
     * this <code>Joggle</code> will be drawn antialiased, otherwise not.
     * The default is true.
     *
     * @param b
     */
    public void setAntialiasing(boolean b) {
        antialiasing = b;
        repaint();
    }

    /**
     * Returns the current light color of this <code>Joggle</code>.
     * The light color is the color at the joggle's highlight angle.
     *
     * @return the light color of this <code>Joggle</code>.
     * @see #getHighlightAngle()
     */
    public Color getJoggleLightColor() {
        return joggleLightColor;
    }

    /**
     * Sets this <code>Joggle's</code> color at the highlight angle.
     *
     * @param c the new light color.
     * @see #getHighlightAngle()
     */
    public void setJoggleLightColor(Color c) {
        joggleLightColor = c;
        repaint();
    }

    /**
     * Returns the current shadow color of this <code>Joggle</code>.
     * The shadow color is visible at 90 degrees or more from the highlight angle.
     *
     * @return the shadow color of this <code>Joggle</code>.
     * @see #getJoggleLightColor()
     * @see #getHighlightAngle()
     */
    public Color getJoggleShadowColor() {
        return joggleShadowColor;
    }

    /**
     * Sets this <code>Joggle's</code> shadow color.
     *
     * @param c the new shadow color.
     */
    public void setJoggleShadowColor(Color c) {
        joggleShadowColor = c;
        repaint();
    }

    /**
     * Returns the color of the marked groove. The marked groove makes it
     * easier to follow any single rotation of this <code>Joggle</code>.
     *
     * @return the color of the marked groove.
     */
    public Color getMarkedGrooveColor() {
        return markedGrooveColor;
    }

    /**
     * Sets the color of this <code>Joggle's</code> marked groove.
     *
     * @param c the new color for the marked groove.
     */
    public void setMarkedGrooveColor(Color c) {
        markedGrooveColor = c;
        repaint();
    }

    /**
     * Returns the currently set direction this <code>Joggle</code> is able to be dragged.
     *
     * @return {@link #FORWARD}, {@link #BACKWARD} or {@link #BIDIRECTIONAL}
     */
    public int getJogDirection() {
        return jogDirection;
    }

    /**
     * Sets the direction this <code>Joggle</code> should be able to be dragged.
     *
     * @param direction {@link #FORWARD}, {@link #BACKWARD} or
     *                  {@link #BIDIRECTIONAL}
     */
    public void setJogDirection(int direction) {
        if (direction != BIDIRECTIONAL && direction != FORWARD &&
                direction != BACKWARD)
            throw new IllegalArgumentException("unexpected jogDirection");
        jogDirection = direction;
    }

    /**
     * Returns this <code>Joggle's</code> data model.
     *
     * @return this <code>Joggle's</code> data model.
     * @see JoggleModel
     */
    public JoggleModel getModel() {
        return model;
    }

    /**
     * Sets a new data model for this <code>Joggle</code>.
     *
     * @param model the new data model.
     * @see JoggleModel
     */
    public void setModel(JoggleModel model) {
        this.model = model;
    }

    /**
     * Returns the value this <code>Joggle</code> is currently representing.
     * <p/>
     * <b>Note:</b> The returned value comes from the model and is almost never
     * equal to the rotationAngle. It rather depends on the rotationAngle
     * by multiplying it with a <b>transmissionRatio</b>. This ratio is defined
     * in the model, too.
     * </p>
     *
     * @return the value this <code>Joggle</code> is currently representing.
     * @see JoggleModel#getValue()
     * @see JoggleModel#getTransmissionRatio()
     */
    public double getValue() {
        return model.getValue();
    }

    /**
     * Sets a new (transmissioned) value to the model.
     *
     * @param value the new value this <code>Joggle</code> has to represent.
     * @see #getValue()
     * @see JoggleModel#setValue(double)
     * @see JoggleModel#getTransmissionRatio()
     */
    public void setValue(double value) {
        /* The jogTimer will be stoped if running because this method calls
        * JoggleModel.setValue(value) and the result is a changeEvent to this
        * Joggle's ChangeListener. The timer will not be stopped if the setValue
        * method was called as a result of the jogTimer
        * (see changeByJogTimer).
        */
        model.setValue(value);
    }

    /**
     * Returns the SharableTimer this Joggle is using.
     *
     * @return the SharableTimer this Joggle is using.
     */
    public SharableTimer getTimer() {
        return jogTimer;
    }

    /**
     * Sets a new SharableTimer for this Joggle.
     *
     * @param timer the new timer.
     */
    public void setTimer(SharableTimer timer) {
        if (jogTimer.isRunning())
            jogTimer.stop();
        jogTimer = timer;
    }

    /* (non-Javadoc)
    * @see java.awt.Component#doLayout()
    */
    public void doLayout() {
        //System.out.println("-- doLayout()");
        super.doLayout();
        painter.setValidateSize();
    }

    /**
     * Sets shadow and light color to new LookAndFeel colors of a scrollbar only
     * if they were not set manually by the user. The method will be called
     * in constructor and if a new LookAndFeel was set and
     * {@link javax.swing.SwingUtilities#updateComponentTreeUI(Component)}
     * was called.
     */
    public void updateUI() {
        //System.out.println("\nupdateUI\n");
        super.updateUI();
        if (joggleShadowColor instanceof UIResource)
            joggleShadowColor = null;
        if (joggleLightColor instanceof UIResource)
            joggleLightColor = null;
        if (joggleShadowColor == null)
            joggleShadowColor = UIManager.getColor("ScrollBar.thumbShadow");
        if (joggleLightColor == null)
            joggleLightColor = UIManager.getColor("ScrollBar.thumbHighlight");
    }

    /* (non-Javadoc)
    * @see java.awt.Container#validateTree()
   protected void validateTree()
   {
     System.out.println("-- validateTree()");
     super.validateTree();
   }
    */
    /*private void setValidateSize()
    {
      insets=getInsets(insets);
      setPreferredSize(new Dimension(insets.left+insets.right+100,
          insets.bottom+insets.top+10));
      setMinimumSize(new Dimension(insets.left+insets.right+10,
          insets.bottom+insets.top+10));
    }*/
    /**
     * Defines methods for drawing lines and filled rectangles (necessary for
     * drawing a joggle). A certain JogglePainter implements these methods
     * in relation to the orientation.
     *
     * @author marcel
     */
    abstract class JogglePainter {
        /**
         * the JoggleWidth should be seen as the length of the joggle in its
         * orientation. It's the real width of the joggle when drawing it horizontal.
         * Otherwise it's the height.
         */
        abstract public int getJoggleWidth();

        abstract public int getJoggleHeight();

        abstract public void drawLine(Graphics2D g2D,
                                      float x1, float y1, float x2, float y2);

        abstract public void fillRect(Graphics2D g2D,
                                      float x, float y, float width, float height);

        /**
         * Return the dragged angle from sourcePoint to destPoint. See MouseMotionListener
         * beeing created in constructor.
         */
        abstract public double getDraggedAngle(Point sourcePoint, Point destPoint);

        /**
         * Return the dragged direction. Direction can be FORWARD or BACKWARD.
         */
        abstract public int getDraggedDirection(Point sourcePoint, Point destPoint);

        abstract public void setValidateSize();
    }

    class HorizontalPainter extends JogglePainter {
        public int getJoggleWidth() {
            return getWidth() - insets.left - insets.right;
        }

        public int getJoggleHeight() {
            return getHeight() - insets.bottom - insets.top;
        }

        public void drawLine(Graphics2D g2D, float x1, float y1, float x2, float y2) {
            //System.out.println("x1="+x1+", y1="+y1+", x2="+x2+", y2="+y2);
            g2D.draw(new Line2D.Float(x1, y1, x2, y2));
        }

        public void fillRect(Graphics2D g2D, float x, float y, float width, float height) {
            g2D.fill(new Rectangle2D.Float(x, y, width, height));
        }

        public double getDraggedAngle(Point sourcePoint, Point destPoint) {
            return (destPoint.x - sourcePoint.x) * Math.sin(apexAngle) * 2 / width;
        }

        public int getDraggedDirection(Point sourcePoint, Point destPoint) {
            return (destPoint.x - sourcePoint.x) > 0 ? FORWARD : BACKWARD;
        }

        public void setValidateSize() {
            insets = getInsets(insets);
            setPreferredSize(new Dimension(insets.left + insets.right + 100,
                    insets.bottom + insets.top + 10));
            setMinimumSize(new Dimension(insets.left + insets.right + 10,
                    insets.bottom + insets.top + 10));
        }
    }

    class VerticalPainter extends JogglePainter {
        public int getJoggleWidth() {
            return getHeight() - insets.bottom - insets.top;
        }

        public int getJoggleHeight() {
            return getWidth() - insets.left - insets.right;
        }

        public void drawLine(Graphics2D g2D, float x1, float y1, float x2, float y2) {
            g2D.draw(new Line2D.Float(y1, x1, y2, x2));
        }

        public void fillRect(Graphics2D g2D, float x, float y, float width, float height) {
            g2D.fill(new Rectangle2D.Float(y, x, height, width));
        }

        public double getDraggedAngle(Point sourcePoint, Point destPoint) {
            return (destPoint.y - sourcePoint.y) * Math.sin(apexAngle) * 2 / width;
        }

        public int getDraggedDirection(Point sourcePoint, Point destPoint) {
            return (destPoint.y - sourcePoint.y) > 0 ? FORWARD : BACKWARD;
        }

        public void setValidateSize() {
            insets = getInsets(insets);
            setPreferredSize(new Dimension(insets.left + insets.right + 10,
                    insets.bottom + insets.top + 100));
            setMinimumSize(new Dimension(insets.left + insets.right + 10,
                    insets.bottom + insets.top + 10));
        }
    }

    //-----event-handling-----

    /**
     * Adds a listener to the list that is notified each time a change to the
     * model occurs.
     *
     * @param l the ChangeListener to add
     * @see #setValue(doublevalue)
     */
    public void addChangeListener(ChangeListener l) {
        listenerList.add(ChangeListener.class, l);
    }

    /**
     * Removes a ChangeListener from this Joggle.
     *
     * @param l the listener to remove.
     */
    public void removeChangeListener(ChangeListener l) {
        listenerList.remove(ChangeListener.class, l);
    }

    /**
     * Sends a ChangeEvent, whose source is this Joggle, to each listener which was
     * added to this Joggle. This method is called each time a ChangeEvent is
     * received from the model.
     *
     * @see #addChangeListener(ChangeListenerl)
     */
    protected void fireStateChanged() {
        Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == ChangeListener.class) {
                if (changeEvent == null)
                    changeEvent = new ChangeEvent(this);
                ((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
            }
        }
    }
}

