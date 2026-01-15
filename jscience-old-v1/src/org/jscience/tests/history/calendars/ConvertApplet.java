//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
import org.jscience.history.calendars.InconsistentDateException;
import org.jscience.history.calendars.MonthDayYear;

import java.applet.Applet;

import java.awt.*;

import java.util.StringTokenizer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ConvertApplet extends Applet {
    /**
     * DOCUMENT ME!
     */
    TextArea res;

    /**
     * DOCUMENT ME!
     */
    AltCalendar[] cals;

    /**
     * DOCUMENT ME!
     */
    int inwhich;

    /**
     * DOCUMENT ME!
     */
    int outwhich;

    /**
     * DOCUMENT ME!
     */
    Choice sel;

    /**
     * DOCUMENT ME!
     */
    Choice inSel;

    /**
     * DOCUMENT ME!
     */
    CalInputter[] calsel;

    /**
     * DOCUMENT ME!
     */
    Panel top;

    /**
     * DOCUMENT ME!
     */
    Panel selArea;

    /**
     * DOCUMENT ME!
     */
    final Color normalCol;

    /**
     * DOCUMENT ME!
     */
    final Color errorCol;

    /**
     * Creates a new ConvertApplet object.
     */
    public ConvertApplet() {
        cals = new AltCalendar[16];
        normalCol = Color.black;
        errorCol = Color.red;
    }

    /**
     * DOCUMENT ME!
     *
     * @param event DOCUMENT ME!
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean action(Event event, Object obj) {
        int i = inwhich;

        try {
            if (event.target == inSel) {
                inwhich = inSel.getSelectedIndex();

                CalInputter calinputter = calsel[inwhich];
                calinputter.changeCal(calsel[i].getCal());
                ((CardLayout) selArea.getLayout()).show(selArea, (String) obj);
            } else {
                CalInputter calinputter1 = calsel[inwhich];
                outwhich = sel.getSelectedIndex();
                calinputter1.updateCal();
                calinputter1.updateDisplay();
                cals[outwhich].set(calinputter1.getCal().toRD());
            }

            res.setForeground(normalCol);
            res.setText(breakLines(cals[outwhich].toString(), res));

            boolean _tmp = true;
        } catch (InconsistentDateException inconsistentdateexception) {
            res.setForeground(errorCol);
            res.setText("Inconsistent Date: " +
                inconsistentdateexception.getMessage());
            calsel[inwhich].changeCal(calsel[inwhich].getCal());
        } catch (ArrayIndexOutOfBoundsException _ex) {
            outwhich = 0;
            inwhich = 0;
            cals[outwhich].set(calsel[inwhich].getCal().toRD());
            res.setForeground(errorCol);
            res.setText(breakLines(cals[outwhich].toString(), res));
        } finally {
            inSel.select(inwhich);
            sel.select(outwhich);

            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param component DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String breakLines(String s, Component component) {
        StringTokenizer stringtokenizer = new StringTokenizer(s);
        byte byte0 = 40;
        int j = component.size().width - byte0;
        FontMetrics fontmetrics = component.getFontMetrics(component.getFont());
        String s1 = "";
        int i = 0;

        while (stringtokenizer.hasMoreTokens()) {
            String s2 = stringtokenizer.nextToken();
            int k = fontmetrics.stringWidth(((i <= 0) ? "" : " ") + s2);

            if ((i + k) < j) {
                s1 = s1 + ((i <= 0) ? "" : " ") + s2;
                i += k;
            } else {
                s1 = s1 + "\n" + s2;
                i = k;
            }
        }

        return s1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     * @param s DOCUMENT ME!
     * @param monthdayyear DOCUMENT ME!
     */
    private void addInputMDY(int i, String s, MonthDayYear monthdayyear) {
        inSel.addItem(s);
        calsel[i] = new MDYInput(monthdayyear);
        selArea.add(s, calsel[i]);
    }

    /**
     * DOCUMENT ME!
     */
    public void init() {
        res = new TextArea(30, 4);
        res.setEditable(false);
        res.setFont(new Font("TimesRoman", 1, 24));
        sel = new Choice();

        int i = 0;
        sel.addItem("Gregorian");
        cals[i++] = new Gregorian();
        sel.addItem("French Revolutionary");
        cals[i++] = new French();
        sel.addItem("Modified French Revolutionary");
        cals[i++] = new ModFrench();
        sel.addItem("Julian");
        cals[i++] = new Julian();
        sel.addItem("Hebrew");
        cals[i++] = new Hebrew();
        sel.addItem("Islamic");
        cals[i++] = new Islamic();
        sel.addItem("Persian");
        cals[i++] = new Persian();
        sel.addItem("Coptic");
        cals[i++] = new Coptic();
        sel.addItem("Ethiopic");
        cals[i++] = new Ethiopic();
        sel.addItem("Bah\341'\355");
        cals[i++] = new Bahai();
        sel.addItem("Mayan");
        cals[i++] = new Mayan();
        sel.addItem("Chinese");
        cals[i++] = new Chinese();
        sel.addItem("Hindu Solar");
        cals[i++] = new ModHinduSolar();
        sel.addItem("Hindu Lunar");
        cals[i++] = new ModHinduLunar();
        sel.addItem("Old Hindu Solar");
        cals[i++] = new OldHinduSolar();
        sel.addItem("Old Hindu Lunar");
        cals[i++] = new OldHinduLunar();
        setLayout(new BorderLayout());
        calsel = new CalInputter[13];
        inSel = new Choice();
        top = new Panel();
        top.setLayout(new BorderLayout(10, 0));
        selArea = new Panel();
        selArea.setLayout(new CardLayout());
        i = 0;
        addInputMDY(i++, "Gregorian", new Gregorian());
        addInputMDY(i++, "Hebrew", new Hebrew());
        addInputMDY(i++, "Islamic", new Islamic());
        addInputMDY(i++, "Julian", new Julian());
        addInputMDY(i++, "Persian", new Persian());
        addInputMDY(i++, "Coptic", new Coptic());
        addInputMDY(i++, "Ethiopic", new Ethiopic());
        addInputMDY(i++, "French", new French());
        addInputMDY(i++, "Mod.Fr.Rev.", new ModFrench());
        addInputMDY(i++, "Hindu Solar", new ModHinduSolar());
        inSel.addItem("Hind.Lun.");
        calsel[i] = new ModHindLunarIn(new ModHinduLunar());
        selArea.add("Hind.Lun.", calsel[i]);
        i++;
        addInputMDY(i++, "OldHind.Sol.", new OldHinduSolar());
        inSel.addItem("OldHind.Lun.");
        calsel[i] = new OldHindLunarIn(new OldHinduLunar());
        selArea.add("OldHind.Lun.", calsel[i]);
        i++;
        top.add("West", inSel);
        top.add("Center", selArea);
        add("North", top);
        add("Center", res);
        add("South", sel);
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        GregorianCalendar gregoriancalendar = new GregorianCalendar();
        calsel[inwhich].changeCal(new Gregorian(gregoriancalendar.get(2) + 1,
                gregoriancalendar.get(5), gregoriancalendar.get(1)));
        cals[outwhich].set(calsel[inwhich].getCal().toRD());
        res.setText(breakLines(cals[outwhich].toString(), res));
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new ConvertApplet();
    }
}
