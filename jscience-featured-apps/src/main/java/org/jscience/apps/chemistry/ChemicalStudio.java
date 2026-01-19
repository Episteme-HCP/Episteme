package org.jscience.apps.chemistry;

import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.jscience.apps.framework.FeaturedAppBase;
import org.jscience.ui.viewers.chemistry.MolecularViewer;

/**
 * Chemical Intelligence Studio.
 * A professional environment for molecular analysis and reaction simulation.
 */
public final class ChemicalStudio extends FeaturedAppBase {

    private MolecularViewer molecularViewer;
    private ReactionKineticsViewer kineticsViewer;

    @Override
    protected String getAppTitle() {
        return "CHEMICAL INTELLIGENCE STUDIO";
    }

    @Override
    protected Region createMainContent() {
        TabPane tabPane = new TabPane();
        tabPane.getStyleClass().add("premium-tab-pane");

        molecularViewer = new MolecularViewer();
        Tab molTab = new Tab("Molecular Structure", molecularViewer);
        molTab.setClosable(false);

        kineticsViewer = new ReactionKineticsViewer();
        Tab kinTab = new Tab("Reaction Kinetics", kineticsViewer);
        kinTab.setClosable(false);

        tabPane.getTabs().addAll(molTab, kinTab);

        VBox root = new VBox(10, tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        return root;
    }

    @Override
    protected void onAppReady() {
        // Initialization logic
    }
}
