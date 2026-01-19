package org.jscience.history.time;

import java.time.LocalTime;

/**
 * Internet Time (Swatch Internet Time).
 * Unit: .beat (1/1000 of a day).
 * Timezone: BMT (Biel Mean Time) = UTC+1.
 */
public class InternetTime {

    public static final int BEATS_PER_DAY = 1000;
    
    private final int beats;

    public InternetTime(int beats) {
        if (beats < 0 || beats >= BEATS_PER_DAY) {
            throw new IllegalArgumentException("Beats must be between 0 and 999");
        }
        this.beats = beats;
    }

    public static InternetTime from(LocalTime localTimeUTC) {
        // Internet Time is BMT (UTC+1)
        // Convert input (assumed UTC? Or System Default?)
        // Let's assume input is LocalTime representing UTC for simplicity of "logic", 
        // or user passes BMT LocalTime.
        // Actually, best is to calculate from seconds in day.
        
        long nanos = localTimeUTC.toNanoOfDay();
        // Shift to BMT (UTC+1) -> +3600 seconds
        long nanosBMT = nanos + (3600L * 1_000_000_000L);
        if (nanosBMT >= 24L * 3600L * 1_000_000_000L) {
            nanosBMT -= 24L * 3600L * 1_000_000_000L;
        }

        // BMT Seconds in day
        double secondsBMT = nanosBMT / 1_000_000_000.0;
        
        // 1 beat = 86.4 seconds
        int beats = (int) (secondsBMT / 86.4);
        return new InternetTime(beats);
    }
    
    /**
     * Creates Internet Time from current system time (System.currentTimeMillis).
     * Automatically handles BMT offset.
     */
    public static InternetTime now() {
        // Current millis since epoch (UTC)
        long now = System.currentTimeMillis();
        // Add BMT offset (1 hour)
        long bmt = now + 3600000; 
        
        long millisInDay = bmt % 86400000;
        if (millisInDay < 0) millisInDay += 86400000;
        
        return new InternetTime((int)(millisInDay / 86400));
    }

    public int getBeats() {
        return beats;
    }

    @Override
    public String toString() {
        return String.format("@%03d", beats);
    }
}
