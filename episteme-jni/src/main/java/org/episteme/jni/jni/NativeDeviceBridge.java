/*
 * Episteme - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.episteme.jni.jni;

/**
 * JNI Bridge for communicating with real hardware devices via C++/Rust native
 * libraries.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class NativeDeviceBridge {

    static {
        try {
            System.loadLibrary("episteme-jni");
        } catch (UnsatisfiedLinkError e) {
            // Fallback for development/test environments
            boolean loaded = false;
            String libName = System.mapLibraryName("episteme-jni");
            String[] searchPaths = { 
                "libs", "../libs", "../../libs", "episteme-jni/libs", 
                System.getProperty("user.dir") + "/libs"
            };
            
            for (String path : searchPaths) {
                java.io.File file = new java.io.File(path, libName);
                if (file.exists()) {
                    try {
                        System.load(file.getAbsolutePath());
                        loaded = true;
                        break;
                    } catch (Throwable ignored) {}
                }
            }
            
            if (!loaded) {
                System.err.println("Native library 'episteme-jni' failed to load via standard and fallback paths.\n" + e);
            }
        }
    }

    /**
     * Connects to a physical device.
     * 
     * @param deviceId The device identifier.
     * @return true if successful.
     */
    public native boolean connectDevice(String deviceId);

    /**
     * Disconnects from a physical device.
     * 
     * @param deviceId The device identifier.
     */
    public native void disconnectDevice(String deviceId);

    /**
     * Reads a double value from a sensor.
     * 
     * @param deviceId The sensor identifier.
     * @return The measured value.
     */
    public native double readSensorValue(String deviceId);

    /**
     * Acquires a spectrum array from a spectrometer.
     * 
     * @param deviceId The device identifier.
     * @return The spectrum data.
     */
    public native double[] acquireSpectrum(int deviceId);
}
