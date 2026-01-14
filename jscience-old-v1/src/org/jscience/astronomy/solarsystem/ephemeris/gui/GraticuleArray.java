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

//this code is rebundled after the code from
//Peter Csapo at http://www.pcsapo.com/csphere/csphere.html
//mailto:peter@pcsapo.com
//website:http://www.pcsapo.com/csphere/csphere.html
//the author agreed we reuse his code under GPL
package org.jscience.astronomy.solarsystem.ephemeris.gui;

import org.jscience.astronomy.solarsystem.ephemeris.Matrix3D;
import org.jscience.astronomy.solarsystem.ephemeris.Vector3;

import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
class GraticuleArray extends StarsArray {
    /** DOCUMENT ME! */
    final double SHRT_MAX = 32767D;

/**
     * Creates a new GraticuleArray object.
     */
    GraticuleArray() {
        super.stars = new StarPoint[12600];

        int i = 0;

        do {
            short word0 = (short) (int) (32767D * Math.cos((double) (i + 3) / 50D));
            short word1 = 0;
            short word2 = (short) (int) (32767D * Math.sin((double) (i + 3) / 50D));
            Color color = Color.white;
            byte byte0 = 0;
            super.stars[i] = new StarPoint(word1, word0, word2, color, byte0);
        } while (++i < 72);

        i = 0;

        do {
            super.stars[143 - i] = new StarPoint(super.stars[i]);
            super.stars[143 - i].y = (short) (-super.stars[143 - i].y);
        } while (++i < 72);

        Vector3 vector3 = new Vector3(0.0D, 0.0D, 0.0D);
        int j = 1;

        do {
            Matrix3D matrix3d = Matrix3D.rotZ(((double) j * 3.1415926535897931D) / 18D);
            int k = 0;

            do {
                vector3.set(super.stars[k].x, super.stars[k].y, super.stars[k].z);
                matrix3d.transform(vector3);
                super.stars[(j * 144) + k] = new StarPoint((short) (int) vector3.x,
                        (short) (int) vector3.y, (short) (int) vector3.z,
                        Color.white, (byte) 0);
            } while (++k < 144);
        } while (++j < 36);

        super.count = 5184;
    }
}
