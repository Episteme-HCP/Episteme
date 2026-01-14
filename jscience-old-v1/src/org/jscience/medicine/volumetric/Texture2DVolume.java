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

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;

import javax.media.j3d.*;

import javax.vecmath.Vector4f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Texture2DVolume extends TextureVolume {
    // sets of textures, one for each axis, sizes of the arrays are set
    // by the dimensions of the Volume
    /** DOCUMENT ME! */
    Texture2D[] xTextures;

    /** DOCUMENT ME! */
    Texture2D[] yTextures;

    /** DOCUMENT ME! */
    Texture2D[] zTextures;

    /** DOCUMENT ME! */
    TexCoordGeneration xTg = new TexCoordGeneration();

    /** DOCUMENT ME! */
    TexCoordGeneration yTg = new TexCoordGeneration();

    /** DOCUMENT ME! */
    TexCoordGeneration zTg = new TexCoordGeneration();

    /** DOCUMENT ME! */
    ColorModel colorModel;

    /** DOCUMENT ME! */
    WritableRaster raster;

/**
     * Creates a new Texture2DVolume object.
     *
     * @param context DOCUMENT ME!
     * @param volume  DOCUMENT ME!
     */
    public Texture2DVolume(Context context, Volume volume) {
        super(context, volume);
    }

    /**
     * DOCUMENT ME!
     */
    void loadTexture() {
        if (useCmap) {
            colorModel = ColorModel.getRGBdefault();
        } else {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            int[] nBits = { 8 };
            colorModel = new ComponentColorModel(cs, nBits, false, false,
                    Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        }

        long start = 0;

        if (timing) {
            start = System.currentTimeMillis();
        }

        System.out.print("Loading Z axis texture maps");
        loadAxis(Z_AXIS);
        System.out.println("done");
        System.out.print("Loading Y axis texture maps");
        loadAxis(Y_AXIS);
        System.out.println("done");
        System.out.print("Loading X axis texture maps");
        loadAxis(X_AXIS);
        System.out.println("done");

        //if (timing) {
        //    long end = System.currentTimeMillis();
        //    double elapsed = (end - start) / 1000.0;
        //    System.out.println("Load took " + numFormat(elapsed) +
        //		" seconds");
        //}
    }

    /**
     * DOCUMENT ME!
     *
     * @param axis DOCUMENT ME!
     */
    private void loadAxis(int axis) {
        int rSize = 0; // number of tex maps to create
        int sSize = 0; // s,t = size of texture map to create
        int tSize = 0;
        Texture2D[] textures = null;

        switch (axis) {
        case Z_AXIS:
            rSize = volume.zDim;
            sSize = volume.xTexSize;
            tSize = volume.yTexSize;
            textures = zTextures = new Texture2D[rSize];
            zTg = new TexCoordGeneration();
            zTg.setPlaneS(new Vector4f(volume.xTexGenScale, 0.0f, 0.0f, 0.0f));
            zTg.setPlaneT(new Vector4f(0.0f, volume.yTexGenScale, 0.0f, 0.0f));

            break;

        case Y_AXIS:
            rSize = volume.yDim;
            sSize = volume.xTexSize;
            tSize = volume.zTexSize;
            textures = yTextures = new Texture2D[rSize];
            yTg = new TexCoordGeneration();
            yTg.setPlaneS(new Vector4f(volume.xTexGenScale, 0.0f, 0.0f, 0.0f));
            yTg.setPlaneT(new Vector4f(0.0f, 0.0f, volume.zTexGenScale, 0.0f));

            break;

        case X_AXIS:
            rSize = volume.xDim;
            sSize = volume.yTexSize;
            tSize = volume.zTexSize;
            textures = xTextures = new Texture2D[rSize];
            xTg = new TexCoordGeneration();
            xTg.setPlaneS(new Vector4f(0.0f, volume.yTexGenScale, 0.0f, 0.0f));
            xTg.setPlaneT(new Vector4f(0.0f, 0.0f, volume.zTexGenScale, 0.0f));

            break;
        }

        raster = colorModel.createCompatibleWritableRaster(sSize, tSize);

        BufferedImage bImage = new BufferedImage(colorModel, raster, false, null);

        int[] intData = null;
        byte[] byteData = null;

        if (useCmap) {
            intData = ((DataBufferInt) raster.getDataBuffer()).getData();
        } else {
            byteData = ((DataBufferByte) raster.getDataBuffer()).getData();
        }

        for (int i = 0; i < rSize; i++) {
            switch (axis) {
            case Z_AXIS:

                if (useCmap) {
                    volume.loadZRGBA(i, intData, cmap);
                } else {
                    volume.loadZIntensity(i, byteData);
                }

                break;

            case Y_AXIS:

                if (useCmap) {
                    volume.loadYRGBA(i, intData, cmap);
                } else {
                    volume.loadYIntensity(i, byteData);
                }

                break;

            case X_AXIS:

                if (useCmap) {
                    volume.loadXRGBA(i, intData, cmap);
                } else {
                    volume.loadXIntensity(i, byteData);
                }

                break;
            }

            Texture2D tex;
            ImageComponent2D pArray;

            if (useCmap) {
                tex = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, sSize,
                        tSize);
                pArray = new ImageComponent2D(ImageComponent.FORMAT_RGBA,
                        sSize, tSize);
            } else {
                tex = new Texture2D(Texture.BASE_LEVEL, Texture.INTENSITY,
                        sSize, tSize);
                pArray = new ImageComponent2D(ImageComponent.FORMAT_CHANNEL8,
                        sSize, tSize);
            }

            pArray.set(bImage);

            tex.setImage(0, pArray);
            tex.setEnable(true);
            tex.setMinFilter(Texture.BASE_LEVEL_LINEAR);
            tex.setMagFilter(Texture.BASE_LEVEL_LINEAR);

            //tex.setMinFilter(Texture.BASE_LEVEL_POINT);
            //tex.setMagFilter(Texture.BASE_LEVEL_POINT);
            tex.setBoundaryModeS(Texture.CLAMP);
            tex.setBoundaryModeT(Texture.CLAMP);

            textures[i] = tex;
            System.out.print(".");
        }
    }
}
