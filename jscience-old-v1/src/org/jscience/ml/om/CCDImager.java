/* ====================================================================
 * /CCDImager.java
 * 
 * (c) by Dirk Lehmann
 * ====================================================================
 */


package org.jscience.ml.om;

import org.jscience.ml.om.util.SchemaException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Describes a CCD camera.
 *
 * @author doergn@users.sourceforge.net
 * @since 1.3
 */
public class CCDImager extends Imager {

    // ---------
    // Constants ---------------------------------------------------------
    // ---------

    /**
     * Constant for XML representation: pixels on x axis
     */
    public static final String XML_ELEMENT_XPIXELS = "pixelsX";

    /**
     * Constant for XML representation: pixels on y axis
     */
    public static final String XML_ELEMENT_YPIXELS = "pixelsY";

    // ------------------
    // Instance Variables ------------------------------------------------
    // ------------------

    // Name of the astronomical object
    private int xPixels = 0;

    // Alternative names of the astronomical object
    private int yPixels = 0;

    // ------------
    // Constructors ------------------------------------------------------
    // ------------

    // ------------------------------------------------------------------- 

    /**
     * Constructs a new instance of a CCDImager from a given DOM
     * target Element.<br>
     * Normally this constructor is called by a child class which itself
     * was called by org.jscience.ml.om.util.SchemaLoader.
     *
     * @param imagerElement The origin XML DOM <target> Element
     * @throws SchemaException if given imagerElement was <code>null</code>
     */
    public CCDImager(Node imagerElement) throws SchemaException {

        super(imagerElement);

        Element imager = (Element) imagerElement;

        Element child = null;
        NodeList children = null;

        // Getting data                     

        // Get xPixels
        children = imager.getElementsByTagName(CCDImager.XML_ELEMENT_XPIXELS);
        if ((children == null)
                || (children.getLength() != 1)
                ) {
            throw new SchemaException("CCDImager must have exact one x pixels element. ");
        }
        child = (Element) children.item(0);
        String x = null;
        if (child == null) {
            throw new SchemaException("CCDImager must have a x pixel element. ");
        } else {
            if (child.getFirstChild() != null) {
                x = child.getFirstChild().getNodeValue();  // Get xPixels from text node                
            } else {
                throw new SchemaException("CCDImager cannot have an empty xPixels element. ");
            }

            this.setXPixels(Integer.parseInt(x));
        }

        // Get yPixels
        children = imager.getElementsByTagName(CCDImager.XML_ELEMENT_YPIXELS);
        if ((children == null)
                || (children.getLength() != 1)
                ) {
            throw new SchemaException("CCDImager must have exact one y pixels element. ");
        }
        child = (Element) children.item(0);
        String y = null;
        if (child == null) {
            throw new SchemaException("CCDImager must have a y pixel element. ");
        } else {
            if (child.getFirstChild() != null) {
                y = child.getFirstChild().getNodeValue();  // Get yPixels from text node                
            } else {
                throw new SchemaException("CCDImager cannot have an empty yPixels element. ");
            }

            this.setYPixels(Integer.parseInt(y));
        }

    }


    // ------------------------------------------------------------------- 
    /**
     * Constructs a new instance of a CCDImager.<br>
     *
     * @param model   The model name
     * @param xPixels The amount of pixel on the x axis
     * @param yPixels The amount of pixel on the y axis
     * @throws SchemaException if given model was <code>null</code>, or
     *                         on of the pixel values was <= 0.
     */
    public CCDImager(String model,
                     int xPixels,
                     int yPixels) {

        super(model);

        super.setType(IImager.CCD);
        this.setXPixels(xPixels);
        this.setYPixels(yPixels);

    }

    // ------
    // Imager ------------------------------------------------------------
    // ------    

    public Element addToXmlElement(Element element) {

        if (element == null) {
            return null;
        }

        Document ownerDoc = element.getOwnerDocument();

        Element e_Imager = super.createXmlImagerElement(element);

        Element e_XPixels = ownerDoc.createElement(CCDImager.XML_ELEMENT_XPIXELS);
        Node n_xPixelsText = ownerDoc.createTextNode("" + this.xPixels);
        e_XPixels.appendChild(n_xPixelsText);
        e_Imager.appendChild(e_XPixels);

        Element e_YPixels = ownerDoc.createElement(CCDImager.XML_ELEMENT_YPIXELS);
        Node n_yPixelsText = ownerDoc.createTextNode("" + this.yPixels);
        e_YPixels.appendChild(n_yPixelsText);
        e_Imager.appendChild(e_YPixels);

        return element;

    }

    // --------------
    // Public Methods ----------------------------------------------------
    // --------------	

    // -------------------------------------------------------------------

    /**
     * Returns the amount of pixels on the x axis.<br>
     *
     * @return Returns amount of pixels on the x axis<br>
     */
    public int getXPixels() {

        return xPixels;

    }


    // -------------------------------------------------------------------
    /**
     * Sets the amount of pixels on the x axis.<br>
     *
     * @param pixels The new amount of pixel on the x axis
     * @throws IllegalArgumentException if given pixels are <= 0
     */
    public void setXPixels(int pixels) throws IllegalArgumentException {

        if (pixels <= 0) {
            throw new IllegalArgumentException("Amount of pixels on x axis must be greater than 0\n");
        }

        xPixels = pixels;

    }


    // -------------------------------------------------------------------
    /**
     * Returns the amount of pixels on the y axis.<br>
     *
     * @return Returns amount of pixels on the y axis<br>
     */
    public int getYPixels() {

        return yPixels;

    }


    // -------------------------------------------------------------------
    /**
     * Sets the amount of pixels on the y axis.<br>
     *
     * @param pixels The new amount of pixel on the y axis
     * @throws IllegalArgumentException if given pixels are <= 0
     */
    public void setYPixels(int pixels) {

        if (pixels <= 0) {
            throw new IllegalArgumentException("Amount of pixels on y axis must be greater than 0\n");
        }

        yPixels = pixels;

    }

}
