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
 * /ISite.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

import org.w3c.dom.Element;


/**
 * An ISite describes an observation site where an observation took place.<br>
 * A site can be identified by its latitude and longitude values, but
 * as for processing reasons its implementation should have
 * more mandatory fields, such as elevation and timezone.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public interface ISite extends ISchemaElement {
    // ---------
    // Constants ---------------------------------------------------------
    // ---------
    /**
     * Constant for XML representation: Site element name.<br>
     * Example:<br>
     * &lt;site&gt;<i>More stuff goes here</i>&lt;/site&gt;
     */
    public static final String XML_ELEMENT_SITE = "site";

    /**
     * Constant for XML representation: Site name element name.<br>
     * Example:<br>
     * &lt;site&gt; <br>
     * <i>More stuff goes here</i> &lt;name&gt;<code>Site name goes
     * here</code>&lt;/name&gt; <i>More stuff goes here</i> &lt;/site&gt;
     */
    public static final String XML_ELEMENT_NAME = "name";

    /**
     * Constant for XML representation: Site longitude element name.<br>
     * Example:<br>
     * &lt;site&gt; <br>
     * <i>More stuff goes here</i> &lt;longitude&gt;<code>Site longitude goes
     * here</code>&lt;/longitude&gt; <i>More stuff goes here</i> &lt;/site&gt;
     */
    public static final String XML_ELEMENT_LONGITUDE = "longitude";

    /**
     * Constant for XML representation: Site latitude element name.<br>
     * Example:<br>
     * &lt;site&gt; <br>
     * <i>More stuff goes here</i> &lt;latitude&gt;<code>Site latitude goes
     * here</code>&lt;/latitude&gt; <i>More stuff goes here</i> &lt;/site&gt;
     */
    public static final String XML_ELEMENT_LATITUDE = "latitude";

    /**
     * Constant for XML representation: Site elevation element name.<br>
     * Example:<br>
     * &lt;site&gt; <br>
     * <i>More stuff goes here</i> &lt;elevation&gt;<code>Site elevation goes
     * here</code>&lt;/elevation&gt; <i>More stuff goes here</i> &lt;/site&gt;
     */
    public static final String XML_ELEMENT_ELEVATION = "elevation";

    /**
     * Constant for XML representation: Site timezone element name.<br>
     * Example:<br>
     * &lt;site&gt; <br>
     * <i>More stuff goes here</i> &lt;timezone&gt;<code>Sites timezone goes
     * here</code>&lt;/timezone&gt; <i>More stuff goes here</i> &lt;/site&gt;
     */
    public static final String XML_ELEMENT_TIMEZONE = "timezone";

    /**
     * Constant for XML representation: Site IAU code element name.<br>
     * Example:<br>
     * &lt;site&gt; <br>
     * <i>More stuff goes here</i> &lt;code&gt;<code>Sites IAU code goes
     * here</code>&lt;/code&gt; <i>More stuff goes here</i> &lt;/site&gt;
     */
    public static final String XML_ELEMENT_IAUCODE = "code";

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------
    /**
     * Adds this Site to a given parent XML DOM Element. The Site
     * element will be set as a child element of the passed element.
     *
     * @param element The parent element for this Site
     *
     * @return Returns the element given as parameter with this Site as child element.<br>
     *         *  Might return <code>null</code> if parent was
     *         <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element element);

    // -------------------------------------------------------------------
    /**
     * Adds the site link to an given XML DOM Element The site element
     * itself will be attached to given elements ownerDocument. If the
     * ownerDocument has no site container, it will be created.<br>
     * Example:<br>
     * &lt;parameterElement&gt;<br>
     * <b>&lt;siteLink&gt;123&lt;/siteLink&gt;</b><br>
     * &lt;/parameterElement&gt;<br>
     * <i>More stuff of the xml document goes here</i><br>
     * <b>&lt;siteContainer&gt;</b><br>
     * <b>&lt;site id="123"&gt;</b><br>
     * <i>site description goes
     * here</i><br><b>&lt;/site&gt;</b><br><b>&lt;/siteContainer&gt;</b><br><br>
     *
     * @param parent The element under which the the site link is created
     *
     * @return Returns the Element given as parameter with a additional site
     *         link, and the site element under the site container of the
     *         ownerDocument Might return <code>null</code> if element was
     *         <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    public org.w3c.dom.Element addAsLinkToXmlElement(org.w3c.dom.Element parent);

    // -------------------------------------------------------------------
    /**
     * Returns the latitude of the site.<br>
     * The latitude is a positiv angle if its north of the equator, and
     * negative if south of the equator.
     *
     * @return Returns an Angle with the geographical latitude
     */
    public org.jscience.ml.om.Angle getLatitude();

    // -------------------------------------------------------------------
    /**
     * Returns the longitude of the site.<br>
     * The longitude is a positiv angle if its east of Greenwich, and negative
     * if west of Greenwich.
     *
     * @return Returns an Angle with the geographical longitude
     */
    public org.jscience.ml.om.Angle getLongitude();

    // -------------------------------------------------------------------
    /**
     * Returns the name of the site.<br>
     * The name may be any string describing the site as precise as it can be.
     *
     * @return Returns the name of the site
     */
    public String getName();

    // -------------------------------------------------------------------
    /**
     * Returns the IAU station code of the site.<br>
     * This method may return <code>null</code> as the site may not have an
     * IAU (International Astronomical Union) station code.
     *
     * @return Returns the IAU code of the site, or <code>null</code> if no
     *         code exists, or was never set.
     */
    public String getIAUCode();

    // -------------------------------------------------------------------
    /**
     * Returns the timezone of the site.<br>
     * The timezone is given as positiv or negative value, depending on the
     * sites timezone difference to the GMT in minutes.
     *
     * @return Returns timzone offset (in comparism to GMT) in minutes
     */
    public int getTimezone();

    // -------------------------------------------------------------------
    /**
     * Returns the elevation of the site.<br>
     * The elevation is given in meters above/under sea level.
     *
     * @return Returns the sites elevation in meters above or under sea level,
     *         or <code>NULL</code> if value was never set
     */
    public float getElevation();

    // -------------------------------------------------------------------
    /**
     * Sets the elevation of the site.<br>
     * The elevation should be given in meters above/under sea level.
     *
     * @param elevation The new elevation for this site
     */
    public void setElevation(float elevation);

    // -------------------------------------------------------------------
    /**
     * Sets the latitude of the site.<br>
     * The latitude must be a positiv angle if its north of the equator, and
     * negative if south of the equator.
     *
     * @param latitude The new latitude for this site
     *
     * @throws IllegalArgumentException if latitude is <code>null</code>
     */
    public void setLatitude(org.jscience.ml.om.Angle latitude)
        throws IllegalArgumentException;

    // -------------------------------------------------------------------
    /**
     * Sets the longitude of the site.<br>
     * The longitude must be a positiv angle if its east of Greenwich, and
     * negative if west of Greenwich.
     *
     * @param longitude The new longitude for this site
     *
     * @throws IllegalArgumentException if longitude is <code>null</code>
     */
    public void setLongitude(org.jscience.ml.om.Angle longitude)
        throws IllegalArgumentException;

    // -------------------------------------------------------------------
    /**
     * Sets the name of the site.<br>
     * The name should be any string describing the site as precise as it can
     * be.
     *
     * @param name The new name for this site
     *
     * @throws IllegalArgumentException if name is <code>null</code>
     */
    public void setName(String name) throws IllegalArgumentException;

    // -------------------------------------------------------------------
    /**
     * Sets the IAU code of the site.<br>
     *
     * @param IAUCode The new IAU code for this site
     */
    public void setIAUCode(String IAUCode);

    // -------------------------------------------------------------------
    /**
     * Sets the timezone of the site.<br>
     * The timezone must be given as positiv or negative value, depending on
     * the sites timezone difference to the GMT in minutes.
     *
     * @param timezone The new timezone for this site in minutes
     *
     * @throws IllegalArgumentException if new timezone is greater than 720
     *         (1260) or lower than -720 (1260)
     */
    public void setTimezone(int timezone) throws IllegalArgumentException;
}
