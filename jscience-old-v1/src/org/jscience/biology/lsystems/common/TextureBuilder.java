// Author Matthias Hauswirth HTA - All rights reserved
package org.jscience.biology.lsystems.common;

import com.sun.j3d.utils.image.TextureLoader;

import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Texture;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.PixelGrabber;
import java.net.URL;

/**
 * This class serves as a factory for Texture and ImageComponent2D objects.
 * It creates these objects from image files (GIF or JPEG).
 * The files must be stored at the same location the as classfile for this class.
 * <p/>
 * Examples:
 * If the TextureBuilder.class is in /home/hrm/j3d/TextureBuilder.class
 * and one calls buildTexture("test.gif"), the there must be a file
 * /home/hrm/j3d/test.gif.<br>
 * <p/>
 * If the TextureBuilder.class is in /home/hrm/j3d/TextureBuilder.class
 * and one calls buildTexture("images/test.gif"), the there must be a file
 * /home/hrm/j3d/images/test.gif.<br>
 * <p/>
 * If the TextureBuilder.class is in C:\j3d\TextureBuilder.class
 * and one calls buildTexture("images/big/test.gif"), the there must be a file
 * C:\j3d\images\big\test.gif.<br>
 * <p/>
 * If the TextureBuilder.class is in http://www.isbiel.ch/~hrm/j3d/TextureBuilder.class
 * and one calls buildTexture("images/big/test.gif"), the there must be a file
 * http://www.isbiel.ch/~hrm/j3d/images/big/test.gif.<br>
 * <p/>
 * If the TextureBuilder.class is in a JAR file under j3d/TextureBuilder.class
 * and one calls buildTexture("images/big/test.gif"), the same JAR file must
 * contain the file j3d/images/big/test.gif.<br>
 * <p/>
 * <p/>
 * To create a Texture, do something like:
 * TextureBuilder tb = new TextureBuilder(); Texture t = tb.buildTexture("a.gif");<br>
 * <p/>
 * Note: This class loads images using a polling mechanism. Bad style! I should
 * use MT and synchronization.<br>
 *
 * @author Matthias Hauswirth HTA - All rights reserved
 */
public class TextureBuilder {
    /**
     * Creates a new TextureBuilder object.
     */
    public TextureBuilder() {
    }

    /**
     * Loads the image file and creates an ImageComponent2D.
     *
     * @throws RuntimeException if the image does not exist or is corrupted
     *                          Note: It would be better to throw an IOException, so users would be
     *                          forced to catch it. But I want to make this class as similiar to
     *                          the com.sun.j3d.utils.image.TextureLoader (which does not throw
     *                          checked Exceptions either) as possible.
     */
    public ImageComponent2D buildImage(String imageName) {
        //Log.debug("TextureBuilder.buildImage(" + imageName + ")");

        BufferedImage bufferedImage = createBufferedImage(imageName);
        TextureLoader textureLoader = new TextureLoader(bufferedImage);
        ImageComponent2D imageComponent2D = textureLoader.getImage();

        //Log.debug(" ImageComponent2D: " + imageComponent2D);

        return imageComponent2D;
    }

    /**
     * Loads the image file and creates an Texture.
     *
     * @throws RuntimeException if the image does not exist or is corrupted
     *                          Note: It would be better to throw an IOException, so users would be
     *                          forced to catch it. But I want to make this class as similiar to
     *                          the com.sun.j3d.utils.image.TextureLoader (which does not throw
     *                          checked Exceptions either) as possible.
     */
    public Texture buildTexture(String imageName) {
        //Log.debug("TextureBuilder.buildTexture(" + imageName + ")");

        BufferedImage bufferedImage = createBufferedImage(imageName);
        TextureLoader textureLoader = new TextureLoader(bufferedImage);
        Texture texture = textureLoader.getTexture();

        //Log.debug(" Texture: " + texture);

        return texture;
    }

    /**
     * Creates a BufferedImage from an image file.
     *
     * @throws RuntimeException if the image does not exist or is corrupted
     *                          Note: It would be better to throw an IOException, so users would be
     *                          forced to catch it. But I want to make this class as similiar to
     *                          the com.sun.j3d.utils.image.TextureLoader (which does not throw
     *                          checked Exceptions either) as possible.
     */
    private BufferedImage createBufferedImage(String imageName) {
        URL imageURL = getClass().getResource(imageName);

        if (imageURL == null) {
            throw new RuntimeException("Image resource '" + imageName +
                    "' not found.");
        }

        //Log.debug(" fetching image '" + imageURL + "'...");

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.createImage(imageURL);
        int status;

        // start loading image
        toolkit.prepareImage(image, -1, -1, null);

        // wait and poll periodically until complete
        while (true) {
            //Log.debug(" checking image loading progress...");
            status = toolkit.checkImage(image, -1, -1, null);

            if ((status & ImageObserver.ERROR) != 0) {
                throw new RuntimeException("Error loading image '" + imageURL +
                        "'");
            } else if ((status & ImageObserver.ALLBITS) != 0) {
                break;
            }

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new Error("Internal Error. Should never happen.");
            }
        }

        //Log.debug(" image loaded.");

        int width = image.getWidth(null);
        int height = image.getHeight(null);
        //Log.debug(" image dimensions: w=" + width + ", h=" + height);

        BufferedImage bufferedImage = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        int[] intPixels = ((DataBufferInt) bufferedImage.getRaster()
                .getDataBuffer()).getData();

        // retrieve image data using PixelGrabber
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height,
                intPixels, 0, width);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new Error("Internal Error. Should never happen.");
        }

        //Log.debug(" pixels grabbed from image (to bufferedImage).");

        return bufferedImage;
    }
}
