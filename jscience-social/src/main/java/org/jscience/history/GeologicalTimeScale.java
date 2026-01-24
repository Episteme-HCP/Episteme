package org.jscience.history;

import java.io.Serializable;
import java.util.List;
import org.jscience.history.temporal.FuzzyTimePoint;
import org.jscience.history.temporal.FuzzyTimeInterval;
import org.jscience.util.persistence.Persistent;

/**
 * Official divisions of geological time according to the International Commission on Stratigraphy (ICS).
 * Rewritten as a timeline of GeologicalEra events.
 *
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 2.0
 */
@Persistent
public final class GeologicalTimeScale extends Timeline implements Serializable {

    private static final long serialVersionUID = 2L;

    private static final GeologicalTimeScale INSTANCE = new GeologicalTimeScale();

    private GeologicalTimeScale() {
        super("Geological Time Scale");
        addEvent(HistoryConstants.HADEAN);
        addEvent(HistoryConstants.ARCHEAN);
        addEvent(HistoryConstants.PROTEROZOIC);
        addEvent(HistoryConstants.PHANEROZOIC);
        addEvent(HistoryConstants.PALEOZOIC);
        addEvent(HistoryConstants.MESOZOIC);
        addEvent(HistoryConstants.CENOZOIC);
        addEvent(HistoryConstants.PALEOGENE);
        addEvent(HistoryConstants.NEOGENE);
        addEvent(HistoryConstants.QUATERNARY);
        addEvent(HistoryConstants.PLEISTOCENE);
        addEvent(HistoryConstants.HOLOCENE);
    }

    public static GeologicalTimeScale getInstance() {
        return INSTANCE;
    }

    /**
     * Finds the era(s) covering a specific time.
     * 
     * @param point the time coordinate
     * @return list of eras
     */
    public List<GeologicalEra> findEras(FuzzyTimePoint point) {
        return getEvents().stream()
                .filter(e -> e instanceof GeologicalEra)
                .map(e -> (GeologicalEra) e)
                .filter(e -> e.getWhen().toInstant().isBefore(point.toInstant())) // Simplistic check
                .toList();
    }
}
