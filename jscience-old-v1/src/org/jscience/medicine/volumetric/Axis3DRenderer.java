/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.jscience.medicine.volumetric;

import javax.media.j3d.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Axis3DRenderer extends AxisRenderer {
    /** DOCUMENT ME! */
    Texture3DVolume texVol;

/**
     * Creates a new Axis3DRenderer object.
     *
     * @param view    DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param vol     DOCUMENT ME!
     */
    public Axis3DRenderer(View view, Context context, Volume vol) {
        super(view, context, vol);
        texVol = new Texture3DVolume(context, vol);
    }

    /**
     * DOCUMENT ME!
     */
    void update() {
        int texVolUpdate = texVol.update();

        switch (texVolUpdate) {
        case Texture2DVolume.RELOAD_NONE:
            fullReloadNeeded = false;
            tctReloadNeeded = false;

            break;

        case Texture2DVolume.RELOAD_VOLUME:
            fullReloadNeeded = true;
            tctReloadNeeded = false;

            break;

        case Texture2DVolume.RELOAD_CMAP:
            fullReloadNeeded = false;
            tctReloadNeeded = true;

            break;
        }

        updateDebugAttrs();

        if (fullReloadNeeded) {
            fullReload();
        }

        if (tctReloadNeeded) {
            tctReload();
        }
    }

    /**
     * DOCUMENT ME!
     */
    void fullReload() {
        clearData();

        if (volume.hasData()) {
            System.out.print("Loading quads...");
            loadQuads();
            tctReload();
            System.out.println("done");
        }

        setWhichChild();

        fullReloadNeeded = false;
    }

    /**
     * DOCUMENT ME!
     */
    void tctReload() {
        if (texVol.useTextureColorTable) {
            texAttr.setTextureColorTable(texVol.texColorMap);
        } else {
            texAttr.setTextureColorTable(null);
        }

        tctReloadNeeded = false;
    }

    /**
     * DOCUMENT ME!
     */
    void loadQuads() {
        loadAxis(Z_AXIS);
        loadAxis(Y_AXIS);
        loadAxis(X_AXIS);
    }

    /**
     * DOCUMENT ME!
     *
     * @param axis DOCUMENT ME!
     */
    private void loadAxis(int axis) {
        OrderedGroup frontGroup = null;
        OrderedGroup backGroup = null;
        int rSize = 0;

        switch (axis) {
        case Z_AXIS:
            frontGroup = (OrderedGroup) axisSwitch.getChild(axisIndex[Z_AXIS][FRONT]);
            backGroup = (OrderedGroup) axisSwitch.getChild(axisIndex[Z_AXIS][BACK]);
            rSize = volume.zDim;
            setCoordsZ();

            break;

        case Y_AXIS:
            frontGroup = (OrderedGroup) axisSwitch.getChild(axisIndex[Y_AXIS][FRONT]);
            backGroup = (OrderedGroup) axisSwitch.getChild(axisIndex[Y_AXIS][BACK]);
            rSize = volume.yDim;
            setCoordsY();

            break;

        case X_AXIS:
            frontGroup = (OrderedGroup) axisSwitch.getChild(axisIndex[X_AXIS][FRONT]);
            backGroup = (OrderedGroup) axisSwitch.getChild(axisIndex[X_AXIS][BACK]);
            rSize = volume.xDim;
            setCoordsX();

            break;
        }

        for (int i = 0; i < rSize; i++) {
            switch (axis) {
            case Z_AXIS:
                setCurCoordZ(i);

                break;

            case Y_AXIS:
                setCurCoordY(i);

                break;

            case X_AXIS:
                setCurCoordX(i);

                break;
            }

            Appearance a = new Appearance();
            a.setMaterial(m);
            a.setTransparencyAttributes(t);
            a.setTextureAttributes(texAttr);
            a.setTexture(texVol.getTexture());
            a.setTexCoordGeneration(texVol.getTexGen());

            if (dbWriteEnable == false) {
                RenderingAttributes r = new RenderingAttributes();
                r.setDepthBufferWriteEnable(dbWriteEnable);
                a.setRenderingAttributes(r);
            }

            a.setPolygonAttributes(p);

            QuadArray quadArray = new QuadArray(4, GeometryArray.COORDINATES);
            quadArray.setCoordinates(0, quadCoords);

            Shape3D frontShape = new Shape3D(quadArray, a);

            BranchGroup frontShapeGroup = new BranchGroup();
            frontShapeGroup.setCapability(BranchGroup.ALLOW_DETACH);
            frontShapeGroup.addChild(frontShape);
            frontGroup.addChild(frontShapeGroup);

            Shape3D backShape = new Shape3D(quadArray, a);

            BranchGroup backShapeGroup = new BranchGroup();
            backShapeGroup.setCapability(BranchGroup.ALLOW_DETACH);
            backShapeGroup.addChild(backShape);
            backGroup.insertChild(backShapeGroup, 0);
        }
    }
}
