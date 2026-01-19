package org.jscience.history.calendars;

/**
 * Bahai Calendar.
 * Based on the Solar Year.
 * 19 Months of 19 Days + Ayyam-i-Ha (Intercalary Days).
 * Era: Badi (begins 1844 AD).
 */
public class BahaiCalendar extends SevenDaysWeek {
    
    private static final String[] DAYS = {
            "Jamal", "Kamal", "Fidal", "`Idal", "Istijlal", "Istiqlal", "Jalal"
    };

    private static final String[] MONTHS = {
            "Baha", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat",
            "Kamal", "Asma'", "`Izzat", "Mashiyyat", "`Ilm", "Qudrat", "Qawl",
            "Masail", "Sharaf", "Sultan", "Mulk", "Ayyam-i-Ha", "`Ala'"
    };

    private static final String[] YEARS = {
            "Alif", "Ba'", "Ab", "Dal", "Bab", "Vav", "Abad", "Jad", "Baha",
            "Hubb", "Bahhaj", "Javab", "Ahad", "Vahhab", "Vidad", "Badi'",
            "Bahi'", "Abha", "Vahid"
    };

    public static final long EPOCH = (new GregorianCalendar(3, 21, 1844)).toRD();

    private int major;
    private int cycle;
    private int year;
    private int month;
    private int day;

    public BahaiCalendar() {
        this(EPOCH);
    }

    public BahaiCalendar(long l) {
        set(l);
    }

    public BahaiCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    public BahaiCalendar(int major, int cycle, int year, int month, int day) {
        set(major, cycle, year, month, day);
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    public synchronized void set(int major, int cycle, int year, int month, int day) {
        this.major = major;
        this.cycle = cycle;
        this.year = year;
        this.month = month;
        this.day = day;
        recomputeRD();
    }

    @Override
    protected synchronized void recomputeRD() {
        int i = (((361 * (major - 1)) + (19 * (cycle - 1)) + year) - 1) + 1844;
        long rdBase = (new GregorianCalendar(3, 20, i)).toRD();

        // Start from Nau-Ruz
        super.rd = rdBase + (long) (19 * (month - 1));

        // Adjust for Ayyam-i-Ha (Month 19 is 'Ala, Month 18 is Mulk. 
        // Wait, Ayyam-i-Ha is usually inserted before the last month.
        // In this implementation, MONTHS[18] is Ayyam-i-Ha (index 18 -> 19th item in array?). 
        // Ah, array size is 20. 
        // 1..18 normal months. 
        // 19 = Ayyam-i-Ha? 
        // 20 = `Ala` (Fast)
        
        if (month == 20) {
             // If we are in the last month ('Ala), we must have passed Ayyam-i-Ha.
             // How many days in Ayyam-i-Ha?
             // Common: 4, Leap: 5.
             if (GregorianCalendar.isLeapYear(i + 1)) {
                super.rd -= 14L; // (19 * 19) calculation assumed uniform 19?
                // Old code logic:
                // super.rd += (long)(19 * (month - 1));
                // if month=20, we added 19*19 = 361 days. 
                // But we should have added 18*19 + lengthOfAyyamIHa?
                // 18*19 = 342.
                // 361 - 342 = 19. 
                // Ayyam-i-Ha is 4 or 5 days. 
                // So we overshot by 19 - 4 = 15 or 19 - 5 = 14.
                // Hence: if leap -> rd -= 14 (adding 5 effective). else rd -= 15 (adding 4 effective).
             } else {
                super.rd -= 15L;
             }
        }
        
        // Wait, if month is 19 (Ayyam-i-Ha), then we added 19*18 = 342.
        // And we are IN Ayyam-i-Ha, day runs 1..5?
        // Logic seems to treat month 19 as a regular month in first line? 
        // No, if month=19, we added 19*18. That's correct start of Ayyam-i-Ha.
        
        super.rd += day;
    }

    @Override
    protected synchronized void recomputeFromRD() {
        // Find Gregorian Year
        int i = (new GregorianCalendar(super.rd)).getYear();
        int j = i - 1844;

        if (((new GregorianCalendar(1, 1, i)).toRD() <= super.rd) &&
                (super.rd <= (new GregorianCalendar(3, 20, i)).toRD())) {
            j--;
        }

        major = (int) AlternateCalendar.fldiv(j, 361L) + 1;
        
        long mod361 = AlternateCalendar.mod(j, 361);
        cycle = (int) AlternateCalendar.fldiv(mod361, 19L) + 1;
        year = (int) AlternateCalendar.mod(j, 19) + 1;

        // Calculate start of year
        long l = super.rd - (new BahaiCalendar(major, cycle, year, 1, 1)).toRD();
        
        // Approximate check: Month 20 ('Ala)
        BahaiCalendar bahai = new BahaiCalendar(major, cycle, year, 20, 1);

        if (super.rd >= bahai.toRD()) {
            month = 20;
        } else {
            // Standard 19 day months
            month = (int) AlternateCalendar.fldiv(l, 19L) + 1;
        }

        BahaiCalendar currentMonthStart = new BahaiCalendar(major, cycle, year, month, 1);
        day = (int) ((super.rd + 1L) - currentMonthStart.toRD());
    }

    @Override
    public String toString() {
        int w = weekDay();
        int mIdx = month - 1;
        // Logic from old code regarding index 18 (Ayyam-i-Ha?)
        // If "j == 18" (month 19?), increment?
        // Old code: int j = day - 1? No, month index? 
        // "int j = day - 1;" "if (j == 18) j++;" 
        // Wait, DAYS has 7 items, MONTHS has 20 items. 
        // The old code uses `MONTHS[j]` where j was derived from `day`? 
        // That seems wrong. It says "day of [MonthName]".
        // Ah, Bahai days also have names (19 names). 
        // But DAYS array only has 7 names (Weekday names).
        // Where are the day names? 
        // In the old code `MONTHS` is reused?
        // "MONTHS[j]" -> `day - 1`. Yes, Bahai days of month have same names as Months of year.
        // Except Month 19 (Ayyam-i-Ha) is not a day name. Day 19 is 'Ala?
        // Let's check:
        // Month Names: 1..18, 19=Ayyam-i-Ha, 20='Ala.
        // Day Names: 1..19. 
        // If day is 19, name is 'Ala (which is MONTHS[19] in old code index? No MONTHS[19] is 'Ala).
        // But MONTHS[18] is Ayyam-i-Ha. Day 18 is Mulk? 
        // If day=19, index=18. If index=18, old code increments to 19 ('Ala). 
        // Because "Ayyam-i-Ha" is not a day name.
        
        int dayNameIndex = day - 1;
        if (dayNameIndex == 18) { // If it hits Ayyam-i-Ha index
             dayNameIndex++;      // Skip to 'Ala
        }

        try {
            return DAYS[w] + " (" + SevenDaysWeek.DAYNAMES[w] +
            ") the day of " + MONTHS[dayNameIndex] + " of the month of " +
            MONTHS[month - 1] + " of the year of " + YEARS[year - 1] +
            " of Vahid " + cycle + " of Kull-i-Shay " + major + "\n(" + major +
            " " + cycle + " " + year + " " + month + " " + day + ")";
        } catch (Exception e) {
            return "Invalid date: " + major + "-" + cycle + "-" + year;
        }
    }
}
