package org.jscience.apps.shared;

import javafx.scene.layout.*;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.ui.viewers.shared.KnowledgeNavigatorViewer;

/**
 * Science Browser: The ultimate JScience knowledge navigator.
 */
public final class ScienceBrowser extends FeaturedAppBase {

    private KnowledgeNavigatorViewer navigator;

    @Override
    protected String getAppTitle() {
        return "JSCIENCE BROWSER PRO";
    }

    @Override
    protected Region createMainContent() {
        navigator = new KnowledgeNavigatorViewer();
        return navigator;
    }

    @Override
    protected void onAppReady() {
        // Initialization
    }
}
