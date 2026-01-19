package org.jscience.history.time;

import java.time.LocalTime;

/**
 * Chinese Time (Traditional).
 * Day divided into 12 Shi (double hours) and 100 Ke.
 * 1 Shi = 2 hours.
 * 1 Ke = 14.4 minutes (14m 24s).
 * This implementation models the 100 Ke system.
 */
public class ChineseTime {

    public static final int KE_PER_DAY = 100;
    
    // Each Ke is 14.4 minutes.
    // Subdivisions usually Fen (100 per Ke? Or 60?)
    // In strict decimal tradition (100 Ke), sub-units might be decimalized too.
    // Old code used "Minutes per Hour" = 60, "Hours per Day" = 100?
    // Wait, old code ChineseTime.java: 
    // HOURS_PER_DAY = 100.
    // MINUTES_PER_HOUR = 60.
    // SECONDS_PER_MINUTE = 60.
    // So 1 "Hour" = 1 Ke (14.4 mins).
    // 1 "Minute" = 1/60 Ke = 14.4s.
    // 1 "Second" = 1/60 Minute = 0.24s.
    
    private final int ke;
    private final int fen; // "Minute"
    private final int miao; // "Second"

    public ChineseTime(int ke, int fen, int miao) {
        this.ke = ke;
        this.fen = fen;
        this.miao = miao;
    }

    public static ChineseTime from(LocalTime time) {
        long nanos = time.toNanoOfDay();
        double totalSeconds = nanos / 1_000_000_000.0;
        
        // 1 Ke = 864 seconds
        // 1 Fen = 864 / 60 = 14.4 seconds
        // 1 Miao = 14.4 / 60 = 0.24 seconds
        
        int k = (int) (totalSeconds / 864.0);
        double rem = totalSeconds % 864.0;
        
        int f = (int) (rem / 14.4);
        rem = rem % 14.4;
        
        int m = (int) (rem / 0.24);
        
        return new ChineseTime(k, f, m);
    }
    
    @Override
    public String toString() {
        return String.format("%d ke %d fen %d miao", ke, fen, miao);
    }
}
