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

package org.jscience.astronomy;

import javax.media.j3d.Group;
import javax.media.j3d.Shape3D;


/**
 * The Comet class provides support for the category of AstralBody of the
 * same name.
 *
 * @author Silvere Martin-Michiellot
 * @version 1.0
 */

//a comet orbitting the Star system of the virtual planet

// below is a reprinted text describing comets from http://solarviews.com/eng/comet.htm
//Comets are small, fragile, irregularly shaped bodies composed of a mixture of non-volatile grains and frozen gases. They have highly elliptical orbits that bring them very close to the Sun and swing them deeply into space, often beyond the orbit of Pluto.
//Comet structures are diverse and very dynamic, but they all develop a surrounding cloud of diffuse material, called a coma, that usually grows in size and brightness as the comet approaches the Sun. Usually a small, bright nucleus (less than 10 km in diameter) is visible in the middle of the coma. The coma and the nucleus together constitute the head of the comet.
//As comets approach the Sun they develop enormous tails of luminous material that extend for millions of kilometers from the head, away from the Sun. When far from the Sun, the nucleus is very cold and its material is frozen solid within the nucleus. In this state comets are sometimes referred to as a "dirty iceberg" or "dirty snowball," since over half of their material is ice. When a comet approaches within a few AU of the Sun, the surface of the nucleus begins to warm, and volatiles evaporate. The evaporated molecules boil off and carry small solid particles with them, forming the comet's coma of gas and dust.
//When the nucleus is frozen, it can be seen only by reflected sunlight. However, when a coma develops, dust reflects still more sunlight, and gas in the coma absorbs ultraviolet radiation and begins to fluoresce. At about 5 AU from the Sun, fluorescence usually becomes more intense than reflected light.
//As the comet absorbs ultraviolet light, chemical processes release hydrogen, which escapes the comet's gravity, and forms a hydrogen envelope. This envelope cannot be seen from Earth because its light is absorbed by our atmosphere, but it has been detected by spacecraft.
//The Sun's radiation pressure and solar wind accelerate materials away from the comet's head at differing velocities according to the size and mass of the materials. Thus, relatively massive dust tails are accelerated slowly and tend to be curved. The ion tail is much less massive, and is accelerated so greatly that it appears as a nearly straight line extending away from the comet opposite the Sun. The following view of Comet West shows two distinct tails. The thin blue plasma tail is made up of gases and the broad white tail is made up of microscopic dust particles.
//Each time a comet visits the Sun, it loses some of its volatiles. Eventually, it becomes just another rocky mass in the solar system. For this reason, comets are said to be short-lived, on a cosmological time scale. Many scientists believe that some asteroids are extinct comet nuclei, comets that have lost all of their volatiles.
public abstract class Comet extends AstralBody {
    /**
     * DOCUMENT ME!
     */
    private String composition; //defaults to empty string

    /**
     * DOCUMENT ME!
     */
    private Shape3D ionTail;

    /**
     * DOCUMENT ME!
     */
    private Shape3D hydrogenEnveloppe;

    /**
     * DOCUMENT ME!
     */
    private Shape3D dustTail;

    /**
     * Creates a new Comet object.
     *
     * @param name DOCUMENT ME!
     */
    public Comet(String name) {
        super(name);
        this.composition = new String("");
    }

    /**
     * Creates a new Comet object.
     *
     * @param name DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public Comet(String name, double mass) {
        super(name, mass);
        this.composition = new String("");
    }

    /**
     * Creates a new Comet object.
     *
     * @param name DOCUMENT ME!
     * @param shape DOCUMENT ME!
     * @param mass DOCUMENT ME!
     */
    public Comet(String name, Group shape, double mass) {
        super(name, shape, mass);
        this.composition = new String("");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getComposition() {
        return composition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param composition DOCUMENT ME!
     */
    public void setComposition(String composition) {
        if (composition != null) {
            this.composition = composition;
        } else {
            throw new IllegalArgumentException(
                "You can't set a null composition");
        }
    }

    //either set a group or use the prefered method of setting a ion tail, an hydrogen envelop and a dust tail
    /**
     * DOCUMENT ME!
     */
    private void setGroup() {
        Group group;

        group = new Group();
        group.addChild(getIonTail());
        group.addChild(getHydrogenEnveloppe());
        group.addChild(getDustTail());
        setGroup(group);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape3D getIonTail() {
        return ionTail;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ionTail DOCUMENT ME!
     */
    public void setIonTail(Shape3D ionTail) {
        this.ionTail = ionTail;
        setGroup();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape3D getHydrogenEnveloppe() {
        return hydrogenEnveloppe;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hydrogenEnveloppe DOCUMENT ME!
     */
    public void setHydrogenEnveloppe(Shape3D hydrogenEnveloppe) {
        this.hydrogenEnveloppe = hydrogenEnveloppe;
        setGroup();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Shape3D getDustTail() {
        return dustTail;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dustTail DOCUMENT ME!
     */
    public void setDustTail(Shape3D dustTail) {
        this.dustTail = dustTail;
        setGroup();
    }

    /**
     * DOCUMENT ME!
     */
    protected void updateShape() {
        super.updateShape();
    }
}
