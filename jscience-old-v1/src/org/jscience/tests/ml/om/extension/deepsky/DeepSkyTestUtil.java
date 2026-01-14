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

/* ====================================================================
 * extension/deepSky/test/DeepSkyTestUtil
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.tests.ml.om.extension.deepsky;

import org.jscience.ml.om.*;
import org.jscience.ml.om.extension.deepsky.DeepSkyFinding;
import org.jscience.ml.om.extension.deepsky.DeepSkyTargetDS;
import org.jscience.ml.om.extension.deepsky.DeepSkyTargetGC;
import org.jscience.ml.om.extension.deepsky.DeepSkyTargetOC;

import java.util.GregorianCalendar;


/**
 * Simple Utility class for testing
 */
public class DeepSkyTestUtil {
    /**
     * DOCUMENT ME!
     */
    private ITarget target1 = null;

    /**
     * DOCUMENT ME!
     */
    private ITarget target2 = null;

    /**
     * DOCUMENT ME!
     */
    private ITarget target3 = null;

    /**
     * DOCUMENT ME!
     */
    private IObserver observer1 = null;

    /**
     * DOCUMENT ME!
     */
    private IObserver observer2 = null;

    /**
     * DOCUMENT ME!
     */
    private ISite site1 = null;

    /**
     * DOCUMENT ME!
     */
    private ISite site2 = null;

    /**
     * DOCUMENT ME!
     */
    private IScope scope1 = null;

    /**
     * DOCUMENT ME!
     */
    private IScope scope2 = null;

    /**
     * DOCUMENT ME!
     */
    private IEyepiece eyepiece1 = null;

    /**
     * DOCUMENT ME!
     */
    private IEyepiece eyepiece2 = null;

    /**
     * DOCUMENT ME!
     */
    private ISession session = null;

    /**
     * DOCUMENT ME!
     */
    private IFinding finding1 = null;

    /**
     * DOCUMENT ME!
     */
    private IFinding finding2 = null;

    /**
     * DOCUMENT ME!
     */
    private IFinding finding3 = null;

    /**
     * DOCUMENT ME!
     */
    private IImager imager = null;

    /**
     * Creates a new DeepSkyTestUtil object.
     */
    public DeepSkyTestUtil() {
        observer1 = createObserver();
        observer2 = createSecondObserver();
        target1 = createDeepSkyTarget();
        target2 = createSecondDeepSkyTarget(observer1);
        target3 = createThirdDeepSkyTarget();
        site1 = createSite();
        site2 = createSecondSite();
        scope1 = createScope();
        scope2 = createSecondScope();
        eyepiece1 = createEyepiece();
        eyepiece2 = createSecondEyepiece();
        session = createSession();
        finding1 = createFinding();
        finding2 = createSecondFinding();
        finding3 = createThirdFinding();
        imager = createFirstImager();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IObservation createDeepSkyObservation() {
        IObservation observation = new Observation(new GregorianCalendar(2004,
                    01, 01, 22, 00),
                new GregorianCalendar(2004, 01, 01, 22, 10), 5.0f, 4, 70.3f,
                target1, observer1, site1, scope1, "Narrow Band Filter",
                eyepiece1, imager, session, finding1);

        return observation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IObservation createDeepSkyObservation2() {
        IObservation observation = new Observation(new GregorianCalendar(2004,
                    01, 01, 22, 15),
                new GregorianCalendar(2004, 01, 01, 22, 30), 5.0f, 2, 150.0f,
                target2, observer1, site2, scope1, "OIII Filter", eyepiece2,
                imager, session, finding3);

        observation.addResult(finding2);

        return observation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IObservation createDeepSkyObservation3() {
        IObservation observation = new Observation(new GregorianCalendar(2004,
                    01, 02, 22, 00),
                new GregorianCalendar(2004, 01, 02, 22, 30), target3,
                observer2, finding3);

        observation.setScope(scope2);

        return observation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IFinding createFinding() {
        DeepSkyFinding finding = new DeepSkyFinding("Looks twisted", 3);

        finding.setMottled(new Boolean(false));
        finding.setResolved(new Boolean(true));
        finding.setStellar(new Boolean(false));
        finding.setLargeDiameter(new Angle(10, Angle.ARCSECOND));

        return finding;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IFinding createThirdFinding() {
        DeepSkyFinding finding = new DeepSkyFinding("Wow!", 4);

        finding.setResolved(new Boolean(true));
        finding.setStellar(new Boolean(false));
        finding.setLargeDiameter(new Angle(10, Angle.ARCSECOND));

        return finding;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IFinding createSecondFinding() {
        DeepSkyFinding finding = new DeepSkyFinding("Hey there're two!", 3);

        finding.setMottled(new Boolean(false));
        finding.setResolved(new Boolean(false));
        finding.setSmallDiameter(new Angle(7, Angle.ARCSECOND));

        return finding;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ISession createSession() {
        ISession session = new Session(new GregorianCalendar(2004, 01, 01, 22,
                    00), new GregorianCalendar(2004, 01, 01, 23, 30), this.site1);

        session.addCoObserver(observer1);
        session.setComments("That was fun!");
        session.setEquipment("Red light, chair and Beethoven No.9");
        session.setWeather("Clear, not a single cloud");

        return session;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IEyepiece createEyepiece() {
        IEyepiece eyepiece = new Eyepiece("Nagler", 31);

        eyepiece.setVendor("TeleVue");

        return eyepiece;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IEyepiece createSecondEyepiece() {
        IEyepiece eyepiece = new Eyepiece("Speers Waler", 10);

        eyepiece.setApparentFOV(new Angle(80, Angle.ARCSECOND));

        return eyepiece;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IScope createScope() {
        IScope scope = new Scope("Meade Starfinder 10\"", 254, 1140);

        return scope;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IScope createSecondScope() {
        IScope scope = new Scope(50f, 10f, "Nikon 10x50 CF");

        return scope;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IImager createFirstImager() {
        return new CCDImager("ToUCam", 800, 640);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ISite createSite() {
        ISite site = new Site("Wehrheim", new Angle(8.567, Angle.DEGREE),
                new Angle(50.3, Angle.DEGREE), 2);

        return site;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ISite createSecondSite() {
        ISite site = new Site("Dossenheim", new Angle(8.657, Angle.DEGREE),
                new Angle(49.45, Angle.DEGREE), 2);

        site.setElevation(300.5f);

        return site;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IObserver createObserver() {
        IObserver observer = new Observer("John", "Doe");

        return observer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IObserver createSecondObserver() {
        IObserver observer = new Observer("Hans", "Mustermann");

        observer.addContact("Musterstrasse 1");
        observer.addContact("12345 Musterstadt");
        observer.addContact("mustermann@britneyspearsmail.com");

        return observer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ITarget createDeepSkyTarget() {
        DeepSkyTargetDS target = new DeepSkyTargetDS("A double star", "WU DC");

        target.setConstellation("Ursa Major");
        target.setSurfaceBrightness(8.7f);
        target.setVisibleMagnitude(10.3f);
        target.setSeparation(new Angle(51.5, Angle.DEGREE));
        target.setPositionAngle(134);

        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @param observer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ITarget createSecondDeepSkyTarget(IObserver observer) {
        DeepSkyTargetOC target = new DeepSkyTargetOC("M45", observer);

        target.setPosition(new EquPosition(new Angle(18.536f, Angle.RADIANT),
                new Angle(33.02f, Angle.DEGREE)));

        target.setConstellation("Canis Vestaci");
        target.setBrightestStar(45.2);
        target.setAmountOfStars(120);
        target.setClusterClassification("Nide");

        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ITarget createThirdDeepSkyTarget() {
        DeepSkyTargetGC target = new DeepSkyTargetGC("M13", "Messier Catalogue");

        target.setConcentration("Dense in center");
        target.setMagnitude(45.4);

        return target;
    }
}
