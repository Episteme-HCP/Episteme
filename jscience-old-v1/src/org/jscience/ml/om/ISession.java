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
 * /ISession.java
 *
 * (c) by Dirk Lehmann
 * ====================================================================
 */
package org.jscience.ml.om;

import org.w3c.dom.Element;


/**
 * An ISession can be used to link several observations together.
 * Typically a session would describe an observation night,
 * where several observations took place.
 * Therefore an ISession requires two mandatory fields:
 * a start date and an end date. All observations of the session
 * should have a start date that is inbetween the sessions
 * start and end date.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.0
 */
public interface ISession extends ISchemaElement {
    // ---------
    // Constants ---------------------------------------------------------
    // ---------
    /**
     * Constant for XML representation: Sessions element name.<br>
     * Example:<br>
     * &lt;session&gt;<i>More stuff goes here</i>&lt;/session&gt;
     */
    public static final String XML_ELEMENT_SESSION = "session";

    /**
     * Constant for XML representation: Sessions start date element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;begin&gt;<code>Sessions start date goes
     * here</code>&lt;/begin&gt; <i>More stuff goes here</i> &lt;/session&gt;
     */
    public static final String XML_ELEMENT_BEGIN = "begin";

    /**
     * Constant for XML representation: Sessions end date element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;end&gt;<code>Sessions end date goes
     * here</code>&lt;/end&gt; <i>More stuff goes here</i> &lt;/session&gt;
     */
    public static final String XML_ELEMENT_END = "end";

    /**
     * Constant for XML representation: Sessions site element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;site&gt;<code>Sessions site goes
     * here</code>&lt;/site&gt; <i>More stuff goes here</i> &lt;/session&gt;
     */
    public static final String XML_ELEMENT_SITE = "site";

    /**
     * Constant for XML representation: Sessions coObserver element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;coObserver&gt;<code>Sessions coObserver
     * goes here</code>&lt;/coObserver&gt; <i>More stuff goes here</i>
     * &lt;/session&gt;
     */
    public static final String XML_ELEMENT_COOBSERVER = "coObserver";

    /**
     * Constant for XML representation: Sessions weather element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;weather&gt;<code>Sessions weather goes
     * here</code>&lt;/weather&gt; <i>More stuff goes here</i>
     * &lt;/session&gt;
     */
    public static final String XML_ELEMENT_WEATHER = "weather";

    /**
     * Constant for XML representation: Sessions equipment element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;equipment&gt;<code>Sessions equipment
     * goes here</code>&lt;/equipment&gt; <i>More stuff goes here</i>
     * &lt;/session&gt;
     */
    public static final String XML_ELEMENT_EQUIPMENT = "equipment";

    /**
     * Constant for XML representation: Sessions comments element name.<br>
     * Example:<br>
     * &lt;session&gt; <br>
     * <i>More stuff goes here</i> &lt;comments&gt;<code>Sessions comments go
     * here</code>&lt;/comments&gt; <i>More stuff goes here</i>
     * &lt;/session&gt;
     */
    public static final String XML_ELEMENT_COMMENTS = "comments";

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------
    /**
     * Adds this Session to a given parent XML DOM Element. The Session
     * element will be set as a child element of the passed element.
     *
     * @param element The parent element for this Session
     *
     * @return Returns the element given as parameter with this Session as
     *         child element.<br>
     *         *  Might return <code>null</code> if parent was
     *         <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element element);

    // -------------------------------------------------------------------
    /**
     * Adds the session link to an given XML DOM Element The session
     * element itself will be attached to given elements ownerDocument. If the
     * ownerDocument has no session container, it will be created.<br>
     * Example:<br>
     * &lt;parameterElement&gt;<br>
     * <b>&lt;sessionLink&gt;123&lt;/sessionLink&gt;</b><br>
     * &lt;/parameterElement&gt;<br>
     * <i>More stuff of the xml document goes here</i><br>
     * <b>&lt;sessionContainer&gt;</b><br>
     * <b>&lt;session id="123"&gt;</b><br>
     * <i>session description goes
     * here</i><br><b>&lt;/session&gt;</b><br><b>&lt;/sessionContainer&gt;</b><br><br>
     *
     * @param parent The element under which the the Session link is created
     *
     * @return Returns the Element given as parameter with a additional Session
     *         link, and the session element under the session container of
     *         the ownerDocument Might return <code>null</code> if element was
     *         <code>null</code>.
     *
     * @see org.w3c.dom.Element
     */
    public org.w3c.dom.Element addAsLinkToXmlElement(org.w3c.dom.Element parent);

    // -------------------------------------------------------------------
    /**
     * Returns the start date of the session.<br>
     *
     * @return Returns the start date of the session
     */
    public java.util.Calendar getBegin();

    // -------------------------------------------------------------------
    /**
     * Returns a List of coobservers who joined this session.<br>
     * Might return <code>null</code> if no coobservers were added to this
     * session.
     *
     * @return Returns a List of coobserver or <code>null</code> if coobservers
     *         were never added.
     */
    public java.util.List getCoObservers();

    // -------------------------------------------------------------------
    /**
     * Returns a comment about this session.<br>
     * Might return <code>null</code> if no comment was set to this session.
     *
     * @return Returns a comment about this session or <code>null</code> if no
     *         comment was set at all.
     */
    public String getComments();

    // -------------------------------------------------------------------
    /**
     * Returns the end date of the session.<br>
     *
     * @return Returns the end date of the session
     */
    public java.util.Calendar getEnd();

    // -------------------------------------------------------------------
    /**
     * Returns the site of the session.<br>
     *
     * @return Returns the site of the session
     */
    public ISite getSite();

    // -------------------------------------------------------------------
    /**
     * Returns a string describing equipment which was used during this session.<br>
     * Typically one should add non optical equipment here like "Radio and a
     * warm bottle of Tea."<br>
     * Might return <code>null</code> if no equipment was set to this session.
     *
     * @return Returns string describing some equipment which was used during
     *         the session or <code>null</code> if no additional equipment was
     *         used at all.
     */
    public String getEquipment();

    // -------------------------------------------------------------------
    /**
     * Returns a describtion of the weather conditions during the session.<br>
     * Might return <code>null</code> if no weather conditions were addedt to
     * this session.
     *
     * @return Returns a describtion of the weather conditions during the
     *         session or <code>null</code> if no weather conditions were
     *         added at all.
     */
    public String getWeather();

    // -------------------------------------------------------------------
    /**
     * Sets the start date of the session.<br>
     *
     * @param begin The new start date of the session.
     *
     * @throws IllegalArgumentException if new start date is <code>null</code>
     */
    public void setBegin(java.util.Calendar begin)
        throws IllegalArgumentException;

    // -------------------------------------------------------------------
    /**
     * Sets the end date of the session.<br>
     *
     * @param end The new end date of the session.
     *
     * @throws IllegalArgumentException if new end date is <code>null</code>
     */
    public void setEnd(java.util.Calendar end) throws IllegalArgumentException;

    // -------------------------------------------------------------------
    /**
     * Sets a site (location) where the session took place.<br>
     * A session can only took place at one site.
     *
     * @param site The site where the session took place.
     *
     * @throws IllegalArgumentException if site is <code>null</code>
     */
    public void setSite(ISite site) throws IllegalArgumentException;

    // -------------------------------------------------------------------
    /**
     * Sets a comment to the session.<br>
     * The old comment will be overwritten.
     *
     * @param comments A new comment for the session
     */
    public void setComments(String comments);

    // -------------------------------------------------------------------
    /**
     * Sets a equipment description to the session.<br>
     * Typically non optical equipment will should be stored here, e.g. "Red
     * LED light and bottle of hot tea."<br>
     * The old equipment will be overwritten.
     *
     * @param equipment The new equipment of the session
     */
    public void setEquipment(String equipment);

    // -------------------------------------------------------------------
    /**
     * Sets a new List of coobservers to this session.<br>
     * The old List of coobservers will be overwritten. If you want to add one
     * ore more coobservers to the existing list use
     * addCoObservers(java.util.List) or addCoObserver(IObserver) instead.
     *
     * @param coObservers The new List of coobservers of the session
     *
     * @return <b>true</b> if the list could be set successfully, <b>false</b>
     *         if the operation fails, because e.g. one of the lists elements
     *         does not implement the IObserver interface. If <b>false</b> is
     *         returned the existing list is not changed at all.
     */
    public boolean setCoObservers(java.util.List coObservers);

    // -------------------------------------------------------------------
    /**
     * Adds a List of coobservers to this session.<br>
     * The old List of coobservers will be extended by the new List of
     * coobservers.
     *
     * @param coObservers A List of coobservers which will be added to the
     *        existing List of coobservers which is stored in the session
     *
     * @return <b>true</b> if the list could be added to the existing list,
     *         <b>false</b> if the operation fails, because e.g. one of the
     *         lists elements does not implement the IObserver interface. If
     *         <b>false</b> is returned the existing list is not changed at
     *         all.
     */
    public boolean addCoObservers(java.util.List coObservers);

    // -------------------------------------------------------------------
    /**
     * Adds a single coobserver to this session.<br>
     *
     * @param coObserver A new coobserver who will be addded to the List of
     *        coobservers
     *
     * @return <b>true</b> if the new coobserver could be added, <b>false</b>
     *         if the operation fails, because e.g. the given IObserver is
     *         <code>null</code>
     */
    public boolean addCoObserver(IObserver coObserver);

    // -------------------------------------------------------------------
    /**
     * Sets the weather conditions of the session.<br>
     * The weather conditions string should explain in some short sentences,
     * how the weather conditions were like during the session. E.g. "Small
     * clouds at the first hour but then totally clear and cool, at about
     * 4�C."
     *
     * @param weather A string describing the weather conditions during the
     *        session
     */
    public void setWeather(String weather);
}
