/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.episteme.core.media.backends;

import com.google.auto.service.AutoService;
import org.episteme.core.media.AudioBackend;
import org.episteme.core.media.VideoBackend;
import org.episteme.core.media.VisionBackend;
import org.episteme.core.media.vision.ImageOp;
import org.episteme.core.technical.backend.Backend;
import org.episteme.core.technical.backend.ComputeBackend;
import org.episteme.core.technical.backend.cpu.CPUBackend;
import org.episteme.core.technical.backend.HardwareAccelerator;
import java.awt.image.BufferedImage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

/**
 * VLCJ (Native VLC) Media Backend for multi-purpose usage (audio/video/vision).
 */
@AutoService({Backend.class, ComputeBackend.class, VideoBackend.class, AudioBackend.class, VisionBackend.class, CPUBackend.class})
public class VLCJMediaBackend implements VideoBackend, AudioBackend, VisionBackend, CPUBackend {

    @Override
    public void shutdown() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (factory != null) {
            factory.release();
        }
    }

    @Override
    public org.episteme.core.technical.backend.ExecutionContext createContext() {
        return new org.episteme.core.technical.backend.ExecutionContext() {
            @Override public <R> R execute(org.episteme.core.technical.backend.Operation<R> op) { return op.compute(this); }
            @Override public void close() {}
        };
    }

    @Override
    public String getAlgorithmType() {
        return "Audio/Video/Vision Engine (VLCJ)";
    }

    @Override
    public HardwareAccelerator getAcceleratorType() {
        return HardwareAccelerator.CPU;
    }

    private MediaPlayerFactory factory;
    private MediaPlayer mediaPlayer;
    private static Boolean availableCache = null;

    static {
        try {
            // Attempt to discover VLC native libraries in the project's libs directory
            java.nio.file.Path current = java.nio.file.Paths.get(System.getProperty("user.dir"));
            java.nio.file.Path libsDir = null;
            while (current != null) {
                java.nio.file.Path libs = current.resolve("libs");
                if (java.nio.file.Files.exists(libs) && java.nio.file.Files.isDirectory(libs)) {
                    libsDir = libs.toAbsolutePath();
                    break;
                }
                current = current.getParent();
            }

            if (libsDir != null) {
                System.setProperty("jna.library.path", System.getProperty("jna.library.path", "") + java.io.File.pathSeparator + libsDir);
                
                java.nio.file.Path plugins = libsDir.resolve("plugins");
                if (java.nio.file.Files.exists(plugins)) {
                    System.setProperty("VLC_PLUGIN_PATH", plugins.toAbsolutePath().toString());
                    System.out.println("[INFO] VLCJBackend: Set VLC_PLUGIN_PATH to " + plugins);
                }
                
                System.out.println("[INFO] VLCJBackend: Discovered libs directory at " + libsDir);
                System.out.flush();
            }
            
            // vlcj 4.x discovery
            new uk.co.caprica.vlcj.factory.discovery.NativeDiscovery().discover();
        } catch (Throwable t) {
            // Discovery might fail if vlcj is not on classpath, but we check that in isAvailable
        }
    }

    public VLCJMediaBackend() {
    }

    private void initPlayer() {
        if (factory == null) {
            try {
                factory = new MediaPlayerFactory();
                mediaPlayer = factory.mediaPlayers().newMediaPlayer();
            } catch (Throwable t) {
                // Silently suppress during discovery
            }
        }
    }

    // ---- Backend Implementation ----

    @Override public String getType() { return "video"; }
    @Override public String getId() { return "vlcj"; }
    @Override public String getName() { return "VLCJ (Native VLC)"; }
    @Override public String getDescription() { return "Bindings to native VLC player (video/audio)."; }
    
    @Override 
    public boolean isAvailable() { 
        if (availableCache != null) return availableCache;
        try {
            Class.forName("uk.co.caprica.vlcj.factory.MediaPlayerFactory");
            // Try to initialize factory to check native libs
            new uk.co.caprica.vlcj.factory.MediaPlayerFactory().release();
            availableCache = true;
            return true;
        } catch (Throwable t) {
            availableCache = false;
            return false;
        }
    } 
    
    @Override public int getPriority() { return 40; }
    
    @Override 
    public Object createBackend() { 
        return new VLCJMediaBackend(); 
    }

    // ---- Audio/Video Backend Implementation ----

    @Override public void load(String path) throws Exception {
        initPlayer();
        if(mediaPlayer!=null) mediaPlayer.media().prepare(path);
    }
    
    @Override public void play() { if(mediaPlayer!=null) mediaPlayer.controls().play(); }
    @Override public void pause() { if(mediaPlayer!=null) mediaPlayer.controls().pause(); }
    @Override public void stop() { if(mediaPlayer!=null) mediaPlayer.controls().stop(); }
    @Override public double getTime() { return (mediaPlayer!=null) ? mediaPlayer.status().time() / 1000.0 : 0; }
    @Override public double getDuration() { return (mediaPlayer!=null) ? mediaPlayer.status().length() / 1000.0 : 0; }
    @Override public float[] getSpectrum() { return new float[128]; }
    
    @Override public <T> T grabFrame() {
        // VLCJ frame grabbing requires a CallbackVideoSurface, which is complex to implement here
        // For now, return null or implement if needed
        return null;
    }

    @Override
    public BufferedImage apply(BufferedImage image, ImageOp<BufferedImage> op) {
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

    @Override public String getBackendName() { return "VLCJ (Native VLC)"; }
}
