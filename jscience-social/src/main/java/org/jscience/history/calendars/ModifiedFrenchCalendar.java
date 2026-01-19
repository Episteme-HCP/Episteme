package org.jscience.history.calendars;



/**
 * Modified French Republican Calendar (Romme system).
 * This simpler version uses a fixed leap year rule similar to Gregorian but with 4000 year exception.
 */
public class ModifiedFrenchCalendar extends MonthDayYear {
    
    protected static final long EPOCH = (new GregorianCalendar(9, 22, 1792)).toRD();

    private static final String[] UNIMONTHS = {
            "Vendémiaire", "Brumaire", "Frimaire", "Nivôse", "Pluviôse",
            "Ventôse", "Germinal", "Floréal", "Prairial", "Messidor",
            "Thermidor", "Fructidor"
        };

    private static final String[] MONTHS = {
            "Vendemiaire", "Brumaire", "Frimaire", "Nivose", "Pluviose",
            "Ventose", "Germinal", "Floreal", "Prairial", "Messidor",
            "Thermidor", "Fructidor"
        };

    protected static final String[] DAYS = {
            "Primidi", "Duodi", "Tridi", "Quartidi", "Quintidi", "Sextidi",
            "Septidi", "Octidi", "Nonidi", "Decadi"
        };

    protected static final String[] JOURS = {
            "de la Vertu", "du Genie", "du Labour", "de la Raison",
            "de la Recompense", "de la Revolution"
        };

    public ModifiedFrenchCalendar() {
        this(EPOCH);
    }

    public ModifiedFrenchCalendar(long l) {
        set(l);
    }

    public ModifiedFrenchCalendar(int i, int j, int k) {
        set(i, j, k);
    }

    public ModifiedFrenchCalendar(AlternateCalendar altcalendar) {
        set(altcalendar.toRD());
    }

    public static boolean isLeapYear(int i) {
        int j = (int) AlternateCalendar.mod(i, 400);

        return (AlternateCalendar.mod(i, 4) == 0) && (j != 100) && (j != 200) &&
        (j != 300) && (AlternateCalendar.mod(i, 4000) != 0);
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    @Override
    protected synchronized void recomputeRD() {
        int i = super.year - 1;
        super.rd = ((((EPOCH - 1L) + (long) (365 * i) +
            AlternateCalendar.fldiv(i, 4L)) - AlternateCalendar.fldiv(i, 100L)) +
            AlternateCalendar.fldiv(i, 400L)) -
            AlternateCalendar.fldiv(i, 4000L);
        super.rd += ((30 * (super.month - 1)) + super.day);
    }

    @Override
    protected synchronized void recomputeFromRD() {
        // Initial estimate for the year
        int i = (int) Math.floor((double) (super.rd - EPOCH) / 365.24225D);
        int j = i;
        super.year = i - 1;

        ModifiedFrenchCalendar modfrench;

        // Converge to the correct year
        for (modfrench = new ModifiedFrenchCalendar(1, 1, j);
                super.rd >= modfrench.toRD(); modfrench.set(1, 1, j)) {
            super.year++;
            j++;
        }

        // Adjust back
        modfrench.set(1, 1, super.year);
        super.month = (int) AlternateCalendar.fldiv(super.rd - modfrench.toRD(), 30L) + 1;
        modfrench.set(super.month, 1, super.year);
        super.day = (int) ((super.rd - modfrench.toRD()) + 1L);
    }

    public int getWeekDay() {
        // 10 day weeks (Decades)
        return ((super.day - 1) % 10) + 1;
    }

    public int getDecade() {
        return ((super.day - 1) / 10) + 1;
    }

    @Override
    protected String monthName() {
        if (AlternateCalendar.unicode) {
            return UNIMONTHS[Math.max(0, Math.min(super.month - 1, UNIMONTHS.length - 1))];
        } else {
            return MONTHS[Math.max(0, Math.min(super.month - 1, MONTHS.length - 1))];
        }
    }

    @Override
    protected String getSuffix() {
        return "";
    }

    @Override
    public java.util.List<String> getMonths() {
        if (AlternateCalendar.unicode) {
            return java.util.Arrays.asList(UNIMONTHS);
        } else {
            return java.util.Arrays.asList(MONTHS);
        }
    }


    @Override
    public String toString() {
        String[] as = { "I", "II", "III" };

        try {
            if (super.month < 13) {
                return "Decade " + as[getDecade() - 1] + ", " +
                DAYS[getWeekDay() - 1] + " de " + monthName() + ", " +
                super.year;
            } else {
                return "Jour " + JOURS[getWeekDay() - 1] + ", " + super.year;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return "Invalid date: " + super.month + " " + super.day + " " + super.year;
        }
    }

    public static void main(String[] args) {
        int i, j, k;
        try {
            i = Integer.parseInt(args[0]);
            j = Integer.parseInt(args[1]);
            k = Integer.parseInt(args[2]);
        } catch (Exception e) {
            i = 1; j = 1; k = 1;
        }

        GregorianCalendar gregorian = new GregorianCalendar(i, j, k);
        System.out.println(gregorian.toRD());
        System.out.println(gregorian + "\n");

        ModifiedFrenchCalendar modfrench = new ModifiedFrenchCalendar(gregorian);
        System.out.println(gregorian + ": " + modfrench);
    }
}
