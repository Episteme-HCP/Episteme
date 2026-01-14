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

import java.text.NumberFormat;

import javax.media.j3d.*;

import javax.vecmath.Vector4f;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Texture3DVolume extends TextureVolume {
    /** DOCUMENT ME! */
    TexCoordGeneration tg = new TexCoordGeneration();

    /** DOCUMENT ME! */
    Texture3D texture;

    /** DOCUMENT ME! */
    ColorModel colorModel;

    /** DOCUMENT ME! */
    WritableRaster raster;

    /** DOCUMENT ME! */
    boolean timing = false;

    /** DOCUMENT ME! */
    NumberFormat numFormatter = null;

/**
     * Creates a new Texture3DVolume object.
     *
     * @param context DOCUMENT ME!
     * @param volume  DOCUMENT ME!
     */
    public Texture3DVolume(Context context, Volume volume) {
        super(context, volume);
    }

    /**
     * DOCUMENT ME!
     */
    void loadTexture() {
        System.out.print("Loading 3D texture map");

        // set up the texture coordinate generation
        tg = new TexCoordGeneration();
        tg.setFormat(TexCoordGeneration.TEXTURE_COORDINATE_3);
        tg.setPlaneS(new Vector4f(volume.xTexGenScale, 0.0f, 0.0f, 0.0f));
        tg.setPlaneT(new Vector4f(0.0f, volume.yTexGenScale, 0.0f, 0.0f));
        tg.setPlaneR(new Vector4f(0.0f, 0.0f, volume.zTexGenScale, 0.0f));

        if (useCmap) {
            colorModel = ColorModel.getRGBdefault();
        } else {
            ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
            int[] nBits = { 8 };
            colorModel = new ComponentColorModel(cs, nBits, false, false,
                    Transparency.TRANSLUCENT, DataBuffer.TYPE_BYTE);
        }

        int sSize = volume.xTexSize;
        int tSize = volume.yTexSize;
        int rSize = volume.zTexSize;

        raster = colorModel.createCompatibleWritableRaster(sSize, tSize);

        BufferedImage bImage = new BufferedImage(colorModel, raster, false, null);

        int[] intData = null;
        byte[] byteData = null;
        ImageComponent3D pArray;

        if (useCmap) {
            intData = ((DataBufferInt) raster.getDataBuffer()).getData();
            texture = new Texture3D(Texture.BASE_LEVEL, Texture.RGBA, sSize,
                    tSize, rSize);
            pArray = new ImageComponent3D(ImageComponent.FORMAT_RGBA, sSize,
                    tSize, rSize);
        } else {
            byteData = ((DataBufferByte) raster.getDataBuffer()).getData();
            texture = new Texture3D(Texture.BASE_LEVEL, Texture.INTENSITY,
                    sSize, tSize, rSize);
            pArray = new ImageComponent3D(ImageComponent.FORMAT_CHANNEL8,
                    sSize, tSize, rSize);
        }

        for (int i = 0; i < volume.zDim; i++) {
            if (useCmap) {
                volume.loadZRGBA(i, intData, cmap);
            } else {
                volume.loadZIntensity(i, byteData);
            }

            pArray.set(i, bImage);

            System.out.print(".");
        }

        texture.setImage(0, pArray);
        texture.setEnable(true);
        texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
        texture.setBoundaryModeS(Texture.CLAMP);
        texture.setBoundaryModeT(Texture.CLAMP);
        texture.setBoundaryModeR(Texture.CLAMP);

        // TODO: reset after load...
        System.out.println("done");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Texture3D getTexture() {
        return texture;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    TexCoordGeneration getTexGen() {
        return tg;
    }
}
