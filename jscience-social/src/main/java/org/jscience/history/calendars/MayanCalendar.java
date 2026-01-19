package org.jscience.history.calendars;



public class MayanCalendar extends AlternateCalendar {
    
    // Julian Day 584284.5
    static final long EPOCH = AlternateCalendar.fromJD(584284.5D);

    static final String[] HAAB_MONTHS = {
            "Pop", "Uo", "Zip", "Zotz", "Tzec", "Xul", "Yaxkin", "Mol", "Chen",
            "Yax", "Zac", "Ceh", "Mac", "Kankin", "Muan", "Pax", "Kayab",
            "Cumku", "Uayeb"
    };

    static final String[] TZOLKIN_NAMES = {
            "Imix", "Ik", "Akbal", "Kan", "Chicchan", "Cimi", "Manik", "Lamat",
            "Muluc", "Oc", "Chuen", "Eb", "Ben", "Ix", "Men", "Cib", "Caban",
            "Etznab", "Cauac", "Ahau"
    };

    public static final MayanCalendar EPOCH_OBJ;
    public static final Haab HAAB_EPOCH;
    public static final Tzolkin TZOLKIN_EPOCH;

    static {
        EPOCH_OBJ = new MayanCalendar(8, 18, 4, 20); // 0.0.0.0.0 is not start? 
        // Note: 8.18.4.20 seems to be the correlation used here?
        // Wait, standard Mayan epoch 0.0.0.0.0 corresponds to a specific date. 
        // 584284.5 JD is Sep 6, 3114 BCE (Julian) - The standard correlation (GMT).
        // 8.18 is... ? 
        // Let's trust the logic from the old file as "Scientific Truth" for this port.
        // But 8,18,4,20 args maps to set(i,j,k,l) -> Haab(i,j), Tzolkin(k,l) 
        // Constructor (int, int, int, int) lines 107 in old file.
        HAAB_EPOCH = EPOCH_OBJ.haab;
        TZOLKIN_EPOCH = EPOCH_OBJ.tzolkin;
    }

    private long longcount;
    public final Haab haab;
    public final Tzolkin tzolkin;

    public MayanCalendar() {
        this(EPOCH);
    }

    public MayanCalendar(long l) {
        haab = new Haab();
        tzolkin = new Tzolkin();
        set(l);
    }

    public MayanCalendar(AlternateCalendar altcalendar) {
        this(altcalendar.toRD());
    }
    
    /**
     * Constructor for specific Long Count components? 
     * No, the int constructor in old file was (i,j,k,l,i1).
     */
    public MayanCalendar(int baktun, int katun, int tun, int uinal, int kin) {
        haab = new Haab();
        tzolkin = new Tzolkin();
        set(baktun, katun, tun, uinal, kin);
    }

    /**
     * Protected constructor used for EPOCH_OBJ initialization
     * (int i, int j, int k, int l) maps to Haab(i, j) and Tzolkin(k, l)
     */
    protected MayanCalendar(int hDay, int hMonth, int tNum, int tName) {
        longcount = 1L; // Dummy init, will be overwritten? No, logic depends on init.
        // In old file: longcount=1L; super.rd = EPOCH; haab=new Haab(i,j); tzolkin=new Tzolkin(k,l);
        // It initializes the epoch objects specifically.
        
        super.rd = EPOCH;
        longcount = 1L; // The 'set' is not called here.
        haab = new Haab(hDay, hMonth);
        tzolkin = new Tzolkin(tNum, tName);
        
        // This object (EPOCH_OBJ) is special, it doesn't seem to sync longcount with rd in the same way 
        // or it's just used to store the Epoch constants for Haab/Tzolkin.
    }

    public synchronized void set(int baktun, int katun, int tun, int uinal, int kin) {
        longcount = (baktun * 144000L) + (katun * 7200L) + (tun * 360L) + (uinal * 20L) + kin;
        // 0x23280 = 144000
        super.rd = longcount + EPOCH;
        recomputeFromRD();
    }

    @Override
    public synchronized void set(long l) {
        super.rd = l;
        recomputeFromRD();
    }

    @Override
    protected synchronized void recomputeFromRD() {
        longcount = super.rd - EPOCH;
        haab.recompute();
        tzolkin.recompute();
    }

    @Override
    protected synchronized void recomputeRD() {
        // No-op in old code? Because RD is derived from longcount in set(), or longcount from RD.
        // But if we change haab/tzolkin? 
        // This implementation seems to be one-way (RD -> Haab/Tzolkin) largely.
    }

    public String longCountString() {
        long l = AlternateCalendar.mod(longcount, 144000L); // Baktun rem
        long l1 = AlternateCalendar.mod(l, 7200L);          // Katun rem
        long l2 = AlternateCalendar.mod(l1, 360L);          // Tun rem

        return AlternateCalendar.fldiv(longcount, 144000L) + "." + // Baktun
        AlternateCalendar.fldiv(l, 7200L) + "." +                  // Katun
        AlternateCalendar.fldiv(l1, 360L) + "." +                  // Tun
        AlternateCalendar.fldiv(l2, 20L) + "." +                   // Uinal
        AlternateCalendar.mod(l2, 20);                             // Kin
    }

    @Override
    public String toString() {
        return longCountString() + ": " + tzolkin + "; " + haab;
    }

    // --- Inner Classes ---

    public class Haab {
        private int month;
        private int day;

        public Haab() {
            this(MayanCalendar.HAAB_EPOCH);
        }

        public Haab(int day, int month) {
            this.day = day;
            this.month = month;
        }

        public Haab(Haab other) {
            this.day = other.day;
            this.month = other.month;
        }

        public int getMonth() { return month; }
        public int getDay() { return day; }

        public void recompute() {
            // Logic from old code
            // longcount is available from outer class
            
            // i = (longcount + epoch_day + 20*(epoch_month-1)) % 365
            // Note: HAAB_EPOCH is accessed statically
            int i = (int) AlternateCalendar.mod(longcount +
                    (long) MayanCalendar.HAAB_EPOCH.getDay() +
                    (long) (20 * (MayanCalendar.HAAB_EPOCH.getMonth() - 1)), 365);
            day = i % 20;
            month = (i / 20) + 1;
        }

        @Override
        public String toString() {
            try {
                return day + " " + MayanCalendar.HAAB_MONTHS[month - 1];
            } catch (Exception e) {
                return "Invalid Date: " + day + " " + month;
            }
        }
    }

    public class Tzolkin {
        private int number;
        private int name;

        public Tzolkin() {
            this(MayanCalendar.TZOLKIN_EPOCH);
        }

        public Tzolkin(int number, int name) {
            this.number = number;
            this.name = name;
        }

        public Tzolkin(Tzolkin other) {
            this.number = other.number;
            this.name = other.name;
        }

        public int getNumber() { return number; }
        public int getName() { return name; }

        public void recompute() {
            // number = amod(longcount + epoch_number, 13)
            number = (int) AlternateCalendar.amod(longcount +
                    (long) MayanCalendar.TZOLKIN_EPOCH.getNumber(), 13L);
            
            // name = amod(longcount + epoch_name, 20)
            name = (int) AlternateCalendar.amod(longcount +
                    (long) MayanCalendar.TZOLKIN_EPOCH.getName(), 20L);
        }

        @Override
        public String toString() {
            try {
                return number + " " + MayanCalendar.TZOLKIN_NAMES[name - 1];
            } catch (Exception e) {
                return "Invalid Date: " + number + " " + name;
            }
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
        MayanCalendar mayan = new MayanCalendar(gregorian);
        System.out.println(gregorian + ": " + mayan);
    }
}
