package org.episteme.core.media.backends;

import com.google.auto.service.AutoService;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.media.VideoBackend;
import org.episteme.core.media.VisionBackend;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import java.awt.image.BufferedImage;

/**
 * JavaCV (FFmpeg) Media Backend for multi-purpose usage (audio/video/vision).
 */
@AutoService({Backend.class, ComputeBackend.class, VideoBackend.class, AudioBackend.class, VisionBackend.class, CPUBackend.class})
public class JavaCVMediaBackend implements VideoBackend, AudioBackend, VisionBackend, CPUBackend {

    @Override
    public String getAlgorithmType() {
        return "Audio/Video/Vision Engine (JavaCV)";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <R> R execute(org.episteme.core.technical.backend.Operation<R> op) { return op.compute(this); }
            @Override public void close() {}
        };
    }

    private FFmpegFrameGrabber grabber;

    // ---- Backend Implementation ----

    @Override public String getType() { return "video"; }
    @Override public String getId() { return "javacv"; }
    @Override public String getName() { return "JavaCV (FFmpeg)"; }
    @Override public String getDescription() { return "JavaCV/FFmpeg backend for media processing (video/audio)."; }
    
    @Override 
    public boolean isAvailable() { 
        try {
            Class.forName("org.bytedeco.javacv.FFmpegFrameGrabber");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
    
    @Override public int getPriority() { return 30; }
    
    @Override 
    public Object createBackend() { 
        return new JavaCVMediaBackend(); 
    }

    // ---- Audio/Video Backend Implementation ----

    @Override
    public void load(String path) throws Exception {
        grabber = new FFmpegFrameGrabber(path);
        grabber.start();
    }
    
    @Override public void play() { throw new UnsupportedOperationException("Playback not supported by JavaCV backend"); }
    @Override public void pause() { throw new UnsupportedOperationException("Pause not supported by JavaCV backend"); }
    @Override public void stop() { try{ if(grabber!=null) grabber.stop(); }catch(Exception e){} }
    @Override public double getTime() { return (grabber!=null) ? grabber.getTimestamp() / 1_000_000.0 : 0; }
    @Override public double getDuration() { return (grabber!=null) ? grabber.getLengthInTime() / 1_000_000.0 : 0; }
    @Override public float[] getSpectrum() { return new float[128]; }
    
    @SuppressWarnings("unchecked")
    @Override public <T> T grabFrame() {
        try {
            if (grabber != null) {
                org.bytedeco.javacv.Java2DFrameConverter converter = new org.bytedeco.javacv.Java2DFrameConverter();
                return (T) converter.convert(grabber.grabImage());
            }
        } catch (Exception e) {}
        return null;
    }

    @Override
    public BufferedImage apply(BufferedImage image, org.episteme.core.media.vision.ImageOp<BufferedImage> op) {
        return op.process(image);
    }

    @Override
    public BufferedImage createImage(Object data, int width, int height) {
        if (data instanceof int[]) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            img.setRGB(0, 0, width, height, (int[]) data, 0, width);
            return img;
        }
        return null;
    }

    @Override public String getBackendName() { return "JavaCV (FFmpeg)"; }

    @Override
    public void shutdown() {
        stop();
    }
}
