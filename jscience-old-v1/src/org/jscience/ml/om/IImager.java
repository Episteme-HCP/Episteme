/* ====================================================================
 * /IImager.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;

import org.w3c.dom.Element;

/**
 * An IImager describes a camera.
 * The model name is a
 * mandatory field which has to be set.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.3
 */
public interface IImager extends ISchemaElement {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    /**
     * Constant for XML representation: imager element name
     */
    public static final String XML_ELEMENT_IMAGER = "imager";

    /**
     * Constant for XML representation: model element name
     * <p/>
     * Example:<br>
     * &lt;imager&gt;
     * <br><i>More stuff goes here</i>
     * &lt;model&gt;<code>Model name goes here</code>&lt;/model&gt;
     * <i>More stuff goes here</i>
     * &lt;/imager&gt;
     */
    public static final String XML_ELEMENT_MODEL = "model";

    /**
     * Constant for XML representation: type element name
     * <p/>
     * Example:<br>
     * &lt;imager&gt;
     * <br><i>More stuff goes here</i>
     * &lt;type&gt;<code>Type goes here</code>&lt;/type&gt;
     * <i>More stuff goes here</i>
     * &lt;/imager&gt;
     */
    public static final String XML_ELEMENT_TYPE = "type";

    /**
     * Constant for XML representation: vendor element name
     * <p/>
     * Example:<br>
     * &lt;imager&gt;
     * <br><i>More stuff goes here</i>
     * &lt;vendor&gt;<code>Vendor name goes here</code>&lt;/vendor&gt;
     * <i>More stuff goes here</i>
     * &lt;/imager&gt;
     */
    public static final String XML_ELEMENT_VENDOR = "vendor";

    /**
     * Constant for XML representation: remarks element name
     * <p/>
     * Example:<br>
     * &lt;imager&gt;
     * <br><i>More stuff goes here</i>
     * &lt;remarks&gt;<code>Vendor name goes here</code>&lt;/remarks&gt;
     * <i>More stuff goes here</i>
     * &lt;/imager&gt;
     */
    public static final String XML_ELEMENT_REMARKS = "remarks";


    /**
     * CCD type constant for imager
     */
    public static final String CCD = "ccd";

    /**
     * Film/chemical type constant for imager
     */
    public static final String FILM = "film";

    // --------------
    // Public methods ----------------------------------------------------
    // --------------

    // -------------------------------------------------------------------

    /**
     * Adds this IImager to a given parent XML DOM Element.
     * The IImager element will be set as a child element of
     * the passed element.
     *
     * @param parent The parent element for this IImager
     * @return Returns the element given as parameter with this
     *         IImager as child element.<br>
     *         Might return <code>null</code> if parent was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public Element addToXmlElement(Element element);


    // -------------------------------------------------------------------
    /**
     * Adds the eyepiece link to an given XML DOM Element
     * The IImager element itself will be attached to given elements
     * ownerDocument. If the ownerDocument has no IImager container, it will
     * be created.<br>
     * Example:<br>
     * &lt;parameterElement&gt;<br>
     * <b>&lt;imagerLink&gt;123&lt;/imagerLink&gt;</b><br>
     * &lt;/parameterElement&gt;<br>
     * <i>More stuff of the xml document goes here</i><br>
     * <b>&lt;imagerContainer&gt;</b><br>
     * <b>&lt;imager id="123"&gt;</b><br>
     * <i>imager description goes here</i><br>
     * <b>&lt;/imager&gt;</b><br>
     * <b>&lt;/imagerContainer&gt;</b><br>
     * <br>
     *
     * @param parent The element under which the the imager link is created
     * @return Returns the Element given as parameter with a additional
     *         imager link, and the imager element under the imager container
     *         of the ownerDocument
     *         Might return <code>null</code> if element was <code>null</code>.
     * @see org.w3c.dom.Element
     */
    public org.w3c.dom.Element addAsLinkToXmlElement(org.w3c.dom.Element parent);


    // -------------------------------------------------------------------
    /**
     * Returns the model name of this imager.<br>
     *
     * @return Returns the model name of this imager.<br>
     */
    public String getModel();


    // -------------------------------------------------------------------
    /**
     * Returns the vendor name of this imager.<br>
     *
     * @return Returns the vendor name of this imager or <code>NULL</code>
     *         if vendor name was not set.<br>
     */
    public String getVendor();


    // -------------------------------------------------------------------
    /**
     * Returns the type of this imager.<br>
     * Only two types are allowed:<br>
     * - IImager.CCD<br>
     * - IImager.Film<br>
     *
     * @return Returns the type imager or <code>NULL</code>
     *         if type was not set.<br>
     */
    public String getType();


    // -------------------------------------------------------------------
    /**
     * Returns the remarks to this imager.<br>
     *
     * @return Returns the remarks to this imager or <code>NULL</code>
     *         if no remarks were set.<br>
     */
    public String getRemarks();


    // -------------------------------------------------------------------
    /**
     * Sets the model name for the imager.<br>
     *
     * @param modelname The new model name to be set.
     * @throws IllegalArgumentException if new modelname is <code>null</code> or empty string
     */
    public void setModel(String modelname) throws IllegalArgumentException;


    // -------------------------------------------------------------------
    /**
     * Sets the vendor name for the imager.<br>
     *
     * @param vendor The new vendor name to be set.
     */
    public void setVendor(String vendor);


    // -------------------------------------------------------------------
    /**
     * Sets the type for this imager.<br>
     * Only two types are allowed:<br>
     * - IImager.CCD<br>
     * - IImager.Film<br>
     * If some other type is given, no change will take place.<br>
     *
     * @param type The new image type.
     */
    public void setType(String type);


    // -------------------------------------------------------------------
    /**
     * Sets the remarks for this imager.<br>
     *
     * @param remarks The new remarks.
     */
    public void setRemarks(String remarks);

}
