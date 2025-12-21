/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025 - Silvere Martin-Michiellot (silvere.martin@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.jscience.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import javafx.scene.control.TitledPane;

import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.jscience.ui.chemistry.MolecularViewer;
import org.jscience.ui.chemistry.PeriodicTableViewer;
import org.jscience.ui.physics.astronomy.StarSystemViewer;
import org.jscience.ui.physics.thermodynamics.ThermodynamicsViewer;
import org.jscience.ui.mathematics.fractals.MandelbrotViewer;
import org.jscience.ui.biology.lsystems.LSystemViewer;
import org.jscience.ui.biology.phylogeny.PhylogeneticTreeViewer;
import org.jscience.ui.mathematics.statistics.GaltonBoardViewer;
import org.jscience.ui.physics.mechanics.MechanicsViewer;
import org.jscience.ui.physics.fluids.FluidDynamicsViewer;
import org.jscience.ui.engineering.components.ResistorColorCodeViewer;
import org.jscience.ui.engineering.circuits.LogicGateSimulator;
import org.jscience.ui.computing.GameOfLifeViewer;
import org.jscience.ui.engineering.instruments.OscilloscopeViewer;

/**
 * JScience Master Demo Launcher.
 * Central hub to launch all scientific demos.
 * 
 * @author Silvere Martin-Michiellot
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class JScienceDemoApp extends Application {

        @Override
        public void start(Stage primaryStage) {
                VBox root = new VBox(20);
                root.setPadding(new Insets(20));
                root.setAlignment(Pos.TOP_CENTER);

                Label title = new Label("JScience Demonstration Suite");
                title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

                VBox content = new VBox(15);
                content.getChildren().addAll(
                                createSection("Astronomy",
                                                createLauncher("Star System Viewer",
                                                                () -> new StarSystemViewer().display(new Stage(),
                                                                                null))),
                                createSection("Chemistry",
                                                createLauncher("Molecular Viewer",
                                                                () -> MolecularViewer.show(new Stage())),
                                                createLauncher("Periodic Table",
                                                                () -> PeriodicTableViewer.show(new Stage()))),
                                createSection("Physics",
                                                createLauncher("Thermodynamics Viewer",
                                                                () -> new ThermodynamicsViewer().start(new Stage())),
                                                createLauncher("Mechanics (Mass-Spring)",
                                                                () -> MechanicsViewer.show(new Stage())),
                                                createLauncher("Fluid Dynamics",
                                                                () -> FluidDynamicsViewer.show(new Stage())),
                                                createLauncher("Oscilloscope",
                                                                () -> OscilloscopeViewer.show(new Stage()))),
                                createSection("Biology",
                                                createLauncher("Phylogenetic Tree Viewer",
                                                                () -> PhylogeneticTreeViewer.show(new Stage()))),
                                createSection("Mathematics & Statistics",
                                                createLauncher("Mandelbrot Fractal",
                                                                () -> MandelbrotViewer.show(new Stage())),
                                                createLauncher("L-Systems Viewer",
                                                                () -> LSystemViewer.show(new Stage())),
                                                createLauncher("Galton Board (Stats)",
                                                                () -> GaltonBoardViewer.show(new Stage())),
                                                createLauncher("Matrix Viewer",
                                                                () -> MatrixViewerDemo.show(new Stage()))),
                                createSection("Electronics & Circuits",
                                                createLauncher("Logic Gate Simulator",
                                                                () -> LogicGateSimulator.show(new Stage())),
                                                createLauncher("Resistor Color Code",
                                                                () -> ResistorColorCodeViewer.show(new Stage()))),
                                createSection("Computing",
                                                createLauncher("Game of Life",
                                                                () -> GameOfLifeViewer.show(new Stage()))));

                // Discover additional demos
                java.util.ServiceLoader<DemoProvider> loader = java.util.ServiceLoader.load(DemoProvider.class);
                for (DemoProvider provider : loader) {
                        content.getChildren().add(createSection(provider.getCategory(),
                                        createLauncher(provider.getName(), () -> provider.show(new Stage()))));
                }

                ScrollPane scroll = new ScrollPane(content);
                scroll.setFitToWidth(true);
                root.getChildren().addAll(title, scroll);

                Scene scene = new Scene(root, 600, 800);
                primaryStage.setTitle("JScience Master Demo");
                primaryStage.setScene(scene);
                primaryStage.show();
        }

        private TitledPane createSection(String title, javafx.scene.Node... nodes) {
                VBox box = new VBox(10, nodes);
                box.setPadding(new Insets(10));
                TitledPane pane = new TitledPane(title, box);
                pane.setCollapsible(true);
                pane.setExpanded(true);
                return pane;
        }

        private Button createLauncher(String name, Runnable action) {
                Button btn = new Button(name);
                btn.setMaxWidth(Double.MAX_VALUE);
                btn.setOnAction(e -> action.run());
                return btn;
        }

        public static void main(String[] args) {
                launch(args);
        }
}
