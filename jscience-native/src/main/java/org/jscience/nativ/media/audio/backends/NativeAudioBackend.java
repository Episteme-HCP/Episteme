/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
 */
package org.jscience.nativ.media.audio.backends;

import com.google.auto.service.AutoService;
import org.jscience.core.media.AudioBackend;
import org.jscience.core.technical.backend.Backend;
import org.jscience.nativ.technical.backend.nativ.NativeLibraryLoader;

import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.util.Optional;

/**
 * Native Audio Backend using miniaudio (via Panama FFM) for low-latency playback.
 * This backend provides direct access to audio hardware, bypassing JavaSound limitations.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.2
 */
@SuppressWarnings({"preview", "unused"})
@AutoService(Backend.class)
public class NativeAudioBackend implements AudioBackend { // Removed redundant Backend interface

    private static final SymbolLookup LOOKUP;
    private static final boolean IS_AVAILABLE_FLAG; // Renamed to avoid confusion/conflict if any

    // FFM Handles
    private static MethodHandle MA_DEVICE_INIT;
    private static MethodHandle MA_DEVICE_START;
    private static MethodHandle MA_DEVICE_STOP;
    private static MethodHandle MA_DECODER_INIT_FILE;
    private static MethodHandle MA_DECODER_GET_LENGTH;

    static {
        Optional<SymbolLookup> lib = NativeLibraryLoader.loadLibrary("miniaudio", Arena.global());
        if (lib.isEmpty()) {
            // Also try 'portaudio' as alternative if miniaudio is not bundled
            lib = NativeLibraryLoader.loadLibrary("portaudio", Arena.global());
        }

        boolean avail = false;
        if (lib.isPresent()) {
            LOOKUP = lib.get();
            try {
                Linker linker = Linker.nativeLinker();
                
                // Load miniaudio functions (assuming miniaudio symbols are exported)
                // ma_result ma_device_init(ma_context* pContext, const ma_device_config* pConfig, ma_device* pDevice);
                // simplified signature for now
                try {
                     MA_DEVICE_INIT = linker.downcallHandle(
                            LOOKUP.find("ma_device_init").orElseThrow(),
                            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
                    );
                    
                    MA_DEVICE_START = linker.downcallHandle(
                            LOOKUP.find("ma_device_start").orElseThrow(),
                            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
                    );

                    MA_DEVICE_STOP = linker.downcallHandle(
                            LOOKUP.find("ma_device_stop").orElseThrow(),
                            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS)
                    );

                    // ma_result ma_decoder_init_file(const char* pFilePath, const ma_decoder_config* pConfig, ma_decoder* pDecoder);
                     MA_DECODER_INIT_FILE = linker.downcallHandle(
                            LOOKUP.find("ma_decoder_init_file").orElseThrow(),
                            FunctionDescriptor.of(ValueLayout.JAVA_INT, ValueLayout.ADDRESS, ValueLayout.ADDRESS, ValueLayout.ADDRESS)
                    );
                    
                     // ma_uint64 ma_decoder_get_length_in_pcm_frames(ma_decoder* pDecoder);
                     MA_DECODER_GET_LENGTH = linker.downcallHandle(
                            LOOKUP.find("ma_decoder_get_length_in_pcm_frames").orElseThrow(),
                            FunctionDescriptor.of(ValueLayout.JAVA_LONG, ValueLayout.ADDRESS)
                    );

                    avail = true;
                } catch (RuntimeException e) {
                     // Symbols missing, partial loading
                     System.err.println("NativeAudioBackend: Symbols missing in native library. " + e.getMessage());
                     avail = false;
                }

            } catch (Throwable t) {
                // If symbols missing, mark unavailable
                avail = false;
            }
        } else {
            LOOKUP = null;
            avail = false;
        }
        IS_AVAILABLE_FLAG = avail;
    }

    // miniaudio StructLayouts (Simplified for core usage)
    private static final StructLayout MA_DEVICE_CONFIG_LAYOUT = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("deviceType"),
            ValueLayout.JAVA_INT.withName("sampleRate"),
            ValueLayout.JAVA_INT.withName("channels"),
            ValueLayout.JAVA_INT.withName("format"),
            ValueLayout.JAVA_INT.withName("shareMode"),
            MemoryLayout.paddingLayout(4), // alignment
            AddressLayout.ADDRESS.withName("pPlayback"),
            AddressLayout.ADDRESS.withName("pCapture"),
            AddressLayout.ADDRESS.withName("pUserData")
    ).withName("ma_device_config");

    private static final StructLayout MA_DECODER_CONFIG_LAYOUT = MemoryLayout.structLayout(
            ValueLayout.JAVA_INT.withName("format"),
            ValueLayout.JAVA_INT.withName("channels"),
            ValueLayout.JAVA_INT.withName("sampleRate"),
            ValueLayout.JAVA_INT.withName("ditherMode")
    ).withName("ma_decoder_config");

    private Arena sessionArena;
    private boolean isPlaying = false;
    private MemorySegment device;
    private MemorySegment decoder;

    @Override
    public boolean isAvailable() {
        return IS_AVAILABLE_FLAG;
    }

    @Override
    public String getBackendName() {
        return "Native Audio (miniaudio)";
    }

    @Override
    public void load(String path) throws Exception {
        if (!isAvailable()) throw new UnsupportedOperationException("Native audio library not available");
        
        if (sessionArena != null) {
            stop();
            sessionArena.close();
        }
        
        this.sessionArena = Arena.ofConfined();
        
        // 1. Initialize Decoder
        decoder = sessionArena.allocate(256); // Placeholder size for ma_decoder struct
        MemorySegment pathSegment = sessionArena.allocateFrom(path);
        
        // ma_decoder_config config = ma_decoder_config_init(format, channels, sampleRate)
        MemorySegment config = sessionArena.allocate(MA_DECODER_CONFIG_LAYOUT);
        config.set(ValueLayout.JAVA_INT, MA_DECODER_CONFIG_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("format")), 0); // unknown
        
        try {
            int result = (int) MA_DECODER_INIT_FILE.invokeExact(pathSegment, MemorySegment.NULL, decoder);
            if (result != 0) throw new RuntimeException("ma_decoder_init_file failed with error code: " + result);

            // 2. Initialize Device
            device = sessionArena.allocate(512); // Placeholder size for ma_device struct
            MemorySegment deviceConfig = sessionArena.allocate(MA_DEVICE_CONFIG_LAYOUT);
            // Simplified config init
            deviceConfig.set(ValueLayout.JAVA_INT, MA_DEVICE_CONFIG_LAYOUT.byteOffset(MemoryLayout.PathElement.groupElement("deviceType")), 1); // playback
            
            result = (int) MA_DEVICE_INIT.invokeExact(MemorySegment.NULL, deviceConfig, device);
            if (result != 0) throw new RuntimeException("ma_device_init failed with error code: " + result);
        } catch (Throwable t) {
            throw new RuntimeException("Native audio initialization failed", t);
        }
        
        System.out.println("[NativeAudio] Loaded: " + path);
    }

    @Override
    public void play() {
        if (!isAvailable() || device == null) return;
        if (isPlaying) return;
        
        try {
            int result = (int) MA_DEVICE_START.invokeExact(device);
            if (result == 0) isPlaying = true;
            System.out.println("[NativeAudio] Playing...");
        } catch (Throwable t) {
            throw new RuntimeException("Failed to start playback", t);
        }
    }

    @Override
    public void pause() {
        if (!isAvailable() || device == null) return;
        if (!isPlaying) return;
        
        try {
            int result = (int) MA_DEVICE_STOP.invokeExact(device);
            if (result == 0) isPlaying = false;
            System.out.println("[NativeAudio] Paused.");
        } catch (Throwable t) {
            throw new RuntimeException("Failed to pause playback", t);
        }
    }

    @Override
    public void stop() {
        if (!isAvailable()) return;
        pause();
    }

    @Override
    public double getTime() {
        return 0.0;
    }

    @Override
    public double getDuration() {
        if (!isAvailable() || decoder == null) return 0.0;
        try {
            long frames = (long) MA_DECODER_GET_LENGTH.invokeExact(decoder);
            return (double) frames / 44100.0; // Assume 44.1kHz for now
        } catch (Throwable t) {
            return 0.0;
        }
    }

    @Override
    public float[] getSpectrum() {
        return new float[0];
    }
}
