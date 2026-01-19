package org.jscience.history.time;

import java.time.LocalTime;


/**
 * Represents time in the Decimal Time system (French Revolutionary Time).
 * 
 * <p>The day is divided into 10 decimal hours.
 * Each decimal hour is divided into 100 decimal minutes.
 * Each decimal minute is divided into 100 decimal seconds.
 * 
 * <p>1 Decimal Day = 100,000 Decimal Seconds = 86,400 SI Seconds.
 * 1 Decimal Second = 0.864 SI Seconds.
 */
public final class DecimalTime {

    public static final int HOURS_PER_DAY = 10;
    public static final int MINUTES_PER_HOUR = 100;
    public static final int SECONDS_PER_MINUTE = 100;
    public static final int SECONDS_PER_DAY = HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE;

    private final int hour;
    private final int minute;
    private final int second;
    
    // Optional fractional part? For now store integers.
    // 1 decimal second precision is ~0.864s. Good enough? 
    // Maybe we want milliseconds (1000 per decimal second)? 
    // Old code used "millis".
    // 1 day = 10^5 seconds = 10^8 millis?
    // Let's stick to second precision or add nanos if requested.
    // The old code had "millis" (1000 per decimal second).
    
    /**
     * Creates a DecimalTime.
     * @param hour 0-9
     * @param minute 0-99
     * @param second 0-99
     */
    public DecimalTime(int hour, int minute, int second) {
        if (hour < 0 || hour >= HOURS_PER_DAY) throw new IllegalArgumentException("Hour must be 0-9");
        if (minute < 0 || minute >= MINUTES_PER_HOUR) throw new IllegalArgumentException("Minute must be 0-99");
        if (second < 0 || second >= SECONDS_PER_MINUTE) throw new IllegalArgumentException("Second must be 0-99");
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Converts standard LocalTime to DecimalTime.
     */
    public static DecimalTime from(LocalTime time) {
        long nanosOfDay = time.toNanoOfDay();
        // Total nanos in day = 86,400 * 1,000,000,000
        // Total decimal seconds in day = 100,000
        // decSeconds = (nanosOfDay / 86,400,000,000,000) * 100,000 ? No.
        // decSeconds = nanosOfDay / (86,400 * 10^9 / 100,000)
        //            = nanosOfDay / 864,000,000
        
        double totalDecimalSeconds = nanosOfDay / 864_000_000.0;
        
        int h = (int) (totalDecimalSeconds / (MINUTES_PER_HOUR * SECONDS_PER_MINUTE));
        double rem = totalDecimalSeconds % (MINUTES_PER_HOUR * SECONDS_PER_MINUTE);
        
        int m = (int) (rem / SECONDS_PER_MINUTE);
        int s = (int) (rem % SECONDS_PER_MINUTE);
        
        return new DecimalTime(h, m, s);
    }

    /**
     * Converts this DecimalTime to standard LocalTime.
     */
    public LocalTime toLocalTime() {
        long totalDecimalSeconds = (hour * MINUTES_PER_HOUR * SECONDS_PER_MINUTE) + 
                                   (minute * SECONDS_PER_MINUTE) + second;
        
        // Convert to nanos
        long nanos = totalDecimalSeconds * 864_000_000L;
        return LocalTime.ofNanoOfDay(nanos);
    }

    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getSecond() { return second; }

    @Override
    public String toString() {
        return String.format("%d:%02d:%02d", hour, minute, second);
    }
}
