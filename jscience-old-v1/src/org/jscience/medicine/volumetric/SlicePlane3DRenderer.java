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


//repackaged after the code available at http://www.j3d.org/tutorials/quick_fix/volume.html
//author: Doug Gehringer
//email:Doug.Gehringer@sun.com
import javax.media.j3d.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class SlicePlane3DRenderer extends SlicePlaneRenderer {
    /** DOCUMENT ME! */
    Texture3DVolume texVol;

    /** DOCUMENT ME! */
    Appearance appearance;

    /** DOCUMENT ME! */
    TextureAttributes texAttr;

    /** DOCUMENT ME! */
    Shape3D shape;

/**
     * Creates a new SlicePlane3DRenderer object.
     *
     * @param view    DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param vol     DOCUMENT ME!
     */
    public SlicePlane3DRenderer(View view, Context context, Volume vol) {
        super(view, context, vol);
        texVol = new Texture3DVolume(context, vol);

        TransparencyAttributes transAttr = new TransparencyAttributes();
        transAttr.setTransparencyMode(TransparencyAttributes.BLENDED);
        texAttr = new TextureAttributes();
        texAttr.setTextureMode(TextureAttributes.MODULATE);
        texAttr.setCapability(TextureAttributes.ALLOW_COLOR_TABLE_WRITE);

        Material m = new Material();
        m.setLightingEnable(false);

        PolygonAttributes p = new PolygonAttributes();
        p.setCullFace(PolygonAttributes.CULL_NONE);
        p.setPolygonOffset(1.0f);
        p.setPolygonOffsetFactor(1.0f);
        appearance = new Appearance();
        appearance.setMaterial(m);
        appearance.setTextureAttributes(texAttr);
        appearance.setTransparencyAttributes(transAttr);
        appearance.setPolygonAttributes(p);
        appearance.setCapability(Appearance.ALLOW_TEXTURE_WRITE);
        appearance.setCapability(Appearance.ALLOW_TEXGEN_WRITE);

        shape = new Shape3D(null, appearance);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
        shape.setCapability(Shape3D.ALLOW_GEOMETRY_WRITE);

        root.addChild(shape);
    }

    /**
     * DOCUMENT ME!
     */
    void setSliceGeo() {
        GeometryArray pgonGeo = null;

        if (numSlicePts > 0) {
            count[0] = numSlicePts;

            pgonGeo = new TriangleFanArray(numSlicePts,
                    TriangleFanArray.COORDINATES, count);
            pgonGeo.setCoordinates(0, slicePts, 0, numSlicePts);
        }

        shape.setGeometry(pgonGeo);
    }

    /**
     * DOCUMENT ME!
     */
    public void update() {
        boolean reloadPgon = false;
        boolean reloadTct = false;
        int texVolUpdate = texVol.update();

        switch (texVolUpdate) {
        case TextureVolume.RELOAD_NONE:
            return;

        case TextureVolume.RELOAD_VOLUME:
            appearance.setTexture(texVol.getTexture());
            appearance.setTexCoordGeneration(texVol.getTexGen());
            reloadPgon = true;
            reloadTct = true;

            break;

        case TextureVolume.RELOAD_CMAP:
            reloadTct = true;

            break;
        }

        if (reloadPgon) {
            //System.out.println("update: reloadPgon true");
            setPlaneGeos();
        } else {
            //System.out.println("update: reloadPgon false");
        }

        if (reloadTct) {
            tctReload();
        }
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
    }
}
