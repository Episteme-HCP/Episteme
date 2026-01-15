/*
  File: CapturingCanvas3D.java

  University of Applied Science Berne,HTA-Biel/Bienne,
  Computer Science Department.

  Diploma thesis J3D Solar System Simulator
  Originally written by Marcel Portner & Bernhard Hari (c) 2000
  
  CVS - Information :
  
  $Header: /zpool01/javanet/scm/svn/tmp/cvs2svn/jade/v1/src/org/jscience/astronomy/CapturingCanvas3D.java,v 1.1 2006-09-07 21:14:22 virtualcall Exp $
  $Author: virtualcall $
  $Date: 2006-09-07 21:14:22 $
  $State: Exp $
  
*/
package org.jscience.astronomy;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class CapturingCanvas3D, using the instructions from the Java3D
 * FAQ pages on how to capture a still image in jpeg format.
 * <p/>
 * A capture button would call a method that looks like
 * <p/>
 * <pre>
 *  public static void captureImage(CapturingCanvas3D MyCanvas3D) {
 *    MyCanvas3D.writeJPEG_ = true;
 *    MyCanvas3D.repaint();
 *  }
 * </pre>
 * <p/>
 * Peter Z. Kunszt
 * Johns Hopkins University
 * Dept of Physics and Astronomy
 * Baltimore MD
 *
 * @author Marcel Portner & Bernhard Hari
 * @version $Revision: 1.1 $
 */
public class CapturingCanvas3D extends Canvas3D {

    public boolean writeJPEG_;
    private int postSwapCount_;

    /**
     * Constructor that generate a Canvas3D.
     *
     * @param gc the GraphicsConfiguration
     */
    public CapturingCanvas3D(GraphicsConfiguration gc) {
        super(gc);
        postSwapCount_ = 0;
        writeJPEG_ = false;
    }

    /**
     * Override Canvas3D's postSwap method to save a JPEG of the canvas.
     */
    public void postSwap() {
        if (writeJPEG_) {
            System.out.println("Writing JPEG");
            int dimX = this.getScreen3D().getSize().width;
            int dimY = this.getScreen3D().getSize().height;

            // The raster components need all be set!
            Raster ras = new Raster(new Point3f(-1.0f, -1.0f, -1.0f),
                    Raster.RASTER_COLOR,
                    0, 0,
                    dimX, dimY,
                    new ImageComponent2D(ImageComponent.FORMAT_RGB,
                            new BufferedImage(dimX, dimY,
                                    BufferedImage.TYPE_INT_RGB)),
                    null);

            GraphicsContext3D ctx = getGraphicsContext3D();
            ctx.readRaster(ras);

            // Now strip out the image info
            BufferedImage img = ras.getImage().getImage();

            // write that to disk....
            try {
                FileOutputStream out = new FileOutputStream("Capture" + postSwapCount_ + ".jpg");
                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
                param.setQuality(0.9f, false); // 90% qualith JPEG
                encoder.setJPEGEncodeParam(param);
                encoder.encode(img);
                writeJPEG_ = false;
                out.close();
            } catch (IOException e) {
                System.out.println("I/O exception!");
            }
            postSwapCount_++;
        }
    }
}
