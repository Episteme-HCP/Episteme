package org.jscience.jni;

/**
 * JNI Bridge for communicating with real hardware devices via C++/Rust native libraries.
 * 
 * @author Silvere Martin-Michiellot
 * @since 1.0
 */
public class NativeDeviceBridge {

    static {
        try {
            System.loadLibrary("jscience_native");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native library 'jscience_native' failed to load.\n" + e);
        }
    }

    /**
     * Connects to a physical device.
     * @param deviceId The device identifier.
     * @return true if successful.
     */
    public native boolean connectDevice(String deviceId);

    /**
     * Disconnects from a physical device.
     * @param deviceId The device identifier.
     */
    public native void disconnectDevice(String deviceId);

    /**
     * Reads a double value from a sensor.
     * @param deviceId The sensor identifier.
     * @return The measured value.
     */
    public native double readSensorValue(String deviceId);
}
