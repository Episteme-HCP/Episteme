package org.jscience.history.time;

import java.time.LocalTime;

/**
 * Modern (Standard) Time.
 * A simple wrapper around standard time components, primarily for consistency with other Time implementations.
 * Note: Uses 24h format by default, but can simulate AM/PM presentation.
 */
public class ModernTime {
    
    private final int hour;
    private final int minute;
    private final int second;
    private final int nano;

    public ModernTime(int hour, int minute, int second, int nano) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.nano = nano;
    }
    
    public static ModernTime from(LocalTime time) {
        return new ModernTime(time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
    }

    public LocalTime toLocalTime() {
        return LocalTime.of(hour, minute, second, nano);
    }
    
    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d.%09d", hour, minute, second, nano);
    }
}
