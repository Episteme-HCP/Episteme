package org.jscience.chemistry.gui.extended.jviewer;

import org.jscience.chemistry.gui.extended.molecule.Matrix3D;

import java.awt.*;
import java.util.Vector;

/**
 * This class provides a viewer which can render and manipulate one or more
 * graphics objects.
 * <p/>
 * Note: Only the reference to the Tripos Java molecule's Matrix3D class keeps
 * jviewer from being a true "object" renderer.  A future effort should
 * be to move Matrix3D from molecule to JViewer.
 *
 * @author Mike Brusati (brusati@tripos.com)
 *         Original Version
 * @author Mike Brusati (brusati@tripos.com)
 *         Modified to apply a negative y scale at paint time, resulting in a flip
 *         of the displayed object about the x axis.  JViewer assumes the objects it
 *         is to display are defined in world coordinates, where the origin is in
 *         the center of the world and y increases "upwards".  Since the JViewer
 *         (and all other Java AWT components) define a coordinate system with the
 *         origin in the upper left corner and y increasing "downwards", it is
 *         necessary to invert y to keep the display consistent across coordinate
 *         systems.
 * @see IEvntHandler
 * @see IRenderer
 */
public class JViewer extends Panel {
    /**
     * DOCUMENT ME!
     */
    public static final int TRANSLATE = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int ROTATE_XY = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int ROTATE_Z = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int SCALE = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int NO_TRANSFORM = 4;
    protected Vector renderers = new Vector();
    protected Matrix3D localMat;
    protected Matrix3D panelMat;
    protected Color backgroundColor = Color.white;
    protected int transformMode = TRANSLATE;
    protected float xTrans;
    protected float yTrans;
    protected float zTrans;
    protected IEvntHandler eHandler = null;
    protected Dimension dim;
    protected Dimension offDim = new Dimension(0, 0);
    protected Graphics theGr;
    protected Image offImage = null;
    private float objDX;
    private float objDY;
    private float objDZ;
    private float minDim;
    private float maxDelta;
    protected float scaleFactor;
    protected boolean needRescale = false;
    //protected Picker picker = null;

    /**
     * The default constructor.
     */
    public JViewer() {
        this(500, 500);
    }

    /**
     * Constructor allowing size to be specified.
     *
     * @param w width of the JViewer
     * @param h height of the JViewer
     */
    public JViewer(int w, int h) {
        dim = new Dimension(w, h);
        setBackground(backgroundColor);
        localMat = new Matrix3D();
        panelMat = new Matrix3D();
    }

    /**
     * A copy constructor.
     *
     * @param jviewer the JViewer being copied
     */
    public JViewer(JViewer jviewer) {
        if (jviewer == null) {
            jviewer = new JViewer();
        }

        int size = jviewer.renderers.size();
        renderers = new Vector(size);

        for (int i = 0; i < size; i++) {
            IRenderer r = (IRenderer) jviewer.renderers.elementAt(i);
            renderers.addElement((IRenderer) r.copy());
        }

        localMat = (Matrix3D) jviewer.localMat.clone();
        panelMat = (Matrix3D) jviewer.panelMat.clone();
        backgroundColor = new Color(jviewer.backgroundColor.getRGB());
        transformMode = jviewer.transformMode;
        xTrans = jviewer.xTrans;
        yTrans = jviewer.yTrans;
        zTrans = jviewer.zTrans;
        eHandler = jviewer.eHandler;
        dim = new Dimension(jviewer.dim);
        offDim = new Dimension(jviewer.offDim);
        theGr = (jviewer.theGr == null) ? null : jviewer.theGr.create();
        offImage = null;
        objDX = jviewer.objDX;
        objDY = jviewer.objDY;
        objDZ = jviewer.objDZ;
        minDim = jviewer.minDim;
        maxDelta = jviewer.maxDelta;
        scaleFactor = jviewer.scaleFactor;
        needRescale = jviewer.needRescale;
        // picker = jviewer.picker;
    }

    /**
     * Set the picker which associates with this viewer
     *
     * @param picker Picker
     *               <p/>
     *               Indicate the minimum size for a JViewer.
     *               <p/>
     *               Indicate the minimum size for a JViewer.
     *               <p/>
     *               Indicate the minimum size for a JViewer.
     *               <p/>
     *               Indicate the minimum size for a JViewer.
     *               <p/>
     *               Indicate the minimum size for a JViewer.
     */
    // public void setPicker(Picker picker) {
    //   this.picker = picker;
    // }

    /**
     * Indicate the minimum size for a JViewer.
     */
    public Dimension minimumSize() {
        return new Dimension(dim);
    }

    /**
     * Indicate the preferred size for a JViewer.
     */
    public Dimension preferredSize() {
        return new Dimension(dim);
    }

    /**
     * Reset the view to its non-transformed state.
     */
    public void resetView() {
        panelMat.unit();
    }

    /**
     * Set the background color.
     *
     * @param bg the background color
     * @see getBackgroundColor
     */
    public void setBackgroundColor(Color bg) {
        backgroundColor = bg;
        setBackground(backgroundColor);
    }

    /**
     * Get the background color.
     *
     * @return the background color
     * @see setBackgroundColor
     */
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Set the transformation mode.
     *
     * @param mode one of TRANSLATE, ROTATE_XY, ROTATE_Z, SCALE, or NO_TRANSFORM
     * @throw IllegalArgumentException if mode is not a valid value
     */
    public void setTransformMode(int mode) {
        switch (mode) {
            case TRANSLATE:
            case ROTATE_XY:
            case ROTATE_Z:
            case SCALE:
            case NO_TRANSFORM:
                transformMode = mode;

                return;

            default:

                String errorMsg =
                        "Argument sent to tripos.jviewer.JViewer::setTransformMode(int) " +
                                "is not one of TRANSLATE, ROTATE_XY, ROTATE_Z, SCALE, or " +
                                "NO_TRANSFORM defined in tripos.jviewer.JViewer ";
                throw (new IllegalArgumentException(errorMsg));
        }
    }

    /**
     * Return the transformation mode.
     */
    public int getTransformMode() {
        return transformMode;
    }

    /**
     * Establish a new object renderer for the viewer to use.
     *
     * @param renderer an implementation of the IRenderer interface
     * @see IRenderer
     */
    public void setRenderer(IRenderer renderer) {
        removeAllRenderers();
        addRenderer(renderer);
    }

    /**
     * Return the list of renderers
     */
    public Vector getIRenderers() {
        return renderers;
    }

    /**
     * Add another object renderer for the viewer to use.  This allows the
     * viewer to render and manipulate multiple objects at once.
     *
     * @param renderer an implementation of the IRenderer interface
     * @see IRenderer
     */
    public void addRenderer(IRenderer renderer) {
        if (renderers.contains(renderer)) {
            return;
        }

        renderers.addElement(renderer);
    }

    /**
     * Remove the specified object renderer from the viewer's list.
     *
     * @param renderer an implementation of the IRenderer interface
     * @return true if the object renderer was removed else false
     * @see IRenderer
     */
    public boolean removeRenderer(IRenderer renderer) {
        return renderers.removeElement(renderer);
    }

    /**
     * Remove all object renderers.
     *
     * @see IRenderer
     */
    public void removeAllRenderers() {
        renderers.removeAllElements();
    }

    /**
     * Set the rescale flag so a new scale factor gets computed at paint time.
     * This state will remain active until after the next paint.  This method is
     * useful when the application wants to change the contents of the viewer,
     * since recomputation of the scale factor is required to fit the new
     * contents to the gui.  If not used, the scale factor will only get
     * recomputed when the viewer's size changes.
     */
    public void markForRescale() {
        needRescale = true;
    }

    /**
     * Determine the new scale factor to be applied to the graphics objects in
     * response to a change in viewer size (or contents if the application
     * called markForRescale to force a rescaling).
     *
     * @see IRenderer
     * @see markForResize
     */
    protected void computeScale() {
        float[] bb;
        float xmin;
        float xmax;
        float ymin;
        float ymax;
        float zmin;
        float zmax;
        float dx;
        float dy;
        float dz;

        xmin = ymin = zmin = 2000.0f;
        xmax = ymax = zmax = -2000.0f;

        objDX = objDY = objDZ = 0.0f;

        //
        // Determine the bounds encompassing all graphics objects.
        //
        for (int i = 0; i < renderers.size(); i++) {
            IRenderer renderer = (IRenderer) renderers.elementAt(i);

            bb = renderer.getBBox();

            //
            // A null bounding box indicates an empty object.  Ignore these.
            //
            if (bb == null) {
                continue;
            }

            dx = bb[1] - bb[0];
            dy = bb[3] - bb[2];
            dz = bb[5] - bb[4];

            xmin = Math.min(xmin, bb[0]);
            ymin = Math.min(ymin, bb[2]);
            zmin = Math.min(zmin, bb[4]);
            xmax = Math.max(xmax, bb[1]);
            ymax = Math.max(ymax, bb[3]);
            zmax = Math.max(zmax, bb[5]);

            objDX = Math.max(objDX, dx);
            objDY = Math.max(objDY, dy);
            objDZ = Math.max(objDZ, dz);
        }

        xTrans = (xmin + xmax) / 2.0f;
        yTrans = (ymin + ymax) / 2.0f;
        zTrans = (zmin + zmax) / 2.0f;

        //
        // Compute the scale factor to be applied to all graphics objects at paint
        // time.  All objects will be rendered at the same scale.
        //
        minDim = (float) (Math.min(dim.width, dim.height)) * 0.8f;
        maxDelta = Math.max(Math.max(objDX, objDY), objDZ);
        scaleFactor = (maxDelta == 0.0f) ? 1.0f : (minDim / maxDelta);
    }

    /**
     * Register an event handler with the gui.  The viewer does not handle
     * events.  All events are dispatched to the an event handler if registered,
     * else to the viewer's super class.
     *
     * @param eh an implementation of the IEvntHandler interface
     * @see IEvntHandler
     */
    public void registerEHandler(IEvntHandler eh) {
        eHandler = eh;
    }

    /**
     * Update the viewer's dimension information in response to a resizing.
     *
     * @param d the new dimension
     */
    public void resize(Dimension d) {
        super.resize(d);

        dim = new Dimension(d);
    }

    /**
     * Update the viewer's dimension information in response to a resizing.
     *
     * @param w the new dimension width
     * @param h the new dimension height
     */
    public void resize(int w, int h) {
        super.resize(w, h);

        dim = new Dimension(w, h);
    }

    /**
     * Update the viewer's dimension information in response to a reshaping.
     *
     * @param x x coordinate of the JViewer's origin
     * @param y y coordinate of the JViewer's origin
     * @param w width of the JViewer
     * @param h height of the JViewer
     */
    public synchronized void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);

        //
        // The layout manager will call this function each time one of the JViewer's
        // ancestor's is laid out.  But the JViewer's size (w and h) will be 0 x 0
        // until its parent has been positioned and sized.  Therefore calls to this
        // method are ignored until size is greater than 0.
        //
        if ((w <= 0) || (h <= 0)) {
            return;
        }

        dim = new Dimension(w, h);
    }

    /**
     * Render all objects known by the gui.
     *
     * @param g the graphics context
     */
    public void paint(Graphics g) {
        int nRenderers = renderers.size();

        //
        // If there's nothing to render, just clear the viewer and return.
        //
        if (nRenderers == 0) {
            g.clearRect(0, 0, dim.width, dim.height);
            needRescale = false;

            return;
        }

        //
        // If the viewer's size changed, compute a new off screen image buffer and
        // recompute the scale factor.  Else if the application is forcing a rescale,
        // just recompute the scale factor.  Note that if an off screen buffer cannot
        // be obtained, the viewer just draws to the screen.
        //
        if ((offImage == null) || (offDim.width != dim.width) ||
                (offDim.height != dim.height)) {
            offImage = createImage(dim.width, dim.height);

            if (offImage != null) {
                theGr = offImage.getGraphics();
                offDim = new Dimension(dim.width, dim.height);
            } else {
                theGr = g;
            }

            computeScale();
        } else if (needRescale) {
            computeScale();
        }

        theGr.setColor(backgroundColor);
        theGr.fillRect(0, 0, dim.width, dim.height);

        //
        // Transform each graphics object and draw it to the off screen image buffer.
        //
        for (int i = 0; i < nRenderers; i++) {
            IRenderer renderer = (IRenderer) renderers.elementAt(i);

            renderer.matUnit();
            renderer.matTranslate(-xTrans, -yTrans, -zTrans);
            renderer.matMult(panelMat);
            renderer.matScale(scaleFactor, -scaleFactor, scaleFactor);
            renderer.matTranslate((float) (dim.width / 2),
                    (float) (dim.height / 2), 0.0f);
            renderer.matTransform();
            renderer.draw(theGr);
        }

        //
        // give picker a chance to draw, it can choose to ignore by doing nothing.
        //
        //if (picker != null) {
        //  picker.draw(theGr);
        //}

        //
        // If an off screen buffer was created, copy its contents to the screen, else
        // the viewer viewer will have already drawn to the screen.
        //
        if (offImage != null) {
            g.drawImage(offImage, 0, 0, this);
        }

        needRescale = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void update(Graphics g) {
        paint(g);
    }

    /**
     * Dispatch any event to the event handler and/or picker, if registered.
     *
     * @param e an event.
     */
    public boolean handleEvent(Event e) {
        boolean isHandledOk = false;

        //todo: we need support for the new picking system
        //if (picker != null) {
        //isHandledOk = picker.handlePickEvent(e);
        // }

        //
        // Send the event to eHandler (who manages rotate, etc.) if the
        // event is not a picking.
        //
        if (!isHandledOk && (eHandler != null)) {
            isHandledOk = eHandler.handleEvnt(e);
        }

        //
        // The event will be dispatched back to super (Panel).
        // The super will dispatched it to application, giving application a
        // chance to act on its need.
        //
        return super.handleEvent(e);
    }

    /**
     * Apply the current transformation in response to a mouse movement.
     *
     * @param dx amount of movement in x
     * @param dy amount of movement in y
     * @return true iff successfully transform the renderers
     */
    public boolean transform(int dx, int dy) {
        if ((renderers.size() == 0) || (transformMode == NO_TRANSFORM)) {
            return false;
        }

        localMat.unit();

        switch (transformMode) {
            case TRANSLATE:
                localMat.translate((maxDelta * ((float) (-dx) / minDim)),
                        (maxDelta * ((float) (dy) / minDim)), 0.0f);

                break;

            case ROTATE_XY:
                localMat.xrot((float) (dy) * (360.0f / minDim));
                localMat.yrot((float) (-dx) * (360.0f / minDim));

                break;

            case ROTATE_Z:
                localMat.zrot((float) (-dx) * (360.0f / minDim));

                break;

            case SCALE:

                double m;
                int d = dx - dy;

                try {
                    m = Math.sqrt((double) (dx * dx) + (double) (dy * dy));
                } catch (ArithmeticException e) {
                    m = 1.0;
                }

                if ((d < 0) || ((d == 0) && (dx < 0))) {
                    m = -m;
                }

                localMat.scale(1.0f - ((2.0f / minDim) * (float) m));

                break;
        }

        panelMat.mult(localMat);

        return true;
    }
} // end of class JViewer
