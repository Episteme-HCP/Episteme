/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
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

package org.jscience.device.sim;

import org.jscience.device.transducers.Telescope;
import org.jscience.mathematics.numbers.real.Real;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.BiConsumer;

/**
 * Simulated telescope for testing and demonstration purposes.
 * <p>
 * Models realistic slewing behavior with configurable slew rate.
 * Supports position callbacks for UI updates during slew operations.
 * </p>
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class SimulatedTelescope implements Telescope {

    private static final String DEFAULT_NAME = "SimulatedTelescope";

    private final String name;
    private double currentRA = 0.0; // Hours (0-24)
    private double currentDec = 0.0; // Degrees (-90 to +90)
    private double targetRA = 0.0;
    private double targetDec = 0.0;

    private boolean isSlewing = false;
    private boolean isConnected = false;

    // Slew rate in degrees per second
    private double slewRate = 2.0;

    // Timer for slew simulation
    private Timer slewTimer;

    // Callback for position updates
    private BiConsumer<Double, Double> positionCallback;

    public SimulatedTelescope() {
        this(DEFAULT_NAME);
    }

    public SimulatedTelescope(String name) {
        this.name = name;
        // Default starting position: Polaris area
        this.currentRA = 2.5;
        this.currentDec = 89.0;
    }

    public SimulatedTelescope(double initialRA, double initialDec) {
        this.name = DEFAULT_NAME;
        this.currentRA = normalizeRA(initialRA);
        this.currentDec = clampDec(initialDec);
    }

    // ========== Device interface methods ==========

    @Override
    public void connect() throws IOException {
        isConnected = true;
    }

    @Override
    public void disconnect() throws IOException {
        abort();
        isConnected = false;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return name;
    }

    @Override
    public String getManufacturer() {
        return "Simulated Device";
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    // ========== Actuator interface methods ==========

    @Override
    public void send(String command) throws IOException {
        if (command != null && command.startsWith("GOTO:")) {
            String[] parts = command.substring(5).split(",");
            if (parts.length == 2) {
                double ra = Double.parseDouble(parts[0].trim());
                double dec = Double.parseDouble(parts[1].trim());
                slewTo(ra, dec);
            }
        } else if ("ABORT".equals(command)) {
            abort();
        }
    }

    // ========== Telescope interface methods ==========

    @Override
    public void slewTo(double ra, double dec) throws IOException {
        if (!isConnected) {
            throw new IOException("Telescope not connected");
        }

        abort(); // Stop any current slew

        this.targetRA = normalizeRA(ra);
        this.targetDec = clampDec(dec);
        this.isSlewing = true;

        // Start slew simulation
        slewTimer = new Timer("TelescopeSlew", true);
        slewTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updatePosition();
            }
        }, 0, 100); // Update every 100ms
    }

    @Override
    public void syncTo(double ra, double dec) throws IOException {
        if (!isConnected) {
            throw new IOException("Telescope not connected");
        }
        this.currentRA = normalizeRA(ra);
        this.currentDec = clampDec(dec);
        notifyPositionChange();
    }

    @Override
    public double getRightAscension() throws IOException {
        return currentRA;
    }

    @Override
    public double getDeclination() throws IOException {
        return currentDec;
    }

    @Override
    public void abort() throws IOException {
        isSlewing = false;
        if (slewTimer != null) {
            slewTimer.cancel();
            slewTimer = null;
        }
    }

    // ========== Additional methods ==========

    /**
     * Returns the current status string.
     */
    public String getStatus() {
        if (!isConnected)
            return "DISCONNECTED";
        if (isSlewing)
            return "SLEWING";
        return "TRACKING";
    }

    /**
     * Sets the slew rate in degrees per second.
     */
    public void setSlewRate(double degreesPerSecond) {
        this.slewRate = degreesPerSecond;
    }

    /**
     * Gets the current slew rate.
     */
    public double getSlewRate() {
        return slewRate;
    }

    /**
     * Returns true if the telescope is currently slewing.
     */
    public boolean isSlewing() {
        return isSlewing;
    }

    /**
     * Sets a callback for position updates during slewing.
     */
    public void setPositionCallback(BiConsumer<Double, Double> callback) {
        this.positionCallback = callback;
    }

    /**
     * Returns the angular distance to the target in degrees.
     */
    public double distanceToTarget() {
        double dRA = (targetRA - currentRA) * 15.0; // Convert hours to degrees
        double dDec = targetDec - currentDec;
        return Math.sqrt(dRA * dRA + dDec * dDec);
    }

    private void updatePosition() {
        double step = slewRate * 0.1; // Per 100ms

        // Calculate deltas
        double dRA = targetRA - currentRA;
        double dDec = targetDec - currentDec;

        // Handle RA wraparound
        if (dRA > 12)
            dRA -= 24;
        if (dRA < -12)
            dRA += 24;

        double distance = Math.sqrt((dRA * 15) * (dRA * 15) + dDec * dDec);

        if (distance < step) {
            // Arrived at target
            currentRA = targetRA;
            currentDec = targetDec;
            isSlewing = false;
            if (slewTimer != null) {
                slewTimer.cancel();
                slewTimer = null;
            }
        } else {
            // Move toward target
            double ratio = step / distance;
            currentRA = normalizeRA(currentRA + dRA * ratio);
            currentDec = clampDec(currentDec + dDec * ratio);
        }

        notifyPositionChange();
    }

    private void notifyPositionChange() {
        if (positionCallback != null) {
            positionCallback.accept(currentRA, currentDec);
        }
    }

    private double normalizeRA(double ra) {
        while (ra < 0)
            ra += 24;
        while (ra >= 24)
            ra -= 24;
        return ra;
    }

    private double clampDec(double dec) {
        return Math.max(-90, Math.min(90, dec));
    }

    /**
     * Formats RA as HH:MM:SS string.
     */
    public static String formatRA(double ra) {
        int hours = (int) ra;
        int minutes = (int) ((ra - hours) * 60);
        double seconds = ((ra - hours) * 60 - minutes) * 60;
        return String.format("%02dh %02dm %05.2fs", hours, minutes, seconds);
    }

    /**
     * Formats Dec as +/-DDÃ‚Â°MM'SS" string.
     */
    public static String formatDec(double dec) {
        char sign = dec >= 0 ? '+' : '-';
        dec = Math.abs(dec);
        int degrees = (int) dec;
        int minutes = (int) ((dec - degrees) * 60);
        double seconds = ((dec - degrees) * 60 - minutes) * 60;
        return String.format("%c%02dÃ‚Â° %02d' %05.2f\"", sign, degrees, minutes, seconds);
    }

    @Override
    public Real readValue() throws IOException {
        // Return 0 for now as dummy sensor data
        return Real.ZERO;
    }
}
