package org.jscience.history.calendars;

public abstract class SevenDaysWeek extends AlternateCalendar {
    
    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    public static final String[] DAYNAMES = {
            "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    };

    public SevenDaysWeek() {
    }

    public int weekDay() {
        return (int) AlternateCalendar.mod(toRD(), 7);
    }

    private static int weekDay(long l) {
        return (int) AlternateCalendar.mod(l, 7);
    }

    public void kDayOnOrBefore(int i) {
        long l = toRD();
        subtract(weekDay(l - (long) i));
    }

    public void kDayOnOrAfter(int i) {
        add(6L);
        kDayOnOrBefore(i);
    }

    public void kDayNearest(int i) {
        add(3L);
        kDayOnOrBefore(i);
    }

    public void kDayAfter(int i) {
        add(7L);
        kDayOnOrBefore(i);
    }

    public void kDayBefore(int i) {
        add(1L);
        kDayOnOrBefore(i);
    }

    public void nthKDay(int i, int j) {
        if (i < 0) {
            kDayBefore(j);
        } else {
            kDayAfter(j);
        }
        add(7 * i);
    }
}
