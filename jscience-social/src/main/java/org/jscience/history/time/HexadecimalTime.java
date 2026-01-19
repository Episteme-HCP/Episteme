package org.jscience.history.time;

import java.time.LocalTime;

/**
 * Hexadecimal Time.
 * Day divided into 16 Hexadecimal Hours (0-F).
 * Hour divided into 256 Hexadecimal Minutes (00-FF) ? No, usually 16 or 256.
 * Old code:
 * HOURS_PER_DAY = 16
 * MAXIMES_PER_HOUR = 16
 * MINUTES_PER_MAXIME = 16
 * SECONDS_PER_MINUTE = 16
 * 
 * So it's a base-16 breakdown: Day -> Hour -> Maxime -> Minute -> Second.
 * 1 Hex Hour = 1.5 Std Hour (90 min).
 * 1 Maxime = 90/16 = 5.625 min.
 * 1 Hex Minute = 5.625 / 16 min = ~21 sec.
 * 1 Hex Second = ~1.31 sec.
 */
public class HexadecimalTime {

    private final int hour;
    private final int maxime;
    private final int minute;
    private final int second;

    public HexadecimalTime(int hour, int maxime, int minute, int second) {
        this.hour = hour;
        this.maxime = maxime;
        this.minute = minute;
        this.second = second;
    }

    public static HexadecimalTime from(LocalTime time) {
        long nanos = time.toNanoOfDay();
        double totalSeconds = nanos / 1_000_000_000.0;
        
        // Total day seconds = 86400
        // Total hex seconds = 16^4 = 65536
        // Conversion factor = 65536/86400 ~= 0.7585
        
        double totalHexSeconds = totalSeconds * (65536.0 / 86400.0);
        
        int h = (int) (totalHexSeconds / 4096); // 16^3
        double rem = totalHexSeconds % 4096;
        
        int mx = (int) (rem / 256); // 16^2
        rem = rem % 256;
        
        int m = (int) (rem / 16); // 16^1
        int s = (int) (rem % 16);
        
        return new HexadecimalTime(h, mx, m, s);
    }
    
    @Override
    public String toString() {
        return String.format(".%X%X%X%X", hour, maxime, minute, second);
    }
}
