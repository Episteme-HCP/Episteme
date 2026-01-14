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

package org.jscience.earth.geosphere;

/**
 * Something that can be recognized (visible, acts as a unit) and lasts for
 * long. Can actually be quite large.
 */
import javax.media.j3d.Bounds;
import javax.media.j3d.Group;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public abstract class LandFeature extends Group {
    /** DOCUMENT ME! */
    private Transform3D position;

    /** DOCUMENT ME! */
    private GeosphereBranchGroup geosphereBranchGroup; //the branchGroup this Event is added to

/**
     * Creates a new LandFeature object.
     *
     * @param position             DOCUMENT ME!
     * @param bounds               DOCUMENT ME!
     * @param geosphereBranchGroup DOCUMENT ME!
     */
    public LandFeature(Transform3D position, Bounds bounds,
        GeosphereBranchGroup geosphereBranchGroup) {
        TransformGroup transformGroup;

        transformGroup = new TransformGroup(position);
        transformGroup.addChild(this);
        setBounds(bounds);
        geosphereBranchGroup.addChild(transformGroup);
    }

    //given place
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Transform3D getPosition() {
        Transform3D transform3D;

        transform3D = new Transform3D();

        return ((TransformGroup) getParent()).getTransform(transform3D);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GeosphereBranchGroup getGeosphereBranchGroup() {
        return getParent().getParent();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract ClimaticEvent fireEvent();
}
