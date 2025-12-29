package org.jscience.ui.demos;

import javafx.stage.Stage;
import org.jscience.ui.DemoProvider;
import org.jscience.ui.history.HistoryTimelineViewer;

public class HistoryTimelineDemo implements DemoProvider {

    @Override
    public String getCategory() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("category.history");
    }

    @Override
    public String getName() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("hist.timeline.title");
    }

    @Override
    public String getDescription() {
        return org.jscience.ui.i18n.SocialI18n.getInstance().get("hist.timeline.desc");
    }

    @Override
    public void show(Stage stage) {
        HistoryTimelineViewer.show(stage);
    }

}
