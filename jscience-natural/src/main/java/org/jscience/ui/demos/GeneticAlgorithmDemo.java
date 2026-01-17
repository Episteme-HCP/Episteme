/*
 * JScience - Java(TM) Tools and Libraries for the Advancement of Sciences.
 * Copyright (C) 2025-2026 - Silvere Martin-Michiellot and Gemini AI (Google DeepMind)
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

package org.jscience.ui.demos;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.jscience.ui.AbstractSimulationDemo;
import org.jscience.ui.AbstractViewer;
import org.jscience.ui.Simulatable;
import org.jscience.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Genetic Algorithm Demo.
 * 
 * @author Silvere Martin-Michiellot
 * <p>
 * <b>Reference:</b><br>
 * Holland, J. H. (1975). <i>Adaptation in Natural and Artificial Systems</i>. University of Michigan Press.
 * </p>
 *
 * @author Gemini AI (Google DeepMind)
 * @since 1.0
 */
public class GeneticAlgorithmDemo extends AbstractSimulationDemo {

    @Override
    public String getCategory() { return "Computing"; }

    @Override
    public String getName() { return org.jscience.ui.i18n.I18n.getInstance().get("geneticalgo.title", "Genetic Algorithm"); }

    @Override
    public String getDescription() { return org.jscience.ui.i18n.I18n.getInstance().get("geneticalgo.desc", "Evolving Pathfinders using Genetic Algorithm"); }

    @Override
    public String getLongDescription() {
        return "A simulation of autonomous agents evolving to reach a target using genetic algorithms (Selection, Crossover, Mutation).";
    }

    @Override
    public Node createViewerNode() {
        return new InternalGeneticViewer();
    }

    private static class InternalGeneticViewer extends AbstractViewer implements Simulatable {

        private final int popSize = 50;
        private int lifespan = 200;
        private int count = 0;
        private int generation = 1;
        private int reachedCount = 0;
        private double bestFitness = 0;
        private int speed = 1;
        private boolean paused = true; // Start paused or running? AbstractSimulationDemo starts paused usually.
        private final Random rand = new Random();

        private final double targetX = 400, targetY = 50;
        private List<DNA> population = new ArrayList<>();
        private Canvas canvas;
        
        // UI Labels
        private Label genLabel, fitLabel, reachLabel;

        public InternalGeneticViewer() {
            canvas = new Canvas(800, 600);
            setCenter(canvas);

            VBox sidebar = new VBox(15);
            sidebar.setPadding(new Insets(15));
            sidebar.setPrefWidth(180);
            sidebar.getStyleClass().add("viewer-sidebar");

            Label title = new Label(org.jscience.ui.i18n.I18n.getInstance().get("geneticalgo.stats", "Statistics"));
            title.getStyleClass().add("header-label");

            genLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.geneticalgorithm.generation.1", "Generation: 1"));
            genLabel.getStyleClass().add("description-label");

            fitLabel = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.geneticalgorithm.fitness.000", "Fitness: 0.00"));
            fitLabel.getStyleClass().add("description-label");

            reachLabel = new Label("Reached: 0/" + popSize);
            reachLabel.getStyleClass().add("description-label");
            
            Label info = new Label(org.jscience.ui.i18n.I18n.getInstance().get("generated.geneticalgorithm.controls.below", "Controls below."));
            info.getStyleClass().add("description-label");

            sidebar.getChildren().addAll(title, new Separator(), genLabel, fitLabel, reachLabel, new Separator(), info);
            setRight(sidebar);

            restart();

            // Interaction
            canvas.setOnMouseClicked(e -> {
                double mx = e.getX(), my = e.getY();
                DNA closest = null; double minDist = 50;
                for (DNA d : population) {
                    double dist = Math.sqrt(Math.pow(d.x - mx, 2) + Math.pow(d.y - my, 2));
                    if (dist < minDist) { minDist = dist; closest = d; }
                }
                if (closest != null) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setTitle(org.jscience.ui.i18n.I18n.getInstance().get("generated.geneticalgorithm.genome.detail", "Genome Detail")); a.setHeaderText(org.jscience.ui.i18n.I18n.getInstance().get("generated.geneticalgorithm.dna.info", "DNA Info"));
                    a.setContentText(String.format("Fitness: %.4f\nReached: %b\nCrashed: %b", closest.fitness, closest.reached, closest.crashed));
                    a.showAndWait();
                }
            });

            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    if (!paused) {
                        for (int s = 0; s < speed; s++) {
                            if (count < lifespan) {
                                updatePhysics();
                                count++;
                            } else {
                                evaluate();
                                selection();
                                generation++;
                                updateLabels();
                                count = 0;
                            }
                        }
                    }
                    render();
                }
            }.start();
        }
        
        private void updateLabels() {
            genLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("geneticalgo.generation", "Generation") + " " + generation);
            fitLabel.setText(String.format("Best Fitness: %.2f", bestFitness));
            reachLabel.setText(org.jscience.ui.i18n.I18n.getInstance().get("geneticalgo.reached", "Reached") + " " + reachedCount + "/" + popSize);
        }

        private void restart() {
            population.clear();
            for (int i = 0; i < popSize; i++) population.add(new DNA(lifespan));
            generation = 1; count = 0;
            updateLabels();
            render();
        }

        @Override public void play() { paused = false; }
        @Override public void pause() { paused = true; }
        @Override public void stop() { paused = true; restart(); }
        @Override public void step() { /* Not easily steppable per tick? Or allow single tick? */ }
        @Override public void setSpeed(double s) { speed = (int)(s * 5); if(speed<1) speed=1; } 
        // AbstractSimulationDemo default speed range 0.0-2.0? Or 0-100?
        // Usually 0.1 to 5.0. 
        // Original slider was 1-5. 
        // If s is 1.0 -> speed 5? No.
        
        @Override public boolean isPlaying() { return !paused; }

        private static class DNA {
            double[] genes;
            double x = 400, y = 550;
            double fitness = 0;
            boolean reached = false, crashed = false;

            DNA(int length) {
                genes = new double[length * 2];
                for (int i = 0; i < genes.length; i++) genes[i] = (Math.random() - 0.5) * 10;
            }
            DNA(double[] genes) { this.genes = genes; }
        }

        private void updatePhysics() {
            for (DNA d : population) {
                if (d.reached || d.crashed) continue;
                d.x += d.genes[count * 2]; d.y += d.genes[count * 2 + 1];
                if (Math.hypot(d.x - targetX, d.y - targetY) < 20) d.reached = true;
                if (d.x < 0 || d.x > 800 || d.y < 0 || d.y > 600) d.crashed = true;
            }
        }

        private void evaluate() {
            bestFitness = 0; reachedCount = 0;
            for (DNA d : population) {
                double dist = Math.hypot(d.x - targetX, d.y - targetY);
                d.fitness = 1.0 / (dist + 1);
                if (d.reached) { d.fitness *= 10; reachedCount++; }
                if (d.crashed) d.fitness /= 2;
                bestFitness = Math.max(bestFitness, d.fitness);
            }
        }

        private void selection() {
            List<DNA> nextGen = new ArrayList<>();
            List<DNA> pool = new ArrayList<>();
            double maxFit = population.stream().mapToDouble(d -> d.fitness).max().orElse(1.0);
            for (DNA d : population) {
                int n = (int) ((d.fitness / maxFit) * 100);
                for (int j = 0; j < n; j++) pool.add(d);
            }
            if (pool.isEmpty()) pool.addAll(population);

            for (int i = 0; i < popSize; i++) {
                DNA parentA = pool.get(rand.nextInt(pool.size()));
                DNA parentB = pool.get(rand.nextInt(pool.size()));
                DNA child = crossover(parentA, parentB);
                mutate(child);
                nextGen.add(child);
            }
            population = nextGen;
        }

        private DNA crossover(DNA a, DNA b) {
            double[] genes = new double[lifespan * 2];
            int mid = rand.nextInt(genes.length);
            for (int i = 0; i < genes.length; i++) genes[i] = (i < mid) ? a.genes[i] : b.genes[i];
            return new DNA(genes);
        }

        private void mutate(DNA d) {
            for (int i = 0; i < d.genes.length; i++) {
                if (rand.nextDouble() < 0.01) d.genes[i] = (rand.nextDouble() - 0.5) * 10;
            }
        }

        private void render() {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.web("#1a1a2e")); gc.fillRect(0, 0, 800, 600);
            gc.setFill(Color.web("#ff6b6b")); gc.fillOval(targetX - 15, targetY - 15, 30, 30);
            gc.setFill(Color.RED); gc.fillOval(targetX - 10, targetY - 10, 20, 20);
            for (DNA d : population) {
                if (d.reached) gc.setFill(Color.LIMEGREEN);
                else if (d.crashed) gc.setFill(Color.web("#333"));
                else gc.setFill(Color.web("#4dabf7"));
                gc.fillOval(d.x - 4, d.y - 4, 8, 8);
            }
            gc.setStroke(Color.web("#666")); gc.strokeOval(400 - 12, 550 - 12, 24, 24);
        }
        
        @Override public String getName() { return "Genetic Viewer"; }
    @Override
    public String getCategory() { return "Computing"; }
    
        @Override
        public String getDescription() { return "InternalGeneticViewer Internal Viewer"; }

        @Override
        public String getLongDescription() { return getDescription(); }

        

    @Override public java.util.List<org.jscience.ui.Parameter<?>> getViewerParameters() { return new java.util.ArrayList<>(); }
}
}
