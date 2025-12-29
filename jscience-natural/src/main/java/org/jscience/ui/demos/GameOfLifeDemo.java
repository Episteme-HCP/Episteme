package org.jscience.ui.demos;

import javafx.scene.Node;
import org.jscience.ui.SimulationDemo;
import org.jscience.ui.computing.ai.GameOfLifeViewer;
import org.jscience.ui.i18n.I18n;

public class GameOfLifeDemo extends SimulationDemo {
    @Override
    public String getCategory() {
        return I18n.getInstance().get("category.computing");
    }

    @Override
    public String getName() {
        return I18n.getInstance().get("life.title");
    }

    @Override
    public String getDescription() {
        return I18n.getInstance().get("life.short_desc", "Conway's Game of Life");
    }

    @Override
    protected String getLongDescription() {
        return "Conway's Game of Life is a cellular automaton that demonstrates how complex patterns " +
                "can emerge from simple rules. Each cell follows three rules based on its neighbors: " +
                "survival, birth, or death by isolation or overpopulation. This interactive visualization " +
                "allows you to explore different starting densities and colors.";
    }

    @Override
    protected Node createViewerNode() {
        GameOfLifeViewer viewer = new GameOfLifeViewer();
        viewer.play();
        return viewer;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
