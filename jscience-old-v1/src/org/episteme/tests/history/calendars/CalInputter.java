//repackaged after the code from Mark E. Shoulson
//email <mark@kli.org>
//website http://web.meson.org/calendars/
//released under GPL
import java.awt.*;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
abstract class CalInputter extends Panel {
    /**
     * Creates a new CalInputter object.
     */
    CalInputter() {
    }

    /**
     * DOCUMENT ME!
     */
    public abstract void updateCal();

    /**
     * DOCUMENT ME!
     */
    public abstract void updateDisplay();

    /**
     * DOCUMENT ME!
     *
     * @param altcalendar DOCUMENT ME!
     */
    public abstract void changeCal(AltCalendar altcalendar);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract AltCalendar getCal();
}
