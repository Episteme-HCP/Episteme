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
/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class TextureVolume implements VolRendConstants {
    /** DOCUMENT ME! */
    static final int RELOAD_NONE = 0;

    /** DOCUMENT ME! */
    static final int RELOAD_VOLUME = 1;

    /** DOCUMENT ME! */
    static final int RELOAD_CMAP = 2;

    /** DOCUMENT ME! */
    static final int RED = 0;

    /** DOCUMENT ME! */
    static final int GREEN = 1;

    /** DOCUMENT ME! */
    static final int BLUE = 2;

    /** DOCUMENT ME! */
    static final int ALPHA = 3;

    // Attrs we care about
    /** DOCUMENT ME! */
    ColormapChoiceAttr colorModeAttr;

    /** DOCUMENT ME! */
    ToggleAttr texColorMapAttr;

    /** DOCUMENT ME! */
    Volume volume;

    /** DOCUMENT ME! */
    Colormap cmap = null;

    /** DOCUMENT ME! */
    boolean useCmap = false;

    /** DOCUMENT ME! */
    boolean useTextureColorTable = false;

    /** DOCUMENT ME! */
    boolean texColorMapEnable = false;

    /** DOCUMENT ME! */
    boolean texColorMapAvailable = true;

    /** DOCUMENT ME! */
    int[][] texColorMap;

    // cache for update()
    /** DOCUMENT ME! */
    int volEditId = -1;

    /** DOCUMENT ME! */
    int cmapEditId = -1;

    /** DOCUMENT ME! */
    boolean timing = false;

    //NumberFormat	numFormatter = null;
    /** DOCUMENT ME! */
    boolean volumeReloadNeeded = true;

    /** DOCUMENT ME! */
    boolean tctReloadNeeded = true;

/**
     * Creates a new TextureVolume object.
     *
     * @param context DOCUMENT ME!
     * @param volume  DOCUMENT ME!
     */
    public TextureVolume(Context context, Volume volume) {
        //        timing = Boolean.getBoolean("timing");
        this.volume = volume;
        colorModeAttr = (ColormapChoiceAttr) context.getAttr("Color Mode");
        texColorMapAttr = (ToggleAttr) context.getAttr("Tex Color Map");
        texColorMapEnable = texColorMapAttr.getValue();
        texColorMap = new int[4][];
        texColorMap[0] = new int[256];
        texColorMap[1] = new int[256];
        texColorMap[2] = new int[256];
        texColorMap[3] = new int[256];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int update() {
        int newVolEditId = -1;

        if ((newVolEditId = volume.update()) != volEditId) {
            volEditId = newVolEditId;
            volumeReloadNeeded = true;
        }

        boolean newTexColorMapEnable = texColorMapAttr.getValue();
        int newCmapEditId = -1;

        if (colorModeAttr.getColormap() != cmap) {
            cmap = colorModeAttr.getColormap();

            if (cmap != null) {
                cmapEditId = cmap.update();
            } else {
                cmapEditId = -1;
            }

            if (texColorMapAvailable && texColorMapEnable) {
                tctReloadNeeded = true;
                useCmap = false;
                useTextureColorTable = true;
            } else {
                if (cmap != null) {
                    useCmap = true;
                } else {
                    useCmap = false;
                }

                useTextureColorTable = false;
                volumeReloadNeeded = true;
            }
        } else if ((cmap != null) &&
                (((newCmapEditId = cmap.update()) != cmapEditId) ||
                (texColorMapEnable != newTexColorMapEnable))) {
            if (texColorMapAvailable && newTexColorMapEnable) {
                tctReloadNeeded = true;
                useCmap = false;
                useTextureColorTable = true;

                if (!texColorMapEnable) {
                    /* previously loaded with color table, need to reload
                     * w/o color table
                     */
                    volumeReloadNeeded = true;
                }
            } else {
                useCmap = true;
                useTextureColorTable = false;
                volumeReloadNeeded = true;
            }

            cmapEditId = newCmapEditId;
            texColorMapEnable = newTexColorMapEnable;
        }

        if (volumeReloadNeeded) {
            volumeReload();
            tctReload();

            return RELOAD_VOLUME;
        } else if (tctReloadNeeded) {
            tctReload();

            return RELOAD_CMAP;
        } else {
            return RELOAD_NONE;
        }
    }

    /**
     * DOCUMENT ME!
     */
    void volumeReload() {
        if (volume.hasData()) {
            long start = 0;

            if (timing) {
                start = System.currentTimeMillis();
            }

            loadTexture();

            if (timing) {
                long end = System.currentTimeMillis();
                double elapsed = (end - start) / 1000.0;
                System.out.println("Texture load took " + 
                /*numFormat*/ (elapsed) + " seconds");
            }
        }

        volumeReloadNeeded = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param byteValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int byteToInt(int byteValue) {
        return (int) ((byteValue / 255.0f) * Integer.MAX_VALUE);
    }

    /**
     * DOCUMENT ME!
     */
    void tctReload() {
        if ((cmap != null) && texColorMapEnable) {
            for (int i = 0; i < 256; i++) {
                texColorMap[RED][i] = byteToInt((cmap.colorMapping[i] &
                        0x00ff0000) >> 16);
                texColorMap[GREEN][i] = byteToInt((cmap.colorMapping[i] &
                        0x0000ff00) >> 8);
                texColorMap[BLUE][i] = byteToInt((cmap.colorMapping[i] &
                        0x000000ff));
                texColorMap[ALPHA][i] = byteToInt((cmap.colorMapping[i] &
                        0xff000000) >> 24);
            }

            useTextureColorTable = true;
        } else {
            useTextureColorTable = false;
        }

        tctReloadNeeded = false;
    }

    /**
     * DOCUMENT ME!
     */
    abstract void loadTexture();
}
