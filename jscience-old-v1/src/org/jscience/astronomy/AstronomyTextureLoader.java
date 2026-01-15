package org.jscience.astronomy;


/*
*      @(#)NewTextureLoades.java 1.0 99/10/21
*
* Copyright (c) 1996-1999 Sun Microsystems, Inc. All Rights Reserved.
*
* Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
* modify and redistribute this software in source and binary code form,
* provided that i) this copyright notice and license appear on all copies of
* the software; and ii) Licensee does not utilize the software in a manner
* which is disparaging to Sun.
*
* This software is provided "AS IS," without a warranty of any kind. ALL
* EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
* IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
* NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
* LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
* OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
* LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
* INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
* CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
* OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGES.
*
* This software is not designed or intended for use in on-line control of
* aircraft, air traffic, aircraft navigation or aircraft communications; or in
* the design, construction, operation or maintenance of any nuclear
* facility. Licensee represents and warrants that it will not use or
* redistribute the Software for such purposes.
*/
import com.sun.j3d.utils.image.TextureLoader;


/**
 * A texture loading utility that doesn't require an image observer for
 * constructing objects.  This class extends the TextureLoader class of the
 * com.sun.j3d.utils.image package.
 */
public class AstronomyTextureLoader extends TextureLoader {
    /** DOCUMENT ME! */
    static java.awt.Component observer;

    // constructors without an image observer argument
/**
     * Constructs a AstronomyTextureLoader object loading the specified iamge in
     * default (RGBA) format. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param image the image object to load
     */
    public AstronomyTextureLoader(java.awt.Image image) {
        super(image, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified image and
     * option flags in the default (RGBA) format. The an image observer must
     * be set using the setImageObserver() method before using this
     * constructor.
     *
     * @param image the image object to load
     * @param flags the flags to use in construction (e.g. generate mipmap)
     */
    public AstronomyTextureLoader(java.awt.Image image, int flags) {
        super(image, flags, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified file using
     * the specified format. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param image  the image object to load
     * @param format specificaiton of which channels to use (e.g. RGB)
     */
    public AstronomyTextureLoader(java.awt.Image image, java.lang.String format) {
        super(image, format, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified file with
     * specified format and flags. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param image  the image object to load
     * @param format specificaiton of which channels to use (e.g. RGB)
     * @param flags  the flags to use in construction (e.g. generate mipmap)
     */
    public AstronomyTextureLoader(java.awt.Image image,
        java.lang.String format, int flags) {
        super(image, format, flags, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified file using
     * the default format (RGBA). The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param fname the name of the file to load
     */
    public AstronomyTextureLoader(java.lang.String fname) {
        super(fname, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified file with the
     * specified flags. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param fname the name of the file to load
     * @param flags the flags to use in construction (e.g. generate mipmap)
     */
    public AstronomyTextureLoader(java.lang.String fname, int flags) {
        super(fname, flags, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified file using
     * the specified format. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param fname  the name of the file to load
     * @param format specificaiton of which channels to use (e.g. RGB)
     */
    public AstronomyTextureLoader(java.lang.String fname,
        java.lang.String format) {
        super(fname, format, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified file using
     * the specified format and flags. The an image observer must be set using
     * the setImageObserver() method before using this constructor.
     *
     * @param fname  the name of the file to load
     * @param format specificaiton of which channels to use (e.g. RGB)
     * @param flags  the flags to use in construction (e.g. generate mipmap)
     */
    public AstronomyTextureLoader(java.lang.String fname,
        java.lang.String format, int flags) {
        super(fname, format, flags, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified URL using the
     * default format. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param url specifies the URL of the image to load
     */
    public AstronomyTextureLoader(java.net.URL url) {
        super(url, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified URL using the
     * specified flags. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param url   specifies the URL of the image to load
     * @param flags the flags to use in construction (e.g. generate mipmap)
     */
    public AstronomyTextureLoader(java.net.URL url, int flags) {
        super(url, flags, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified URL using the
     * specified format. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param url    specifies the URL of the image to load
     * @param format specificaiton of which channels to use (e.g. RGB)
     */
    public AstronomyTextureLoader(java.net.URL url, java.lang.String format) {
        super(url, format, observer);
    }

/**
     * Constructs a AstronomyTextureLoader object loading the specified URL using the
     * specified format and flags. The an image observer must be set using the
     * setImageObserver() method before using this constructor.
     *
     * @param url    specifies the URL of the image to load
     * @param format specificaiton of which channels to use (e.g. RGB)
     * @param flags  the flags to use in construction (e.g. generate mipmap)
     */
    public AstronomyTextureLoader(java.net.URL url, java.lang.String format,
        int flags) {
        super(url, format, flags, observer);
    }

    /**
     * Specify an object to server as the image observer. Use this
     * method once before constructing any texture loaders.
     *
     * @param imageObserver the object to be used in subsequent
     *        AstronomyTextureLoader constuctions
     */
    public static void setImageObserver(java.awt.Component imageObserver) {
        observer = imageObserver;
    }

    /**
     * Retreve the object used as the image observer for
     * AstronomyTextureLoader objects. Use this method when the image observer
     * is needed.
     *
     * @return the object used in as the image observer in subsequent
     *         AstronomyTextureLoader constuctions
     */
    public static java.awt.Component getImageObserver() {
        return observer;
    }
}
